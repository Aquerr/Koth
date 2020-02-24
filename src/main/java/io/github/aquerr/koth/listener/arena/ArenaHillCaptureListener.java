package io.github.aquerr.koth.listener.arena;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.event.ArenaHillCaptureEvent;
import io.github.aquerr.koth.listener.AbstractListener;
import org.spongepowered.api.event.Listener;

public class ArenaHillCaptureListener extends AbstractListener
{
    public ArenaHillCaptureListener(final Koth plugin)
    {
        super(plugin);
    }

    @Listener
    public void onHillCapture(final ArenaHillCaptureEvent event)
    {

    }
}
