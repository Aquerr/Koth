package io.github.aquerr.koth.command.arguments;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArenaNameArgument extends CommandElement
{
	private final Koth plugin;

	public ArenaNameArgument(final Koth plugin, final @Nullable Text key)
	{
		super(key);
		this.plugin = plugin;
	}

	@Nullable
	@Override
	protected Object parseValue(final CommandSource source, final CommandArgs args) throws ArgumentParseException
	{
		if (args.hasNext())
		{
			return args.next();
		}
		else
		{
			return "";
		}
	}

	@Override
	public List<String> complete(final CommandSource src, final CommandArgs args, final CommandContext context)
	{
		final Set<String> arenasNames = this.plugin.getArenaManager().getArenas().keySet();
		final List<String> list = new ArrayList<>(arenasNames);
		Collections.sort(list);

		if (args.hasNext())
		{
			final String charSequence = args.nextIfPresent().orElse("").toLowerCase();
			return list.stream().filter(x->x.contains(charSequence)).collect(Collectors.toList());
		}

		return list;
	}
}
