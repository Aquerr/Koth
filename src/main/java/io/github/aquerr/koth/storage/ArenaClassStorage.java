package io.github.aquerr.koth.storage;

import com.google.inject.ImplementedBy;
import io.github.aquerr.koth.model.ArenaClass;
import io.github.aquerr.koth.storage.hocon.HoconArenaClassStorage;

import java.util.List;

@ImplementedBy(HoconArenaClassStorage.class)
public interface ArenaClassStorage
{
    boolean saveOrUpdate(ArenaClass arenaClass);

    boolean delete(String arenaClassName);

    ArenaClass getArenaClass(String arenaClassName);

    List<ArenaClass> getArenaClasses();
}
