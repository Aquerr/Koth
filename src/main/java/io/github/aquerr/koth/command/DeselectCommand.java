package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DeselectCommand extends AbstractCommand
{
    public DeselectCommand(final Koth plugin)
    {
        super(plugin);
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {
        if (!(source instanceof Player))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game player can use this command!"));
        super.getPlugin().getPlayerSelectionPoints().remove(((Player) source).getUniqueId());
        source.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "Selection points have been cleared!"));
        return CommandResult.success();
    }
}
