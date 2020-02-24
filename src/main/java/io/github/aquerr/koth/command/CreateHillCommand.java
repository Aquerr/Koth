package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.Hill;
import io.github.aquerr.koth.entity.SelectionPoints;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

public class CreateHillCommand extends AbstractCommand
{
    public CreateHillCommand(final Koth plugin)
    {
        super(plugin);
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {
        final String name = args.requireOne(Text.of("name"));

        if (!(source instanceof Player))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

        final Player player = (Player)source;
        final World world = player.getWorld();
        Arena arena = super.getPlugin().getPlayersCreatingArena().get(player.getUniqueId());
        if (arena == null)
            arena = super.getPlugin().getPlayersEditingArena().get(player.getUniqueId());

        if(arena == null)
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must be in arena editing mode to do this."));

        if(arena.getHills().stream().anyMatch(x->x.getName().equalsIgnoreCase(name)))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Hill with such name already exists in this arena!"));

        if (!arena.getWorldUniqueId().equals(world.getUniqueId()))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Hills must exists in the same world where arena exists!"));

        if (!super.getPlugin().getPlayerSelectionPoints().containsKey(player.getUniqueId()))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must select two points in the world first before creating an arena."));

        final SelectionPoints selectionPoints = super.getPlugin().getPlayerSelectionPoints().get(player.getUniqueId());
        if(!arena.intersects(world.getUniqueId(), selectionPoints.getFirstPoint()) || !arena.intersects(world.getUniqueId(), selectionPoints.getSecondPoint()))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Hill must be placed inside the editing arena!"));

        for (final Hill hill : arena.getHills())
        {
            if (hill.intersects(selectionPoints.getFirstPoint()) || hill.intersects(selectionPoints.getSecondPoint()))
                throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Hill cannot be placed on top another hill!"));
        }

        final Hill hill = new Hill(name, selectionPoints.getFirstPoint(), selectionPoints.getSecondPoint());
        arena.addHill(hill);
        final boolean didSucceed = super.getPlugin().getArenaManager().updateArena(arena);
        if (!didSucceed)
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Something went wrong with saving the arena..."));
        super.getPlugin().getPlayerSelectionPoints().remove(player.getUniqueId());
        player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GOLD, "Added hill to the arena!"));
        return CommandResult.success();
    }
}
