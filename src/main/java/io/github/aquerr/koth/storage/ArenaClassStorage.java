package io.github.aquerr.koth.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.model.ArenaClass;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
        Path storageDirPath = plugin.getConfigDir().resolve("storage");
        this.arenaClassesFile = storageDirPath.resolve("classes.conf");
        if (Files.notExists(storageDirPath))
        {
            try
            {
                Files.createDirectory(storageDirPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

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
        this.configurationLoader = HoconConfigurationLoader.builder().path(this.arenaClassesFile).build();
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

    public boolean addArenaClass(final ArenaClass arenaClass)
    {
        final ConfigurationNode arenaClassNode = this.configNode.node("classes", arenaClass.getName());
        try
        {
            arenaClassNode.node("items").get(new TypeToken<List<ItemStack>>(){}, arenaClass.getItems());
        }
        catch(final SerializationException e)
        {
            e.printStackTrace();
        }
        return saveChanges();
    }

    public boolean updateArenaClass(final ArenaClass arena) throws SerializationException
    {
        return addArenaClass(arena);
    }

    public boolean deleteArenaClass(final String name)
    {
        this.configNode.node("classes").removeChild(name);
        return saveChanges();
    }

    public ArenaClass getArenaClass(final String name) throws SerializationException
    {
        final ConfigurationNode arenaClassNode = this.configNode.node("classes", name);
        final List<ItemStack> items = arenaClassNode.node("items").getList(new TypeToken<ItemStack>(){});
        return new ArenaClass(name, items);
    }

    public List<ArenaClass> getArenaClasses() throws SerializationException
    {
        final ConfigurationNode arenaClassesNode = this.configNode.node("classes");
        final Map<Object, ? extends ConfigurationNode> arenaClassesNodes =  arenaClassesNode.childrenMap();
        final Set<Object> arenaClassesNames = arenaClassesNodes.keySet();
        final List<ArenaClass> arenaClasses = new ArrayList<>();
        for (final Object arenaClassName : arenaClassesNames)
        {
            if(!(arenaClassName instanceof String))
                continue;
            final ArenaClass arena = getArenaClass(String.valueOf(arenaClassName));
            arenaClasses.add(arena);
        }
        return arenaClasses;
    }

    private boolean saveChanges()
    {
        try
        {
            configurationLoader.save(configNode);
            return true;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
