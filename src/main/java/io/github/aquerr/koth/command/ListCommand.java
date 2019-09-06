package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListCommand extends AbstractCommand
{
    public ListCommand(final Koth plugin)
    {
        super(plugin);
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {
        //TODO: List Command should list all arenas on the server.
        final Collection<Arena> arenas = super.getPlugin().getArenaManager().getArenas().values();
        final List<Text> helpList = new ArrayList<>();

        for (final Arena arena : arenas)
        {
            //TODO: Color arena properties and their values.
            final Text arenaDescription = Text.of("Name: " + arena.getName() + "\n"
                    + "World: " + Sponge.getServer().getWorld(arena.getWorldUniqueId()).get().getName() + "\n"
                    + "First Corner: " + arena.getFirstPoint() + "\n"
                    + "Second Corner: " + arena.getSecondPoint() + "\n"
                    + "Max players: " + arena.getMaxPlayers() + "\n"
                    + "Hills: " + arena.getHills().size() + "\n"
                    + "Teams: " + arena.getTeams().size() + "\n"
                    + "Round time: " + arena.getRoundTime().getSeconds() + "\n");
            final Text arenaText = Text.builder()
                    .append(Text.of(TextColors.BLUE, "- " + arena.getPlayers().size() + "/" + arena.getMaxPlayers()))
                    .append(Text.of(TextColors.GOLD, " " + arena.getName()))
                    .onHover(TextActions.showText(arenaDescription))
                    .build();
            helpList.add(arenaText);
        }

        final PaginationList paginationList = PaginationList.builder().title(Text.of(TextColors.GOLD, "Arenas list")).contents(helpList).padding(Text.of(TextColors.DARK_BLUE, "-")).linesPerPage(10).build();
        paginationList.sendTo(source);

        return CommandResult.success();
    }
}
