//package io.github.aquerr.koth.command.arguments;
//
//import io.github.aquerr.koth.Koth;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class ArenaClassNameArgument extends CommandElement
//{
//	private final Koth plugin;
//
//	public ArenaClassNameArgument(final Koth plugin, final @Nullable Text key)
//	{
//		super(key);
//		this.plugin = plugin;
//	}
//
//	@Nullable
//	@Override
//	protected Object parseValue(final CommandSource source, final CommandArgs args) throws ArgumentParseException
//	{
//		if (args.hasNext())
//		{
//			return args.next();
//		}
//		else
//		{
//			return "";
//		}
//	}
//
//	@Override
//	public List<String> complete(final CommandSource src, final CommandArgs args, final CommandContext context)
//	{
//		final Set<String> arenaClassesNames = this.plugin.getArenaClassManager().getArenaClasses().keySet();
//		final List<String> list = new ArrayList<>(arenaClassesNames);
//		Collections.sort(list);
//
//		if (args.hasNext())
//		{
//			final String charSequence = args.nextIfPresent().orElse("").toLowerCase();
//			return list.stream().filter(x->x.contains(charSequence)).collect(Collectors.toList());
//		}
//
//		return list;
//	}
//}
