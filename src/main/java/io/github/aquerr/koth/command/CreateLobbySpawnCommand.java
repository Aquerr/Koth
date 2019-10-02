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

public class CreateLobbySpawnCommand extends AbstractCommand
{
	public CreateLobbySpawnCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		if(!(source instanceof Player))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

		final Player player = (Player)source;

		Arena arena = super.getPlugin().getPlayersCreatingArena().get(player.getUniqueId());
		if (arena == null)
			arena = super.getPlugin().getPlayersEditingArena().get(player.getUniqueId());

		if(arena == null)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must be in arena editing mode to do this."));

		if(!arena.getLobby().intersects(player.getLocation().getBlockPosition()))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Spawn must be placed inside the Lobby arena!"));

		arena.getLobby().setSpawnPoint(player.getLocation().getBlockPosition());
		final boolean didSucceed = super.getPlugin().getArenaManager().updateArena(arena);
		if(!didSucceed)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Something went wrong with saving the arena..."));
		player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GOLD, "Spawn Lobby has been added to the arena!"));
		player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "If you are done with the spawn lobby. You can now setup hills on the arena. Select two points with your wand and type ", TextColors.GOLD, "/koth createhill <name>", TextColors.WHITE, " to create a hill.\n" +
				"You can create as many hill as you want."));
		return CommandResult.success();
	}
}
