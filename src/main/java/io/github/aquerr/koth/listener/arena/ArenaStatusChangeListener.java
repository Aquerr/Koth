package io.github.aquerr.koth.listener.arena;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.event.ArenaStatusChangeEvent;
import io.github.aquerr.koth.listener.AbstractListener;
import org.spongepowered.api.event.Listener;

public class ArenaStatusChangeListener extends AbstractListener
{
    public ArenaStatusChangeListener(final Koth plugin)
    {
        super(plugin);
    }

    @Listener
    public void onArenaStatusChangeListener(final ArenaStatusChangeEvent event)
    {
        event.getArena().onArenaStatusChangeEvent(event);
    }
}
