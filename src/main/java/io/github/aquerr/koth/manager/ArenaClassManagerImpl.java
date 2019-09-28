package io.github.aquerr.koth.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.ArenaClass;
import io.github.aquerr.koth.storage.StorageManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Singleton
public class ArenaClassManagerImpl implements ArenaClassManager
{
	private final Map<String, ArenaClass> arenasCache = new HashMap<>(1);

	private final StorageManager storageManager;

	@Inject
	public ArenaClassManagerImpl(final StorageManager storageManager)
	{
		this.storageManager = storageManager;
	}

	@Override
	public Map<String, ArenaClass> getArenaClasses()
	{
		return this.arenasCache;
	}

	@Override
	public Optional<ArenaClass> getArenaClass(final String name)
	{
		final ArenaClass arena = this.arenasCache.get(name);
		if (arena == null)
			return Optional.empty();
		return Optional.of(arena);
	}

	@Override
	public boolean addArenaClass(final ArenaClass arenaClass)
	{
		//Add arena to storage by using a separate thread.
		//We do not want to use main thread for storage.
		CompletableFuture.runAsync(() -> {
			boolean didSucceed = false;
			try
			{
				didSucceed = this.storageManager.addArenaClass(arenaClass);
			}
			catch (ObjectMappingException e)
			{
				e.printStackTrace();
			}
			if (!didSucceed)
				Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Class named \"" + arenaClass.getName() + "\" could not be saved into the storage..."));
		});

		//Add arena to global arena list (cache)
		final ArenaClass putArena = arenasCache.put(arenaClass.getName(), arenaClass);
		return putArena == null;
	}

	@Override
	public boolean updateArenaClass(final ArenaClass arenaClass)
	{
		//TODO: Should we preform additional update operations here or will commands classes do everything on their own?

		//Delete arena from the storage in a separate thread.
		CompletableFuture.runAsync(() -> {
			final boolean didSucceed = this.storageManager.updateArenaClass(arenaClass);
			if (!didSucceed)
				Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Class named \"" + arenaClass.getName() + "\" could not be updated in the storage..."));
		});

		//TODO: Hmmmm?
		return true;
	}

	@Override
	public boolean deleteArenaClass(final String name)
	{
		//Delete arena from the storage in a separate thread.
		CompletableFuture.runAsync(() -> {
			final boolean didSucceed = this.storageManager.deleteArena(name);
			if (!didSucceed)
				Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Class named \"" + name + "\" could not be deleted from the storage..."));
		});

		final ArenaClass deletedArena = this.arenasCache.remove(name);
		return deletedArena != null;
	}

	@Override
	public boolean reloadCache()
	{
		try
		{
			final List<ArenaClass> arenas = this.storageManager.getArenaClasses();
			for (final ArenaClass arena : arenas)
			{
				this.arenasCache.put(arena.getName(), arena);
			}
			return true;
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
