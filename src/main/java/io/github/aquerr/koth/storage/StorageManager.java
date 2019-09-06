package io.github.aquerr.koth.storage;

import com.google.inject.ImplementedBy;
import io.github.aquerr.koth.entity.Arena;

@ImplementedBy(StorageManagerImpl.class)
public interface StorageManager
{
    Arena getArena(final String name);

    boolean addArena(final Arena arena);

    boolean updateArena(final Arena arena);

    boolean deleteArena(final String name);
}
