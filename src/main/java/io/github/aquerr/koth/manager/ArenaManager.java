package io.github.aquerr.koth.manager;

import io.github.aquerr.koth.entity.Arena;

import java.util.List;

public interface ArenaManager
{
    List<Arena> getArenas();

    boolean addArena(Arena arena);

    boolean updateArena(Arena arena);

    boolean deleteArena(String name);
}
