package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.Koth;

public abstract class AbstractListener
{
    private final Koth plugin;

    AbstractListener(final Koth plugin)
    {
        this.plugin = plugin;
    }

    public Koth getPlugin()
    {
        return this.plugin;
    }
}
