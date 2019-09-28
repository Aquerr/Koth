package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DeleteArenaClassCommand extends AbstractCommand
{
	public DeleteArenaClassCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		final String arenaClassName = args.requireOne(Text.of("name"));

		final boolean didSucceed = super.getPlugin().getArenaClassManager().deleteArenaClass(arenaClassName);
		if(!didSucceed)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Could not delete the class..."));
		source.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN,"Successfully deleted the class!"));

		return CommandResult.success();
	}
}
