package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.ArenaClass;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListArenaClassesCommand extends AbstractCommand
{
	public ListArenaClassesCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		//TODO: List Command should list all arenas on the server.
		final Collection<ArenaClass> arenaClasses = super.getPlugin().getArenaClassManager().getArenaClasses().values();
		final List<Text> helpList = new ArrayList<>();

		for (final ArenaClass arenaClass : arenaClasses)
		{
			final Text.Builder arenaClassDescBuilder = Text.builder();

			arenaClassDescBuilder.append(Text.of(TextColors.BLUE, "Name: ", TextColors.GOLD, arenaClass.getName() + "\n"));
			arenaClassDescBuilder.append(Text.of(TextColors.GREEN, "Items: " + "\n"));
			arenaClass.getItems().forEach(x -> arenaClassDescBuilder.append(Text.of(TextColors.YELLOW, x.getType().getTranslation().get(), TextColors.RESET, " x" + x.getQuantity() + "\n")));

			final Text arenaText = Text.builder()
					.append(Text.of(TextColors.BLUE, "- ", TextColors.GOLD, arenaClass.getName()))
					.onHover(TextActions.showText(arenaClassDescBuilder.build()))
					.build();
			helpList.add(arenaText);
		}

		final PaginationList paginationList = PaginationList.builder().title(Text.of(TextColors.GOLD, "Classes list")).contents(helpList).padding(Text.of(TextColors.DARK_BLUE, "-")).linesPerPage(10).build();
		paginationList.sendTo(source);

		return CommandResult.success();
	}
}
