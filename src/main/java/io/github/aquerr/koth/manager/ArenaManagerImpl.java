package io.github.aquerr.koth.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.storage.StorageManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
    public Map<String, Arena> getArenas()
    {
        return this.arenasCache;
    }

    @Override
    public Optional<Arena> getArena(final String name)
    {
        final Arena arena = this.arenasCache.get(name);
        if (arena == null)
            return Optional.empty();
        return Optional.of(arena);
    }

    @Override
    public boolean addArena(final Arena arena)
    {
        //Add arena to storage by using a separate thread.
        //We do not want to use main thread for storage.
        CompletableFuture.runAsync(() -> {
            boolean didSucceed = false;
            try
            {
                didSucceed = this.storageManager.addArena(arena);
            }
            catch (ObjectMappingException e)
            {
                e.printStackTrace();
            }
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
        //TODO: Should we preform additional update operations here or will commands classes do everything on their own?

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

    @Override
    public boolean reloadCache()
    {
        try
        {
            final List<Arena> arenas = this.storageManager.getArenas();
            for (final Arena arena : arenas)
            {
                this.arenasCache.put(arena.getName(), arena);
            }
            return true;
        }
        catch (final  Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Arena> getArenaForUser(final User player)
    {
        Arena arena = null;
        for(final Arena possibleArena : getArenas().values())
        {
            if(possibleArena.getPlayers().contains(player.getUniqueId()))
            {
                arena = possibleArena;
                break;
            }
        }

        if(arena != null)
            return Optional.of(arena);
        else return Optional.empty();
    }
}
