//package io.github.aquerr.koth.command;
//
//import io.github.aquerr.koth.Koth;
//import io.github.aquerr.koth.PluginInfo;
//import io.github.aquerr.koth.entity.ArenaClass;
//import org.spongepowered.api.command.CommandException;
//import org.spongepowered.api.command.CommandResult;
//import org.spongepowered.api.command.CommandSource;
//import org.spongepowered.api.command.args.CommandContext;
//import org.spongepowered.api.entity.living.player.Player;
//import org.spongepowered.api.item.inventory.Inventory;
//import org.spongepowered.api.item.inventory.ItemStack;
//import org.spongepowered.api.item.inventory.Slot;
//import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
//import org.spongepowered.api.item.inventory.entity.PlayerInventory;
//import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
//import org.spongepowered.api.item.inventory.type.GridInventory;
//import org.spongepowered.api.text.Text;
//import org.spongepowered.api.text.format.TextColors;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Optional;
//
//public class CreateArenaClassCommand extends AbstractCommand
//{
//	public CreateArenaClassCommand(final Koth plugin)
//	{
//		super(plugin);
//	}
//
//	@Override
//	public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
//	{
//		final String className = args.requireOne(Text.of("name"));
//
//		if(!(source instanceof Player))
//			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));
//
//		final Player player = (Player)source;
//		final List<ItemStack> items = getItemsFromPlayer(player);
//
//		final boolean didSucceed = super.getPlugin().getArenaClassManager().addArenaClass(new ArenaClass(className, items));
//		if(!didSucceed)
//			throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Something went wrong while creating the arena class..."));
//		source.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "Successfully created the arena class!"));
//		return CommandResult.success();
//	}
//
//	private List<ItemStack> getItemsFromPlayer(final Player player)
//	{
//		final List<ItemStack> items = new ArrayList<>();
//		final MainPlayerInventory inventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));
//
////		final Iterator<Slot> slots = (Iterator<Slot>) inventory.slots();
////		while(slots.hasNext())
////		{
////			final Slot slot = slots.next();
////			final Optional<ItemStack> itemStack = slot.poll();
////			if(itemStack.isPresent())
////			{
////				items.add(ItemStack.builder().from(itemStack.get()).build());
////			}
////		}
//
//		for(final Inventory slotInventory : inventory.getGrid().slots())
//		{
//			if(!(slotInventory instanceof Slot))
//				continue;
//
//			final Slot slot = (Slot)slotInventory;
//			final Optional<ItemStack> itemStack = slot.peek();
//			if(itemStack.isPresent())
//			{
//				items.add(ItemStack.builder().fromContainer(itemStack.get().toContainer()).build());
//			}
//		}
//
//		for(final Inventory slotInventory : inventory.getHotbar())
//		{
//			if(!(slotInventory instanceof Slot))
//				continue;
//
//			final Slot slot = (Slot)slotInventory;
//			final Optional<ItemStack> itemStack = slot.peek();
//			if(itemStack.isPresent())
//			{
//				items.add(ItemStack.builder().fromContainer(itemStack.get().toContainer()).build());
//			}
//		}
//		return items;
//	}
//}
