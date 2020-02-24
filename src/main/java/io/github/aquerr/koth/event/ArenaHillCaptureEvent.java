package io.github.aquerr.koth.event;

import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.ArenaTeam;
import io.github.aquerr.koth.entity.Hill;
import org.spongepowered.api.event.cause.Cause;

public class ArenaHillCaptureEvent extends AbstractArenaEvent
{
    private final Hill hill;
    private final ArenaTeam arenaTeam;

    ArenaHillCaptureEvent(final Arena arena, final Hill hill, final ArenaTeam arenaTeam, final Cause cause)
    {
        super(arena, cause);
        this.hill = hill;
        this.arenaTeam = arenaTeam;
    }

    public Hill getHill()
    {
        return this.hill;
    }

    public ArenaTeam getAssociatedTeam()
    {
        return this.arenaTeam;
    }
}
