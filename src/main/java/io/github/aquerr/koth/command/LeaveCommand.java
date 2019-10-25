package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class LeaveCommand extends AbstractCommand
{
	public LeaveCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		if(!(source instanceof Player))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

		final Player player = (Player)source;
		boolean isPlayingArena = false;
		Arena arena = null;
		for(final Arena possibleArena : super.getPlugin().getArenaManager().getArenas().values())
		{
			if(possibleArena.getPlayers().contains(player.getUniqueId()))
			{
				isPlayingArena = true;
				arena = possibleArena;
				break;
			}
		}

		if(!isPlayingArena)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You have not joined any arena yet!"));

		final boolean didLeave = arena.leave(player);
		if(didLeave)
			player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "You left the arena!"));
		else
			player.sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Could not join the arena."));

		player.getInventory().clear();
		player.setLocation(player.getWorld().getSpawnLocation());
		final boolean didSuccess = super.getPlugin().getArenaManager().updateArena(arena);
		return CommandResult.success();
	}
}
