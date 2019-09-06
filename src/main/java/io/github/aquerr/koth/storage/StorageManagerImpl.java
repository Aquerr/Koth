package io.github.aquerr.koth.storage;

import com.google.inject.Singleton;
import io.github.aquerr.koth.entity.Arena;

@Singleton
public class StorageManagerImpl implements StorageManager
{
    public Arena getArena(final String arenaName)
    {
        return null;
    }

    public boolean addArena(final Arena arena)
    {
        return false;
    }

    public boolean updateArena(final Arena arena)
    {
        return false;
    }

    public boolean deleteArena(final String name)
    {
        return false;
    }
}
