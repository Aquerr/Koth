package io.github.aquerr.koth.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.model.ArenaClass;
import io.github.aquerr.koth.storage.StorageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.configurate.serialize.SerializationException;

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
			catch (SerializationException e)
			{
				e.printStackTrace();
			}
			if (!didSucceed)
				Sponge.server().sendMessage(TextComponent.ofChildren(PluginInfo.PLUGIN_ERROR.append(Component.text("Class named \"" + arenaClass.getName() + "\" could not be saved into the storage...", NamedTextColor.RED))));
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
				Sponge.server().sendMessage(TextComponent.ofChildren(PluginInfo.PLUGIN_ERROR.append(Component.text("Class named \"" + arenaClass.getName() + "\" could not be updated in the storage...", NamedTextColor.RED))));
		});

		//TODO: Hmmmm?
		return true;
	}

	@Override
	public boolean deleteArenaClass(final String name)
	{
		//Delete arena from the storage in a separate thread.
		CompletableFuture.runAsync(() -> {
			final boolean didSucceed = this.storageManager.deleteArenaClass(name);
			if (!didSucceed)
				Sponge.server().sendMessage(TextComponent.ofChildren(PluginInfo.PLUGIN_ERROR.append(Component.text("Class named \"" + name + "\" could not be deleted from the storage...", NamedTextColor.RED))));
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
