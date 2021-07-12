package io.github.aquerr.koth.storage;

import com.google.inject.ImplementedBy;
import io.github.aquerr.koth.model.ArenaClass;

import java.util.List;

@ImplementedBy(StorageManagerImpl.class)
public interface StorageManager
{
//    List<Arena> getArenas();
//    Arena getArena(final String name);
//    boolean addArena(final Arena arena) throws ObjectMappingException;
//    boolean updateArena(final Arena arena);
//    boolean deleteArena(final String name);

    List<ArenaClass> getArenaClasses();
    boolean addOrUpdateArenaClass(final ArenaClass arenaClass);
    boolean deleteArenaClass(final String name);

}
