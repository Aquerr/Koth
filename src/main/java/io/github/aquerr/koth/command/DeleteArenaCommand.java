package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DeleteArenaCommand extends AbstractCommand
{
	public DeleteArenaCommand(final Koth plugin)
	{
		super(plugin);
	}


	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		final String arenaName = args.requireOne("name");
		final boolean didSucceed = super.getPlugin().getArenaManager().deleteArena(arenaName);
		if (!didSucceed)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Could not delete arena: " + arenaName));
		source.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN,"Successfully deleted arena: " + arenaName));
		return CommandResult.success();
	}

}
