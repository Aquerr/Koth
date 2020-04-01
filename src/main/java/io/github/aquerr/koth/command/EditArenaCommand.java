package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class EditArenaCommand extends AbstractCommand
{
	public EditArenaCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		final String name = args.requireOne(Text.of("name"));

		if(!(source instanceof Player))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

		final Player player = (Player)source;
		if(super.getPlugin().getPlayersEditingArena().containsKey(player.getUniqueId()) || super.getPlugin().getPlayersCreatingArena().containsKey(player.getUniqueId()))
		{
			super.getPlugin().getPlayersEditingArena().remove(player.getUniqueId());
			super.getPlugin().getPlayersCreatingArena().remove(player.getUniqueId());
			player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "Turned off edit mode."));
			return CommandResult.success();
		}

		final Optional<Arena> arena = super.getPlugin().getArenaManager().getArena(name);
		if(!arena.isPresent())
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Arena with the given name does not exists!"));

		super.getPlugin().getPlayersEditingArena().put(player.getUniqueId(), arena.get());
		player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "You are now editing arena " + name));
		return CommandResult.success();
	}
}
