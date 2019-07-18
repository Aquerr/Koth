package io.github.aquerr.koth;

import com.google.inject.Inject;
import io.github.aquerr.koth.command.KothCommand;
import io.github.aquerr.koth.command.WandCommand;
import io.github.aquerr.koth.listeners.WandUsageListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.source.ConsoleSource;
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

@Plugin(id = PluginInfo.ID)
public class Koth {

    private final Map<List<String>, CommandCallable> subcommands = new HashMap<>();
    private final CommandManager commandManager;
    private final EventManager eventManager;
    private final Path configDir;

    @Inject
    private Koth(final CommandManager commandManager, final EventManager eventManager, @ConfigDir(sharedRoot = false) Path configDir)
    {
        this.commandManager = commandManager;
        this.eventManager = eventManager;
        this.configDir = configDir;
    }

    @Listener
    public void onInitialization(final GameInitializationEvent event)
    {
        Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.YELLOW, "Loading Koth listeners and commands..."));

        //Register commands and listeners
        registerListeners();
        registerCommands();

        //If something went wrong, disable plugin.
    }

    private void registerListeners()
    {
        this.eventManager.registerListeners(this, new WandUsageListener());
    }

    private void registerCommands()
    {
        //Wand Command
        this.subcommands.put(Collections.singletonList("wand"), CommandSpec.builder()
                .description(Text.of("Gives player a KOTH builder wand."))
                .permission(PluginPermissions.WAND_COMMAND)
                .executor(new WandCommand(this))
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

    }
}
