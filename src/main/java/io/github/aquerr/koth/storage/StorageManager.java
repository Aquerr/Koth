package io.github.aquerr.koth.storage;

import com.google.inject.ImplementedBy;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.ArenaClass;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

@ImplementedBy(StorageManagerImpl.class)
public interface StorageManager
{
    List<Arena> getArenas();
    Arena getArena(final String name);
    boolean addArena(final Arena arena) throws ObjectMappingException;
    boolean updateArena(final Arena arena);
    boolean deleteArena(final String name);

    boolean addArenaClass(final ArenaClass arenaClass) throws ObjectMappingException;
    boolean updateArenaClass(final ArenaClass arenaClass);
    boolean deleteArenaClass(final String name);
}
