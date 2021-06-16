package io.github.aquerr.koth;

import com.google.inject.Inject;
import io.github.aquerr.koth.command.DeselectCommand;
import io.github.aquerr.koth.command.HelpCommand;
import io.github.aquerr.koth.command.ListArenaClassesCommand;
import io.github.aquerr.koth.command.VersionCommand;
import io.github.aquerr.koth.command.WandCommand;
import io.github.aquerr.koth.config.Configuration;
import io.github.aquerr.koth.config.ConfigurationImpl;
import io.github.aquerr.koth.listener.CommandUsageListener;
import io.github.aquerr.koth.listener.EntitySpawnListener;
import io.github.aquerr.koth.listener.PlayerAttackListener;
import io.github.aquerr.koth.listener.PlayerDisconnectListener;
import io.github.aquerr.koth.listener.PlayerMoveListener;
import io.github.aquerr.koth.listener.SignClickListener;
import io.github.aquerr.koth.listener.WandUsageListener;
import io.github.aquerr.koth.manager.ArenaClassManager;
import io.github.aquerr.koth.manager.SelectionManager;
import io.github.aquerr.koth.util.SelectionPoints;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

import java.nio.file.Path;
import java.util.*;

@Plugin("koth")
public class Koth
{
    private static Koth INSTANCE;

    private final Map<List<String>, Command.Parameterized> subcommands = new HashMap<>();
    private final EventManager eventManager;
    private final Path configDir;


//    private final Map<UUID, Arena> playersCreatingArena = new HashMap<>();
//    private final Map<UUID, Arena> playersEditingArena = new HashMap<>();

//    private final Map<UUID, Arena> playersPlayingOnArena = new HashMap<>();

    private Configuration configuration;

//    @Inject
//    private ArenaManager arenaManager;

    @Inject
    private ArenaClassManager arenaClassManager;

    @Inject
    private SelectionManager selectionManager;

    private final PluginContainer pluginContainer;

    @Inject
    private Koth(final PluginContainer pluginContainer, final EventManager eventManager, @ConfigDir(sharedRoot = false) final Path configDir)
    {
        this.pluginContainer = pluginContainer;
        this.eventManager = eventManager;
        this.configDir = configDir;
        INSTANCE = this;
    }

    public static Koth getInstance()
    {
        return INSTANCE;
    }

    @Listener
    public void onInitialization(final ConstructPluginEvent event)
    {
        try
        {
            this.pluginContainer.logger().info("Loading Koth listeners and commands...");

            //Setup storage and managers first so that listeners and commands can use them later.
            registerTypeSerializers();
            this.configuration = new ConfigurationImpl(this, this.configDir);
//            arenaManager.reloadCache();
            arenaClassManager.reloadCache();

            //Register commands and listeners
            registerListeners();

            //If something went wrong, disable plugin.

            this.pluginContainer.logger().info("Koth loaded successfully! Time for a fight on the arena!");
        }
        catch (final Exception exception)
        {
            disablePlugin();
        }
    }

    @Listener
    public void onCommandRegister(final RegisterCommandEvent<Command.Parameterized> event)
    {
        registerCommands(event);
        this.pluginContainer.logger().info("Commands loaded.");
    }

    @Listener
    public void onGameReload(final RefreshGameEvent event)
    {
        this.configuration.reload();
//        this.arenaManager.reloadCache();
        this.arenaClassManager.reloadCache();
    }

    private void registerTypeSerializers()
    {
//        TypeSerializerCollection.defaults().childBuilder().register(TypeToken.get(Hill.class), new HillTypeSerializer());
//        TypeSerializerCollection.defaults().childBuilder().register(TypeToken.get(ArenaTeam.class), new ArenaTeamTypeSerializer());
//        TypeSerializerCollection.defaults().childBuilder().register(TypeToken.get(Lobby.class), new LobbyTypeSerializer());
//        TypeSerializerCollection.defaults().childBuilder().register(TypeToken.get(ArenaProperties.class), new ArenaPropertiesTypeSerializer());
    }

    public Configuration getConfiguration()
    {
        return this.configuration;
    }

//    public ArenaManager getArenaManager()
//    {
//        return this.arenaManager;
//    }

    public ArenaClassManager getArenaClassManager()
    {
        return this.arenaClassManager;
    }

//    public Map<UUID, Arena> getPlayersCreatingArena()
//    {
//        return this.playersCreatingArena;
//    }
//
//    public Map<UUID, Arena> getPlayersEditingArena()
//    {
//        return this.playersEditingArena;
//    }

//    public Map<UUID, Arena> getPlayersPlayingOnArena()
//    {
//        return this.playersPlayingOnArena;
//    }

    private void registerListeners()
    {
        this.eventManager.registerListeners(this.pluginContainer, new WandUsageListener(this.selectionManager));
        this.eventManager.registerListeners(this.pluginContainer, new PlayerDisconnectListener(this.selectionManager));
        this.eventManager.registerListeners(this.pluginContainer, new PlayerMoveListener());
        this.eventManager.registerListeners(this.pluginContainer, new EntitySpawnListener());
        this.eventManager.registerListeners(this.pluginContainer, new PlayerAttackListener());
        this.eventManager.registerListeners(this.pluginContainer, new SignClickListener());
        this.eventManager.registerListeners(this.pluginContainer, new CommandUsageListener());

        //Arena events

//        this.eventManager.registerListeners(this, new ArenaJoinLeaveListener(this));
//        this.eventManager.registerListeners(this, new ArenaStartStopListener(this));
//        this.eventManager.registerListeners(this, new ArenaStatusChangeListener(this));
//        this.eventManager.registerListeners(this, new ArenaHillCaptureListener(this));
    }

    private void registerCommands(RegisterCommandEvent<Command.Parameterized> event)
    {
        //Help Command
        this.subcommands.put(Collections.singletonList("help"), Command.builder()
            .shortDescription(Component.text("Show all available commands"))
            .permission(PluginPermissions.HELP_COMMAND)
            .addParameter(Parameter.integerNumber().key("page").optional().build())
            .executor(new HelpCommand(this))
            .build());

        //Wand Command
        this.subcommands.put(Collections.singletonList("wand"), Command.builder()
            .shortDescription(Component.text("Gives player a KOTH builder wand."))
            .permission(PluginPermissions.WAND_COMMAND)
            .executor(new WandCommand())
            .build());

        //Deselect Command
        this.subcommands.put(Arrays.asList("deselect", "desel"), Command.builder()
            .shortDescription(Component.text("Clears wand selection points"))
            .permission(PluginPermissions.DESELECT_COMMAND)
            .executor(new DeselectCommand(this.selectionManager))
            .build());
//
//        //Create Arena Class Command
//        this.subcommands.put(Collections.singletonList("createclass"), CommandSpec.builder()
//                .description(Text.of("Creates an arena class"))
//                .permission(PluginPermissions.CREATE_CLASS_COMMAND)
//                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
//                .executor(new CreateArenaClassCommand(this))
//                .build());
//
//        //Delete Arena Class Command
//        this.subcommands.put(Collections.singletonList("deleteclass"), CommandSpec.builder()
//            .description(Text.of("Deletes a specified class."))
//            .permission(PluginPermissions.DELETE_CLASS_COMMAND)
//            .arguments(new ArenaClassNameArgument(this, Text.of("name")))
//            .executor(new DeleteArenaClassCommand(this))
//            .build());
//
        //List Arena Classes Command
        this.subcommands.put(Collections.singletonList("classes"), Command.builder()
            .shortDescription(Component.text("List all classes registered on the server."))
            .permission(PluginPermissions.LIST_CLASSES_COMMAND)
            .executor(new ListArenaClassesCommand(this.arenaClassManager))
            .build());
//
//		//Create Arena Command
//		this.subcommands.put(Collections.singletonList("createarena"), CommandSpec.builder()
//				.description(Text.of("Creates an arena"))
//				.permission(PluginPermissions.CREATE_ARENA_COMMAND)
//				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
//						GenericArguments.onlyOne(GenericArguments.enumValue(Text.of("type"), ArenaType.class)))
//				.executor(new CreateArenaCommand(this))
//				.build());
//
//		//Create Delete Arena
//		this.subcommands.put(Collections.singletonList("deletearena"), CommandSpec.builder()
//				.description(Text.of("Delete an arena"))
//				.permission(PluginPermissions.DELETE_ARENA_COMMAND)
//				.arguments(new ArenaNameArgument(this, Text.of("name")))
//				.executor(new DeleteArenaCommand(this))
//				.build());
//
//        //List Arenas Command
//        this.subcommands.put(Collections.singletonList("list"), CommandSpec.builder()
//            .description(Text.of("List all arenas on the server"))
//            .permission(PluginPermissions.LIST_COMMAND)
//            .executor(new ListCommand(this))
//            .build());
//
//        //Create Hill Command
//        this.subcommands.put(Collections.singletonList("createhill"), CommandSpec.builder()
//            .description(Text.of("Creates a hill in arena"))
//            .permission(PluginPermissions.CREATE_HILL_COMMAND)
//            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
//            .executor(new CreateHillCommand(this))
//            .build());
//
//        //Set Lobby Command
//        this.subcommands.put(Collections.singletonList("createlobby"), CommandSpec.builder()
//            .description(Text.of("Creates arena lobby with selected selection points."))
//            .permission(PluginPermissions.CREATE_LOBBY_COMMAND)
//            .executor(new CreateLobbyCommand(this))
//            .build());
//
//        //Set Lobby Spawn Command
//        this.subcommands.put(Collections.singletonList("createlobbyspawn"), CommandSpec.builder()
//                .description(Text.of("Creates spawn arena lobby"))
//                .permission(PluginPermissions.CREATE_LOBBYSPAWN_COMMAND)
//                .executor(new CreateLobbySpawnCommand(this))
//                .build());
//
//        //Edit Arena Command
//        this.subcommands.put(Collections.singletonList("edit"), CommandSpec.builder()
//            .description(Text.of("Turns on/off edit mode for the given arena."))
//            .permission(PluginPermissions.EDIT_COMMAND)
//            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
//            .executor(new EditArenaCommand(this))
//            .build());
//
//        //Create Team Spawn
//        this.subcommands.put(Collections.singletonList("createteamspawn"), CommandSpec.builder()
//                .description(Text.of("Create team spawn"))
//                .permission(PluginPermissions.CREATE_TEAM_SPAWN_COMMAND)
//                .arguments(GenericArguments.onlyOne(new ArenaTeamArgument(this, Text.of("team"))))
//                .executor(new CreateTeamSpawnCommand(this))
//                .build());
//
//        //Add Team Command
//        this.subcommands.put(Collections.singletonList("addteam"), CommandSpec.builder()
//            .description(Text.of("Adds a team to the arena"))
//            .permission(PluginPermissions.ADD_TEAM_COMMAND)
//            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
//            .executor(new AddTeamCommand(this))
//            .build());
//
//        //Remove Team Command
//        this.subcommands.put(Collections.singletonList("removeteam"), CommandSpec.builder()
//                .description(Text.of("Removes a team from the arena"))
//                .permission(PluginPermissions.REMOVE_TEAM_COMMAND)
//                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
//                .executor(new RemoveTeamCommand(this))
//                .build());
//
//        //Join Command
//        this.subcommands.put(Collections.singletonList("join"), CommandSpec.builder()
//            .description(Text.of("Join arena."))
//            .permission(PluginPermissions.JOIN_COMMAND)
//            .arguments(GenericArguments.onlyOne(new ArenaNameArgument(this, Text.of("name"))))
//            .executor(new JoinCommand(this))
//            .build());
//
//        //Leave Command
//        this.subcommands.put(Collections.singletonList("leave"), CommandSpec.builder()
//                .description(Text.of("Leave arena."))
//                .permission(PluginPermissions.LEAVE_COMMAND)
//                .executor(new LeaveCommand(this))
//                .build());
//
        //Version Command
        this.subcommands.put(Collections.singletonList("version"), Command.builder()
                .shortDescription(Component.text("View version of the plugin"))
                .permission(PluginPermissions.VERSION_COMMAND)
                .executor(new VersionCommand())
                .build());

        //KOTH Commands
        final Command.Parameterized kothCommand = Command.builder()
                .shortDescription(Component.text("Shows all commands in KOTH plugin"))
                .addChildren(this.subcommands)
                .executor(new HelpCommand(this))
                .build();

        event.register(this.pluginContainer, kothCommand, "koth");
    }

    /**
     * Method used to disable KOTH plugin.
     * Unregisters plugin listeners, commands and clears instance objects objects.
     */
    private void disablePlugin()
    {
        //TODO: Remove command mappings

//        final Set<CommandMapping> commandMappings = this..getOwnedBy(this);
//        for (final CommandMapping commandMapping : commandMappings)
//        {
//            this.commandManager.removeMapping(commandMapping);
//        }

        this.subcommands.clear();
    }

    public Path getConfigDir()
    {
        return this.configDir;
    }

    public Map<List<String>, Command.Parameterized> getSubcommands()
    {
        return this.subcommands;
    }

    public PluginContainer getPluginContainer()
    {
        return this.pluginContainer;
    }

    public SelectionManager getSelectionManager()
    {
        return selectionManager;
    }
}
