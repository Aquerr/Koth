package io.github.aquerr.koth;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.github.aquerr.koth.command.*;
import io.github.aquerr.koth.command.arguments.ArenaClassNameArgument;
import io.github.aquerr.koth.command.arguments.ArenaNameArgument;
import io.github.aquerr.koth.entity.*;
import io.github.aquerr.koth.listener.EntitySpawnListener;
import io.github.aquerr.koth.listener.PlayerAttackListener;
import io.github.aquerr.koth.listener.PlayerLeaveListener;
import io.github.aquerr.koth.listener.PlayerMoveListener;
import io.github.aquerr.koth.listener.WandUsageListener;
import io.github.aquerr.koth.manager.ArenaClassManager;
import io.github.aquerr.koth.manager.ArenaManager;
import io.github.aquerr.koth.storage.serializer.ArenaTeamTypeSerializer;
import io.github.aquerr.koth.storage.serializer.HillTypeSerializer;
import io.github.aquerr.koth.storage.serializer.LobbyTypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.util.*;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION, authors = {"Aquerr/Nerdi"})
public class Koth {

    private final Map<List<String>, CommandCallable> subcommands = new HashMap<>();
    private final CommandManager commandManager;
    private final EventManager eventManager;
    private final Path configDir;

    private final Map<UUID, SelectionPoints> playerSelectionPoints = new HashMap<>();

    private final Map<UUID, Arena> playersCreatingArena = new HashMap<>();
    private final Map<UUID, Arena> playersEditingArena = new HashMap<>();

    private final Map<UUID, Arena> playersPlayingOnArena = new HashMap<>();

    @Inject
    private ArenaManager arenaManager;

    @Inject
    private ArenaClassManager arenaClassManager;

    @Inject
    private Koth(final CommandManager commandManager, final EventManager eventManager, @ConfigDir(sharedRoot = false) final Path configDir)
    {
        this.commandManager = commandManager;
        this.eventManager = eventManager;
        this.configDir = configDir;
    }

    @Listener
    public void onInitialization(final GameInitializationEvent event)
    {
        try
        {
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.YELLOW, "Loading Koth listeners and commands..."));

            //Setup storage and managers first so that listeners and commands can use them later.
            registerTypeSerializers();
            arenaManager.reloadCache();
            arenaClassManager.reloadCache();

            //Register commands and listeners
            registerListeners();
            registerCommands();

            //If something went wrong, disable plugin.

            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "Koth loaded successfully! Time for a fight on the arena!"));
        }
        catch (final Exception exception)
        {
            disablePlugin();
        }
    }

    private void registerTypeSerializers()
    {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Hill.class), new HillTypeSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ArenaTeam.class), new ArenaTeamTypeSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Lobby.class), new LobbyTypeSerializer());
    }

    public ArenaManager getArenaManager()
    {
        return this.arenaManager;
    }

    public ArenaClassManager getArenaClassManager()
    {
        return this.arenaClassManager;
    }

    public Map<UUID, SelectionPoints> getPlayerSelectionPoints()
    {
        return this.playerSelectionPoints;
    }

    public Map<UUID, Arena> getPlayersCreatingArena()
    {
        return this.playersCreatingArena;
    }

    public Map<UUID, Arena> getPlayersEditingArena()
    {
        return this.playersEditingArena;
    }

    public Map<UUID, Arena> getPlayersPlayingOnArena()
    {
        return this.playersPlayingOnArena;
    }

    private void registerListeners()
    {
        this.eventManager.registerListeners(this, new WandUsageListener(this));
        this.eventManager.registerListeners(this, new PlayerLeaveListener(this));
        this.eventManager.registerListeners(this, new PlayerMoveListener(this));
        this.eventManager.registerListeners(this, new EntitySpawnListener(this));
        this.eventManager.registerListeners(this, new PlayerAttackListener(this));
    }

    private void registerCommands()
    {
        //Help Command
        this.subcommands.put(Collections.singletonList("help"), CommandSpec.builder()
            .description(Text.of("Show all available commands"))
            .permission(PluginPermissions.HELP_COMMAND)
            .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page"))))
            .executor(new HelpCommand(this))
            .build());

        //Wand Command
        this.subcommands.put(Collections.singletonList("wand"), CommandSpec.builder()
            .description(Text.of("Gives player a KOTH builder wand."))
            .permission(PluginPermissions.WAND_COMMAND)
            .executor(new WandCommand(this))
            .build());

        //Deselect Command
        this.subcommands.put(Arrays.asList("deselect", "desel"), CommandSpec.builder()
            .description(Text.of("Clears wand selection points"))
            .permission(PluginPermissions.DESELECT_COMMAND)
            .executor(new DeselectCommand(this))
            .build());

        //Create Arena Class Command
        this.subcommands.put(Collections.singletonList("createclass"), CommandSpec.builder()
                .description(Text.of("Creates an arena class"))
                .permission(PluginPermissions.CREATE_CLASS_COMMAND)
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .executor(new CreateArenaClassCommand(this))
                .build());

        //Delete Arena Class Command
        this.subcommands.put(Collections.singletonList("deleteclass"), CommandSpec.builder()
            .description(Text.of("Deletes a specified class."))
            .permission(PluginPermissions.DELETE_CLASS_COMMAND)
            .arguments(new ArenaClassNameArgument(this, Text.of("name")))
            .executor(new DeleteArenaClassCommand(this))
            .build());

        //List Arena Classes Command
        this.subcommands.put(Collections.singletonList("classes"), CommandSpec.builder()
            .description(Text.of("List all classes registered on the server."))
            .permission(PluginPermissions.LIST_CLASSES_COMMAND)
            .executor(new ListArenaClassesCommand(this))
            .build());

		//Create Arena Command
		this.subcommands.put(Collections.singletonList("createarena"), CommandSpec.builder()
				.description(Text.of("Creates an arena"))
				.permission(PluginPermissions.CREATE_ARENA_COMMAND)
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
						GenericArguments.onlyOne(GenericArguments.enumValue(Text.of("type"), ArenaType.class)))
				.executor(new CreateArenaCommand(this))
				.build());

		//Create Delete Arena
		this.subcommands.put(Collections.singletonList("deletearena"), CommandSpec.builder()
				.description(Text.of("Delete an arena"))
				.permission(PluginPermissions.DELETE_ARENA_COMMAND)
				.arguments(new ArenaNameArgument(this, Text.of("name")))
				.executor(new DeleteArenaCommand(this))
				.build());

        //List Arenas Command
        this.subcommands.put(Collections.singletonList("list"), CommandSpec.builder()
            .description(Text.of("List all arenas on the server"))
            .permission(PluginPermissions.LIST_COMMAND)
            .executor(new ListCommand(this))
            .build());

        //Create Hill Command
        this.subcommands.put(Collections.singletonList("createhill"), CommandSpec.builder()
            .description(Text.of("Creates a hill in arena"))
            .permission(PluginPermissions.CREATE_HILL_COMMAND)
            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
            .executor(new CreateHillCommand(this))
            .build());

        //Set Lobby Command
        this.subcommands.put(Collections.singletonList("createlobby"), CommandSpec.builder()
            .description(Text.of("Creates arena lobby with selected selection points."))
            .permission(PluginPermissions.CREATE_LOBBY_COMMAND)
            .executor(new CreateLobbyCommand(this))
            .build());

        //Set Lobby Spawn Command
        this.subcommands.put(Collections.singletonList("createlobbyspawn"), CommandSpec.builder()
                .description(Text.of("Creates spawn arena lobby"))
                .permission(PluginPermissions.CREATE_LOBBYSPAWN_COMMAND)
                .executor(new CreateLobbySpawnCommand(this))
                .build());

        //Edit Arena Command
        this.subcommands.put(Collections.singletonList("edit"), CommandSpec.builder()
            .description(Text.of("Turns on/off edit mode for the given arena."))
            .permission(PluginPermissions.EDIT_COMMAND)
            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
            .executor(new EditArenaCommand(this))
            .build());

        //Version Command
        this.subcommands.put(Collections.singletonList("version"), CommandSpec.builder()
                .description(Text.of("View version of the plugin"))
                .permission(PluginPermissions.VERSION_COMMAND)
                .executor(new VersionCommand(this))
                .build());

        //KOTH Commands
        final CommandSpec kothCommand = CommandSpec.builder()
            .description(Text.of("Shows all commands in KOTH plugin"))
            .children(this.subcommands)
            .build();


        this.commandManager.register(this, kothCommand, "koth");
    }

    /**
     * Method used to disable KOTH plugin.
     * Unregisters plugin listeners, commands and clears instance objects objects.
     */
    private void disablePlugin()
    {
        final Set<CommandMapping> commandMappings = this.commandManager.getOwnedBy(this);
        for (final CommandMapping commandMapping : commandMappings)
        {
            this.commandManager.removeMapping(commandMapping);
        }

        this.subcommands.clear();
    }

    public Path getConfigDir()
    {
        return this.configDir;
    }

    public Map<List<String>, CommandCallable> getSubcommands()
    {
        return this.subcommands;
    }
}
