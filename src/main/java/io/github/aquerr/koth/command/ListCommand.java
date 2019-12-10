package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginPermissions;
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
            final Text.Builder arenaDescBuilder = Text.builder();

            arenaDescBuilder.append(Text.of(TextColors.BLUE, "Name: ", TextColors.GOLD, arena.getName() + "\n"));
            arenaDescBuilder.append(Text.of(TextColors.BLUE, "World: ", TextColors.GOLD, Sponge.getServer().getWorld(arena.getWorldUniqueId()).get().getName() + "\n"));
            if(source.hasPermission(PluginPermissions.WAND_COMMAND))
            {
                arenaDescBuilder.append(Text.of(TextColors.BLUE, "First Corner: ", TextColors.GOLD, arena.getFirstPoint() + "\n"));
                arenaDescBuilder.append(Text.of(TextColors.BLUE, "Second Corner: ", TextColors.GOLD, arena.getSecondPoint() + "\n"));
                arenaDescBuilder.append(Text.of(TextColors.BLUE, "Lobby first corner: ", TextColors.GOLD, arena.getLobby().getFirstPoint() + "\n"));
                arenaDescBuilder.append(Text.of(TextColors.BLUE, "Lobby second corner: ", TextColors.GOLD, arena.getLobby().getSecondPoint() + "\n"));
            }
            arenaDescBuilder.append(Text.of(TextColors.BLUE, "Max players: ", TextColors.GOLD, arena.getMaxPlayers() + "\n"));
            arenaDescBuilder.append(Text.of(TextColors.BLUE, "Hills: ", TextColors.GOLD, arena.getHills().size() + "\n"));
            arenaDescBuilder.append(Text.of(TextColors.BLUE, "Teams: ", TextColors.GOLD, arena.getTeams().size() + "\n"));
            arenaDescBuilder.append(Text.of(TextColors.BLUE, "Round time: ", TextColors.GOLD, arena.getRoundTime().getSeconds() + "\n"));
            arenaDescBuilder.append(Text.of(TextColors.GREEN, "Click to join!"));

            final Text arenaText = Text.builder()
                    .append(Text.of(TextColors.BLUE, "- " + arena.getPlayers().size() + "/" + arena.getMaxPlayers()))
                    .append(Text.of(TextColors.GOLD, " " + arena.getName()))
                    .onHover(TextActions.showText(arenaDescBuilder.build()))
                    .onClick(TextActions.runCommand("/koth join " + arena.getName()))
                    .build();
            helpList.add(arenaText);
        }

        final PaginationList paginationList = PaginationList.builder().title(Text.of(TextColors.GOLD, "Arenas list")).contents(helpList).padding(Text.of(TextColors.DARK_BLUE, "-")).linesPerPage(10).build();
        paginationList.sendTo(source);

        return CommandResult.success();
    }
}
