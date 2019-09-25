package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.ArenaType;
import io.github.aquerr.koth.entity.SelectionPoints;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

public class CreateArenaCommand extends AbstractCommand
{
    public CreateArenaCommand(final Koth plugin)
    {
        super(plugin);
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {
        final String arenaName = args.requireOne("name");
        final ArenaType type = args.requireOne("type");

        if (!(source instanceof Player))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

        if(super.getPlugin().getArenaManager().getArenas().containsKey(arenaName))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Arena with such name already exists."));

        final Player player = (Player)source;
        if (!super.getPlugin().getPlayerSelectionPoints().containsKey(player.getUniqueId()))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must select two points in the world first before creating an arena!"));

        final World world = player.getWorld();
        final SelectionPoints selectionPoints = super.getPlugin().getPlayerSelectionPoints().get(player.getUniqueId());

        if (selectionPoints.getFirstPoint() == null || selectionPoints.getSecondPoint() == null)
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must select two points in the world first before creating an arena!"));

        final Arena arena = new Arena(arenaName, type, world.getUniqueId(), selectionPoints.getFirstPoint(), selectionPoints.getSecondPoint());
        final boolean didSucceed = super.getPlugin().getArenaManager().addArena(arena);
        if (!didSucceed)
        {
            player.sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Something went wrong while creating the arena..."));
            return CommandResult.success();
        }

        super.getPlugin().getPlayersCreatingArena().put(player.getUniqueId(), arena);
        super.getPlugin().getPlayerSelectionPoints().remove(player.getUniqueId()); //TODO: Shouldn't we remove player from map even if adding of arena fails?
        player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "The arena has been created!"));
        player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "Now it is time to set up hills in the arena. Select two points with your wand and type ", TextColors.GOLD, "/koth createhill", TextColors.WHITE, " to create a hill.\n" +
                "You can create as many hills as you want."));
        return CommandResult.success();
    }
}
