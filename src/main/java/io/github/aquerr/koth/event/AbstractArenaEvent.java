package io.github.aquerr.koth.event;

import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public abstract class AbstractArenaEvent extends AbstractEvent implements Event, Cancellable
{
    private boolean isCancelled = false;

    private final Arena arena;
    private final Cause cause;

    AbstractArenaEvent(final Arena arena, final Cause cause)
    {
        super();
        this.arena = arena;
        this.cause = cause;
    }

    public Arena getArena()
    {
        return this.arena;
    }

    @Override
    public Cause getCause()
    {
        return this.cause;
    }

    @Override
    public boolean isCancelled()
    {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled)
    {
        this.isCancelled = cancelled;
    }
}
