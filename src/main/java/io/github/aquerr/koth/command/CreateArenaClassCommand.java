package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.ArenaClass;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateArenaClassCommand extends AbstractCommand
{
	public CreateArenaClassCommand(final Koth plugin)
	{
		super(plugin);
	}

	@Override
	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
	{
		final String className = args.requireOne(Text.of("name"));

		if(!(source instanceof Player))
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

		final Player player = (Player)source;
		final Inventory inventory = player.getInventory();

		List<ItemStack> items = new ArrayList<>();

		for(Inventory value : inventory)
		{
			final Optional<ItemStack> itemStack = value.peek();
			if(itemStack.isPresent())
				items.add(itemStack.get());
		}

		final boolean didSucceed = super.getPlugin().getArenaClassManager().addArenaClass(new ArenaClass(className, items));
		if(!didSucceed)
			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Something went wrong while creating the arena class..."));
		source.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "Successfully created the arena class!"));
		return CommandResult.success();
	}
}
