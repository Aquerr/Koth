package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.Root;

import java.util.Optional;

public class CommandUsageListener extends AbstractListener
{
	public CommandUsageListener(final Koth plugin)
	{
		super(plugin);
	}

	@Listener(order = Order.FIRST, beforeModifications = true)
	public void onCommandUsage(final SendCommandEvent event, final @Root User player)
	{
		final Optional<Arena> optionalArena = super.getPlugin().getArenaManager().getArenaForUser(player);
		if(!optionalArena.isPresent())
			return;

		String usedCommand = event.getCommand();

		//Allow using all koth commands by default.
		if(usedCommand.startsWith("koth"))
			return;

		for(final String whiteListedCommand : super.getPlugin().getConfiguration().getWhiteListedCommandsOnArena())
		{
			if(whiteListedCommand.equals("*"))
				return;

			if (usedCommand.charAt(0) == '/')
			{
				usedCommand = usedCommand.substring(1);
			}

			usedCommand = usedCommand.toLowerCase();

			if(usedCommand.equals(whiteListedCommand) || usedCommand.startsWith(whiteListedCommand))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
}
