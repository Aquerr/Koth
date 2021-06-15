package io.github.aquerr.koth.command;

import io.github.aquerr.koth.PluginInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public abstract class AbstractCommand implements CommandExecutor
{
    @Override
    public abstract CommandResult execute(CommandContext context) throws CommandException;

    protected ServerPlayer requireServerPlayer(CommandContext context) throws CommandException
    {
        return context.cause().first(ServerPlayer.class)
                .orElseThrow(() -> new CommandException(PluginInfo.PLUGIN_ERROR.append(Component.text("Only in-game player can use this command!", NamedTextColor.RED))));
    }
}