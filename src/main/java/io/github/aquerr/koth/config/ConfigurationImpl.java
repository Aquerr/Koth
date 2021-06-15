package io.github.aquerr.koth.config;

import com.google.inject.Singleton;
import io.github.aquerr.koth.Koth;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Singleton
public class ConfigurationImpl implements Configuration
{
	private static final String SETTINGS_FILE_NAME = "settings.conf";

	private final Koth plugin;

	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private CommentedConfigurationNode configNode;

	//Config fields
	private String languageFile;
	private List<String> whiteListedCommandsOnArena;

	public ConfigurationImpl(final Koth plugin, final Path configDir)
	{
		this.plugin = plugin;
		final Optional<Asset> optionalAsset = Sponge.assetManager().asset(this.plugin.getPluginContainer(), SETTINGS_FILE_NAME);
		if(!optionalAsset.isPresent())
			Sponge.server().shutdown();

		final Asset asset = optionalAsset.get();
		try
		{
			asset.copyToDirectory(configDir, false, true);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		this.configLoader = HoconConfigurationLoader.builder().headerMode(HeaderMode.PRESERVE).path(configDir.resolve(SETTINGS_FILE_NAME)).build();
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
		return configNode.node(nodePath).getInt(defaultValue);
	}

	@Override
	public double getDouble(final double defaultValue, final Object... nodePath)
	{
		Object value = configNode.node(nodePath).getDouble(defaultValue);

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
		return configNode.node(nodePath).getFloat(defaultValue);
	}

	@Override
	public boolean getBoolean(final boolean defaultValue, final Object... nodePath)
	{
		return configNode.node(nodePath).getBoolean(defaultValue);
	}

	@Override
	public String getString(final String defaultValue, final Object... nodePath)
	{
		return configNode.node(nodePath).getString(defaultValue);
	}

	@Override
	public List<String> getListOfStrings(final Collection<String> defaultValue, final Object... nodePath)
	{
		try
		{
			return configNode.node(nodePath).getList(TypeToken.get(String.class), new ArrayList<>(defaultValue));
		}
		catch(SerializationException e)
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
