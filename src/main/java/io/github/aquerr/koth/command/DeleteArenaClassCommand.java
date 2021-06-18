package io.github.aquerr.koth.command;

import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.manager.ArenaClassManager;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class DeleteArenaClassCommand extends AbstractCommand
{
    private final ArenaClassManager arenaClassManager;

    public DeleteArenaClassCommand(ArenaClassManager arenaClassManager)
    {
        this.arenaClassManager = arenaClassManager;
    }

    @Override
    public CommandResult execute(CommandContext context) throws CommandException
    {
        final String className = context.requireOne(Parameter.string().key("name").build());
        ServerPlayer serverPlayer = requireServerPlayer(context);

        final boolean didSucceed = this.arenaClassManager.deleteArenaClass(className);
        if(!didSucceed)
            throw new CommandException(PluginInfo.PLUGIN_ERROR.append(Component.text("Could not delete the class...")));
        serverPlayer.sendMessage(PluginInfo.PLUGIN_PREFIX.append(Component.text("Successfully deleted the class!")));
        return CommandResult.success();
    }
}
