package io.github.aquerr.koth.event;

import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

public class ArenaLeaveEvent extends AbstractArenaEvent
{
    private final Player player;

    public ArenaLeaveEvent(final Player player, final Arena arena, final Cause cause)
    {
        super(arena, cause);
        this.player = player;
    }

    public Player getPlayer()
    {
        return this.player;
    }
}