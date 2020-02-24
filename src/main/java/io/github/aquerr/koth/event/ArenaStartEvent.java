package io.github.aquerr.koth.event;

import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.event.cause.Cause;

public class ArenaStartEvent extends AbstractArenaEvent
{
    ArenaStartEvent(final Arena arena, final Cause cause)
    {
        super(arena, cause);
    }
}
