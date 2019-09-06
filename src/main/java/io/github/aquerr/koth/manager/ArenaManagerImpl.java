package io.github.aquerr.koth.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.storage.StorageManager;
import io.github.aquerr.koth.storage.StorageManagerImpl;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Singleton
public class ArenaManagerImpl implements ArenaManager
{
    private final Map<String, Arena> arenasCache = new HashMap<>(1);

    private final StorageManager storageManager;

    @Inject
    public ArenaManagerImpl(final StorageManager storageManager)
    {
        this.storageManager = storageManager;
    }

    @Override
    public List<Arena> getArenas()
    {
        return new ArrayList<>(this.arenasCache.values());
    }

    @Override
    public boolean addArena(final Arena arena)
    {
        //Add arena to storage by using a separate thread.
        //We do not want to use main thread for storage.
        CompletableFuture.runAsync(() -> {
            final boolean didSucceed = this.storageManager.addArena(arena);
            if (!didSucceed)
                Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Arena named \"" + arena.getName() + "\" could not be saved into the storage..."));
        });

        //Add arena to global arena list (cache)
        final Arena putArena = arenasCache.put(arena.getName(), arena);
        return putArena == null;
    }

    @Override
    public boolean updateArena(final Arena arena)
    {
        //Delete arena from the storage in a separate thread.
        CompletableFuture.runAsync(() -> {
            final boolean didSucceed = this.storageManager.updateArena(arena);
            if (!didSucceed)
                Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Arena named \"" + arena.getName() + "\" could not be updated in the storage..."));
        });

        //TODO: Hmmmm?
        return true;
    }

    @Override
    public boolean deleteArena(final String name)
    {
        //Delete arena from the storage in a separate thread.
        CompletableFuture.runAsync(() -> {
            final boolean didSucceed = this.storageManager.deleteArena(name);
            if (!didSucceed)
                Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Arena named \"" + name + "\" could not be deleted from the storage..."));
        });

        final Arena deletedArena = this.arenasCache.remove(name);
        return deletedArena != null;
    }
}
