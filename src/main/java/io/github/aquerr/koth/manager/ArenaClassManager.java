package io.github.aquerr.koth.manager;

import com.google.inject.ImplementedBy;
import io.github.aquerr.koth.entity.ArenaClass;

import java.util.Map;
import java.util.Optional;

@ImplementedBy(ArenaClassManagerImpl.class)
public interface ArenaClassManager
{

	Map<String, ArenaClass> gerArenaClasses();

	Optional<ArenaClass> getArenaClass(final String name);

	boolean addArenaClass(final ArenaClass arenaClass);

	boolean updateArenaClass(final ArenaClass arenaClass);

	boolean deleteArenaClass(final String name);

	boolean reloadCache();
}
