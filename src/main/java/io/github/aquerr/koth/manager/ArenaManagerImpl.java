package io.github.aquerr.koth.manager;

import com.google.inject.Singleton;
import io.github.aquerr.koth.entity.Arena;

import java.util.List;

@Singleton
public class ArenaManagerImpl implements ArenaManager
{
    @Override
    public List<Arena> getArenas()
    {
        return null;
    }

    @Override
    public boolean addArena(final Arena arena)
    {
        return false;
    }

    @Override
    public boolean updateArena(final Arena arena)
    {
        return false;
    }

    @Override
    public boolean deleteArena(final String name)
    {
        return false;
    }
}
