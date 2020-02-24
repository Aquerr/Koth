package io.github.aquerr.koth.event;

import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.event.cause.Cause;

public class ArenaStopEvent extends AbstractArenaEvent
{
    ArenaStopEvent(final Arena arena, final Cause cause)
    {
        super(arena, cause);
    }
}
