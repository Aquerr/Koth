package io.github.aquerr.koth.event;

import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.ArenaStatus;
import org.spongepowered.api.event.cause.Cause;

public class ArenaStatusChangeEvent extends AbstractArenaEvent
{
    private final ArenaStatus arenaStatus;

    ArenaStatusChangeEvent(Arena arena, final ArenaStatus arenaStatus, Cause cause)
    {
        super(arena, cause);
        this.arenaStatus = arenaStatus;
    }

    public ArenaStatus getNewArenaStatus()
    {
        return this.arenaStatus;
    }
}
