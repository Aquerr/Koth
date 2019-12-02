package io.github.aquerr.koth.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Singleton;
import io.github.aquerr.koth.Koth;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.loader.HeaderMode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Singleton
public class ConfigurationImpl implements Configuration
{
	private final Koth plugin;
	private final Path configDir;

	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private CommentedConfigurationNode configNode;

	//Config fields
	private String languageFile;
	private List<String> whiteListedCommandsOnArena;

	public ConfigurationImpl(final Koth plugin, final Path configDir)
	{
		this.plugin = plugin;
		this.configDir = configDir;
		final Optional<Asset> optionalAsset = Sponge.getAssetManager().getAsset(this.plugin, "settings.conf");
		if(!optionalAsset.isPresent())
			Sponge.getServer().shutdown();

		final Asset asset = optionalAsset.get();
		final Path settingsPath = configDir.resolve("settings.conf");
		try
		{
			asset.copyToFile(settingsPath, false, true);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		this.configLoader = HoconConfigurationLoader.builder().setHeaderMode(HeaderMode.PRESERVE).setPath(settingsPath).build();
		reload();
		save();

		//TODO: Load config fields...
		this.whiteListedCommandsOnArena = getListOfStrings(Collections.emptyList(), "whitelisted-commands");
		this.languageFile = getString("english.lang", "language-file");
	}

	@Override
	public void reload()
	{
		try
		{
			this.configNode = this.configLoader.load();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void save()
	{
		try
		{
			this.configLoader.save(this.configNode);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public int getInt(final int defaultValue, final Object... nodePath)
	{
		return configNode.getNode(nodePath).getInt(defaultValue);
	}

	@Override
	public double getDouble(final double defaultValue, final Object... nodePath)
	{
		Object value = configNode.getNode(nodePath).getValue(defaultValue);

		if (value instanceof Integer)
		{
			int number = (Integer) value;
			return number;
		}
		else if(value instanceof Double)
		{
			return (double) value;
		}
		else return 0;
	}

	@Override
	public float getFloat(final float defaultValue, final Object... nodePath)
	{
		return configNode.getNode(nodePath).getFloat(defaultValue);
	}

	@Override
	public boolean getBoolean(final boolean defaultValue, final Object... nodePath)
	{
		return configNode.getNode(nodePath).getBoolean(defaultValue);
	}

	@Override
	public String getString(final String defaultValue, final Object... nodePath)
	{
		return configNode.getNode(nodePath).getString(defaultValue);
	}

	@Override
	public List<String> getListOfStrings(final Collection<String> defaultValue, final Object... nodePath)
	{
		try
		{
			return configNode.getNode(nodePath).getList(TypeToken.of(String.class), new ArrayList<>(defaultValue));
		}
		catch(ObjectMappingException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public String getLanguageFileName()
	{
		return this.languageFile;
	}

	@Override
	public List<String> getWhiteListedCommandsOnArena()
	{
		return this.whiteListedCommandsOnArena;
	}
}
