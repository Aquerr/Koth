package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.SelectionPoints;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CreateLobbyCommand extends AbstractCommand
{
	public CreateLobbyCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		if(!(source instanceof Player))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

		final Player player = (Player)source;
		if (!super.getPlugin().getPlayerSelectionPoints().containsKey(player.getUniqueId()))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must select two points in the world first before creating an arena!"));

		final SelectionPoints selectionPoints = super.getPlugin().getPlayerSelectionPoints().get(player.getUniqueId());
		if (selectionPoints.getFirstPoint() == null || selectionPoints.getSecondPoint() == null)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must select two points in the world first before creating an arena!"));

		Arena arena = super.getPlugin().getPlayersCreatingArena().get(player.getUniqueId());
		if (arena == null)
			arena = super.getPlugin().getPlayersEditingArena().get(player.getUniqueId());

		if(arena == null)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must be in arena editing mode to do this."));

		if(arena.intersects(selectionPoints.getFirstPoint()) || arena.intersects(selectionPoints.getSecondPoint()))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Lobby must be placed outside the arena!"));

		arena.setLobbyPoints(selectionPoints.getFirstPoint(), selectionPoints.getSecondPoint());
		final boolean didSucceed = super.getPlugin().getArenaManager().updateArena(arena);
		if(!didSucceed)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Something went wrong with saving the arena..."));
		super.getPlugin().getPlayerSelectionPoints().remove(player.getUniqueId());
		player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GOLD, "Lobby has been added to the arena!"));
		return CommandResult.success();
	}
}
