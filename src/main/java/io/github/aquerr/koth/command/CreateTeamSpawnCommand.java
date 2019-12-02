package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.ArenaTeam;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CreateTeamSpawnCommand extends AbstractCommand
{
	public CreateTeamSpawnCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		final ArenaTeam team = args.requireOne(Text.of("team"));

		if(!(source instanceof Player))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

		final Player player = (Player)source;

		Arena arena = super.getPlugin().getPlayersCreatingArena().get(player.getUniqueId());
		if (arena == null)
			arena = super.getPlugin().getPlayersEditingArena().get(player.getUniqueId());

		if(arena == null)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must be in arena editing mode to do this."));

		if(!arena.intersects(player.getLocation().getBlockPosition()))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Spawn must be placed inside the editing arena!"));

		team.setSpawnPoint(player.getLocation().getBlockPosition());
		final boolean didSucceed = super.getPlugin().getArenaManager().updateArena(arena);
		if(!didSucceed)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Something went wrong with saving the arena..."));
		player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GOLD, "Spawn Team has been added to the arena!"));
		return CommandResult.success();
	}
}
