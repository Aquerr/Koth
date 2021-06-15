package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.util.SelectionPoints;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.inventory.ItemStack;

public class WandUsageListener extends AbstractListener
{
    public WandUsageListener(final Koth plugin)
    {
        super(plugin);
    }

    @Listener
    public void onRightClick(final InteractBlockEvent.Secondary event, final @Root ServerPlayer player)
    {
        if(event.cause().context().get(EventContextKeys.USED_HAND).get() != HandTypes.MAIN_HAND.get())
            return;

        if(event.block() == BlockSnapshot.NONE.get())
            return;

        if(player.itemInHand(HandTypes.MAIN_HAND) == ItemStack.empty())
            return;

        final ItemStack itemInHand = player.itemInHand(HandTypes.MAIN_HAND);

        String itemName = itemInHand.get(Keys.CUSTOM_NAME)
                .map(PlainTextComponentSerializer.plainText()::serialize)
                .orElse(null);

        if (itemName == null || !itemName.equals("Koth Wand"))
            return;

        SelectionPoints selectionPoints = super.getPlugin().getSelectionManager().getSelectionPointsForPlayer(player)
                .orElse(new SelectionPoints(null, event.block().position()));
        selectionPoints.setSecondPoint(event.block().position());

        super.getPlugin().getSelectionManager().setSelectionPointsForPlayer(player, selectionPoints);
        player.sendMessage(TextComponent.ofChildren(Component.text("Second point", NamedTextColor.GOLD).append(Component.text(" has been selected at ", NamedTextColor.BLUE)).append(Component.text(event.block().position().toString(), NamedTextColor.GOLD))));
        event.setCancelled(true);
    }

    @Listener
    public void onLeftClick(final InteractBlockEvent.Primary.Start event, final @Root ServerPlayer player)
    {
        if(event.cause().context().get(EventContextKeys.USED_HAND).get() != HandTypes.MAIN_HAND.get())
            return;

        if(event.block() == BlockSnapshot.NONE.get())
            return;

        if(player.itemInHand(HandTypes.MAIN_HAND) == ItemStack.empty())
            return;

        final ItemStack itemInHand = player.itemInHand(HandTypes.MAIN_HAND);

        String itemName = itemInHand.get(Keys.CUSTOM_NAME)
                .map(PlainTextComponentSerializer.plainText()::serialize)
                .orElse(null);

        if (itemName == null || !itemName.equals("Koth Wand"))
            return;

        SelectionPoints selectionPoints = super.getPlugin().getSelectionManager().getSelectionPointsForPlayer(player)
                .orElse(new SelectionPoints(event.block().position(), null));
        selectionPoints.setFirstPoint(event.block().position());

        super.getPlugin().getSelectionManager().setSelectionPointsForPlayer(player, selectionPoints);
        player.sendMessage(TextComponent.ofChildren(Component.text("First point", NamedTextColor.GOLD).append(Component.text(" has been selected at ", NamedTextColor.BLUE)).append(Component.text(event.block().position().toString(), NamedTextColor.GOLD))));
        event.setCancelled(true);
    }
}
