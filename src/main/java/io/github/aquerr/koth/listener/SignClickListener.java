package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.block.entity.BlockEntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;

public class SignClickListener extends AbstractListener
{
	public SignClickListener(final Koth plugin)
	{
		super(plugin);
	}

	@Listener
	public void onSignClick(final InteractBlockEvent.Secondary event, final @Root Player player)
	{
		final BlockSnapshot targetBlock = event.block();
		final Optional<ServerLocation> optionalLocation = targetBlock.location();
		if(!optionalLocation.isPresent())
			return;

		final ServerLocation location = optionalLocation.get();
		final Optional<? extends BlockEntity> optionalTileEntity = location.blockEntity();

		if(!optionalTileEntity.isPresent())
			return;

		final BlockEntity tileEntity = optionalTileEntity.get();

		if(tileEntity.type() != BlockEntityTypes.SIGN)
			return;

		//Check if player is playing arena
//		final Optional<Arena> arena = super.getPlugin().getArenaManager().getArenaForUser(player);
//		if(!arena.isPresent())
//			return;
//
//		//Read second line from sign and check if it corresponds to an arena class
//		final Sign sign = (Sign)tileEntity;
//		final SignData signData = sign.getSignData();
//		final Text text = signData.lines().get(1);
//
//		if(text.isEmpty())
//			return;
//
//		final String possibleArenaClassName = text.toPlain();
//		if(!possibleArenaClassName.startsWith("[") || !possibleArenaClassName.endsWith("]"))
//			return;
//
//		final String arenaClassName = possibleArenaClassName.replace("[", "").replace("]", "");
//
//		final Optional<ArenaClass> optionalArenaClass = super.getPlugin().getArenaClassManager().getArenaClass(arenaClassName);
//		if(!optionalArenaClass.isPresent())
//		{
//			player.sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Could not find the arena class named ", TextColors.GOLD, arenaClassName, TextColors.RED, "!"));
//			return;
//		}
//
//		final ArenaClass arenaClass = optionalArenaClass.get();
//
//		player.getInventory().clear();
//		for(final ItemStack itemStack : arenaClass.getItems())
//		{
//			player.getInventory().offer(ItemStack.builder().from(itemStack).build());
//		}
//
//		player.sendMessage(Text.of("You chose class: " + arenaClassName));
	}
}
