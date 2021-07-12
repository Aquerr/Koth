package io.github.aquerr.koth.storage.hocon;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.model.ArenaClass;
import io.github.aquerr.koth.storage.ArenaClassStorage;
import io.github.aquerr.koth.storage.KothConfigurateHelper;
import io.github.aquerr.koth.storage.hocon.handler.ArenaClassConfigurationFileHandler;
import io.github.aquerr.koth.storage.hocon.handler.ConfigurationFileHandler;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class HoconArenaClassStorage implements ArenaClassStorage
{
    private static final String FILE_EXTENSION = ".conf";

    private final Path classesDirPath;

    // ArenaClass name -> HoconConfigurationLoader
    private final Map<String, ConfigurationFileHandler<ArenaClass>> loaders;

    @Inject
    public HoconArenaClassStorage(final @ConfigDir(sharedRoot = false) Path configDir)
    {
        this.classesDirPath = configDir.resolve("storage").resolve("classes");
        this.loaders = new HashMap<>();
        createDirectoryStructure();
        loadExistingArenaClassFiles();
    }

    @Override
    public boolean saveOrUpdate(final ArenaClass arenaClass)
    {
        ConfigurationFileHandler<ArenaClass> fileHandler = this.loaders.get(arenaClass.getName().toLowerCase());

        try
        {
            // Create file and loader if not exist
            if (fileHandler == null)
            {
                final Path arenaClassFilePath = this.classesDirPath.resolve(arenaClass.getName().toLowerCase() + FILE_EXTENSION);
                Files.createFile(arenaClassFilePath);
                final HoconConfigurationLoader hoconConfigurationLoader = KothConfigurateHelper.loaderForPath(arenaClassFilePath);
                final ConfigurationNode root = hoconConfigurationLoader.load();
                fileHandler = new ArenaClassConfigurationFileHandler(arenaClass.getName().toLowerCase(), hoconConfigurationLoader, root);
                this.loaders.put(arenaClass.getName().toLowerCase(), fileHandler);
            }

            // Perform save/update
            fileHandler.save(arenaClass);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String arenaClassName)
    {
        try
        {
            Files.deleteIfExists(this.classesDirPath.resolve(arenaClassName.toLowerCase() + FILE_EXTENSION));
            this.loaders.remove(arenaClassName.toLowerCase());
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    public ArenaClass getArenaClass(final String name)
    {
        final ConfigurationFileHandler<ArenaClass> fileHandler = this.loaders.get(name.toLowerCase());
        if (fileHandler == null)
            return null;
        return fileHandler.get();
    }

    public List<ArenaClass> getArenaClasses()
    {
        final List<ArenaClass> arenaClasses = new ArrayList<>(this.loaders.size());
        for (final Map.Entry<String, ConfigurationFileHandler<ArenaClass>> fileHandlerEntry : this.loaders.entrySet())
        {
            final ArenaClass arenaClass = getArenaClass(fileHandlerEntry.getKey());
            arenaClasses.add(arenaClass);
        }
        return arenaClasses;
    }

    private void loadExistingArenaClassFiles()
    {
        try(final Stream<Path> filesPaths = Files.list(this.classesDirPath))
        {
            filesPaths.forEach(this::loadArenaClassFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadArenaClassFile(final Path filePath)
    {
        try
        {
            final HoconConfigurationLoader hoconConfigurationLoader = KothConfigurateHelper.loaderForPath(filePath);
            final ConfigurationNode configNode = hoconConfigurationLoader.load();
            final String arenaClassName = filePath.getFileName().toString().substring(0, filePath.getFileName().toString().indexOf("."));
            this.loaders.put(arenaClassName.toLowerCase(), new ArenaClassConfigurationFileHandler(arenaClassName, hoconConfigurationLoader, configNode));
        }
        catch (ConfigurateException e)
        {
            e.printStackTrace();
        }
    }

    private void createDirectoryStructure()
    {
        if(Files.notExists(this.classesDirPath))
        {
            try
            {
                Files.createDirectories(this.classesDirPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
