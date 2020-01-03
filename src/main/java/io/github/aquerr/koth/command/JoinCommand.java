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

public class JoinCommand extends AbstractCommand
{
	public JoinCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		final String arenaName = args.requireOne(Text.of("name"));

		if(!(source instanceof Player))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

		final Player player = (Player)source;
		if(super.getPlugin().getArenaManager().getArenaForUser(player).isPresent())
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You are already playing on one arena! You must leave it first!"));

		final Optional<Arena> optionalArena = super.getPlugin().getArenaManager().getArena(arenaName);
		if(!optionalArena.isPresent())
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Arena with such name does not exist!"));

		if(player.getInventory().totalItems() > 0)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must empty your inventory before joining the arena!"));

		final Arena arena = optionalArena.get();
		final boolean didJoin = arena.addPlayer(player);
		if(didJoin)
			player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "You joined arena " + arenaName));
		else
			player.sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Could not join the arena."));

		//Teleport player to arena lobby
		player.setLocation(arena.getLobby().getSpawnPoint().toDouble(), arena.getWorldUniqueId());

		final boolean didSuccess = super.getPlugin().getArenaManager().updateArena(arena);
		return CommandResult.success();
	}
}
