package io.github.aquerr.koth.manager;

import io.github.aquerr.koth.entity.ArenaClass;

import java.util.Map;
import java.util.Optional;

public interface ArenaClassManager
{

	Map<String, ArenaClass> gerArenaClasses();

	Optional<ArenaClass> getArenaClass(final String name);

	boolean addArena(final ArenaClass arenaClass);

	boolean updateArena(final ArenaClass arenaClass);

	boolean deleteArena(final String name);

	boolean reloadCache();
}
