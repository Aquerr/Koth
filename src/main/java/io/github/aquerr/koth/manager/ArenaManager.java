package io.github.aquerr.koth.manager;

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;
import io.github.aquerr.koth.entity.Arena;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ImplementedBy(ArenaManagerImpl.class)
public interface ArenaManager
{
    /**
     * Gets all arenas from cache.
     * @return A list of arenas.
     */
    Map<String, Arena> getArenas();

    /**
     * Gets arena for the given name
     * @param name the name of the arena
     * @return arena or Optional.empty if arena does not exist
     */
    Optional<Arena> getArena(String name);

    /**
     * Adds arena to the storage and loads it to the cache.
     * @param arena the arena that should be added.
     * @return <tt>true</tt> if operation succeed or <tt>false</tt> if not
     */
    boolean addArena(Arena arena);

    /**
     * Updates arena in the storage.
     * @param arena the arena that should be updated in the storage.
     * @return <tt>true</tt> if operation succeed or <tt>false</tt> if not
     */
    boolean updateArena(Arena arena);

    /**
     * Deletes arena from the storage and unloads it from the cache.
     * @param name the name of the arena that should be deleted.
     * @return <tt>true</tt> if operation succeed of <tt>false</tt> if not
     */
    boolean deleteArena(String name);
}
