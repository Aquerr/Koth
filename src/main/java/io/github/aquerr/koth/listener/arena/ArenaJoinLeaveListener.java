package io.github.aquerr.koth.listener.arena;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.event.ArenaJoinEvent;
import io.github.aquerr.koth.event.ArenaLeaveEvent;
import io.github.aquerr.koth.listener.AbstractListener;
import org.spongepowered.api.event.Listener;

public class ArenaJoinLeaveListener extends AbstractListener
{
    public ArenaJoinLeaveListener(final Koth plugin)
    {
        super(plugin);
    }

    @Listener
    public void onArenaJoin(final ArenaJoinEvent event)
    {
        event.getArena().onArenaJoinEvent(event);
    }

    @Listener
    public void onArenaLeave(final ArenaLeaveEvent event)
    {
        event.getArena().onArenaLeaveEvent(event);
    }
}
