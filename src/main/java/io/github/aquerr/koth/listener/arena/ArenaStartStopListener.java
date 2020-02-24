package io.github.aquerr.koth.listener.arena;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.event.ArenaStartEvent;
import io.github.aquerr.koth.event.ArenaStopEvent;
import io.github.aquerr.koth.listener.AbstractListener;
import org.spongepowered.api.event.Listener;

public class ArenaStartStopListener extends AbstractListener
{
    public ArenaStartStopListener(final Koth plugin)
    {
        super(plugin);
    }

    @Listener
    public void onArenaStart(final ArenaStartEvent event)
    {
        event.getArena().onArenaStartEvent(event);
    }

    @Listener
    public void onArenaStop(final ArenaStopEvent event)
    {
        event.getArena().onArenaStopEvent(event);
    }
}
