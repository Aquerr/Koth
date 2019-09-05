package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;

public class WandUsageListener extends AbstractListener
{
    public WandUsageListener(final Koth plugin)
    {
        super(plugin);
    }

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
