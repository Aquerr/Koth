package io.github.aquerr.koth.command;

import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.manager.SelectionManager;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class DeselectCommand extends AbstractCommand
{
    private final SelectionManager selectionManager;

    public DeselectCommand(final SelectionManager selectionManager)
    {
        this.selectionManager = selectionManager;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException
    {
        ServerPlayer serverPlayer = requireServerPlayer(context);
        this.selectionManager.removeSelectionPointsForPlayer(serverPlayer);
        context.sendMessage(Identity.nil(), PluginInfo.PLUGIN_PREFIX.append(Component.text("Selection points have been cleared!", NamedTextColor.GREEN)));
        return CommandResult.success();
    }
}
