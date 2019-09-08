package io.github.aquerr.koth.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.Koth;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class ArenaClassStorage
{
    private final Koth plugin;
    private final Path arenaClassesFile;
    private final ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private ConfigurationNode configNode;

    @Inject
    public ArenaClassStorage(final Koth plugin)
    {
        this.plugin = plugin;
        this.arenaClassesFile = plugin.getConfigDir().resolve("storage").resolve("classes.conf");
        if(Files.notExists(this.arenaClassesFile))
        {
            try
            {
                Files.createFile(arenaClassesFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        this.configurationLoader = HoconConfigurationLoader.builder().setPath(this.arenaClassesFile).build();
        try
        {
            this.configNode = this.configurationLoader.load();
            this.configurationLoader.save(this.configNode);
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }
}
