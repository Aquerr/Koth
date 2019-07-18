package io.github.aquerr.koth.listeners;

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;

public class WandUsageListener {

    @Listener
    public void onRightClick(final InteractBlockEvent.Secondary event, final @Root Player player)
    {
        if (event.getHandType() == HandTypes.MAIN_HAND)
            return;

        player.sendMessage(Text.of("Right Click!"));
    }

    @Listener
    public void onLeftClick(final InteractBlockEvent.Primary event, final @Root Player player)
    {
        if (event.getHandType() == HandTypes.OFF_HAND)
            return;

        player.sendMessage(Text.of("Left Click!"));
    }
}
