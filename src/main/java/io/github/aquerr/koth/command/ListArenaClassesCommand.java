package io.github.aquerr.koth.command;

import io.github.aquerr.koth.manager.ArenaClassManager;
import io.github.aquerr.koth.model.ArenaClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListArenaClassesCommand extends AbstractCommand
{
    private final ArenaClassManager arenaClassManager;

	public ListArenaClassesCommand(final ArenaClassManager arenaClassManager)
	{
	    this.arenaClassManager = arenaClassManager;
	}

	@Override
	public CommandResult execute(final CommandContext context) throws CommandException
	{
		//TODO: List Command should list all arenas on the server.
		final Collection<ArenaClass> arenaClasses = this.arenaClassManager.getArenaClasses().values();
		final List<Component> helpList = new ArrayList<>();

		for (final ArenaClass arenaClass : arenaClasses)
		{
			final TextComponent.Builder arenaClassDescBuilder = Component.text()
					.append(Component.text("Name: ", NamedTextColor.BLUE))
					.append(Component.text(arenaClass.getName(), NamedTextColor.GOLD))
					.append(Component.newline())
					.append(Component.text("Items: ", NamedTextColor.GREEN))
					.append(Component.newline());

			arenaClass.getItems().forEach(x -> arenaClassDescBuilder.append(Component.text(x.type().key(RegistryTypes.ITEM_TYPE).asString()))
					.append(Component.text(" x" + x.quantity()))
					.append(Component.newline()));

			final TextComponent arenaText = Component.empty()
					.append(Component.text("- ", NamedTextColor.BLUE).append(Component.text(arenaClass.getName(), NamedTextColor.GOLD)))
					.hoverEvent(HoverEvent.showText(arenaClassDescBuilder.build()));
			helpList.add(arenaText);
		}

		final PaginationList paginationList = PaginationList.builder()
				.title(Component.text("Classes list", NamedTextColor.GOLD))
				.contents(helpList)
				.padding(Component.text("-", NamedTextColor.DARK_BLUE))
				.linesPerPage(10)
				.build();
		paginationList.sendTo(context.cause().audience());

		return CommandResult.success();
	}
}
