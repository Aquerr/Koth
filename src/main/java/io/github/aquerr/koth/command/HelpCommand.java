package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.*;

public class HelpCommand extends AbstractCommand
{
    private final Koth koth;

    public HelpCommand(final Koth plugin)
    {
        this.koth = plugin;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException
    {
        final Optional<Integer> helpPage = context.one(Parameter.integerNumber().key("page").optional().build());
        int pageNumber = helpPage.orElse(1);
        final Map<List<String>, Command.Parameterized> subcommands = this.koth.getSubcommands();
        final List<Component> helpList = new ArrayList<>();

        for (final List<String> aliases: subcommands.keySet())
        {
            Command.Parameterized command = subcommands.get(aliases);

            if(context.cause().audience() instanceof Player && !command.canExecute(context.cause()))
                continue;

            final TextComponent textComponent = Component.empty()
//            textComponent.append(Component.text("/koth " + String.join(", ", aliases) + " " + ((TextComponent)command.usage(context.cause())).content(), NamedTextColor.GOLD));
            .append(Component.text("/koth " + String.join(", ", aliases) + " " + command.parameters(), NamedTextColor.GOLD))
            .append(Component.text(" - " + PlainTextComponentSerializer.plainText().serialize(command.shortDescription(context.cause()).get()), NamedTextColor.WHITE));
            //            textBuilder.append(Text.of(TextColors.WHITE, " - " + commandCallable.getShortDescription(source).get().toPlain()  + "\n"));
//            textBuilder.append(Text.of(TextColors.GRAY, "Usage: " + "/koth " + String.join("", aliases) + " " + commandCallable.getUsage(source).toPlain()));
            helpList.add(textComponent);
        }

        //TODO: Sort commands alphabetically
//        helpList.sort(Text::compareTo);
        helpList.sort((o1, o2) ->
        {
            if (o1 instanceof TextComponent && o2 instanceof TextComponent)
            {
                return PlainTextComponentSerializer.plainText().serialize(o1).compareTo(PlainTextComponentSerializer.plainText().serialize(o2));
            }
            return 0;
        });

        final PaginationList paginationList = PaginationList.builder()
                .linesPerPage(14)
                .padding(Component.text("-", NamedTextColor.BLUE))
                .title(Component.text("Commands List", NamedTextColor.GOLD))
                .contents(helpList)
                .build();
        paginationList.sendTo(context.cause().audience(), pageNumber);

        return CommandResult.success();
    }
}
