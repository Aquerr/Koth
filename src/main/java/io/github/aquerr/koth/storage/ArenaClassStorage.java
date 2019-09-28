package io.github.aquerr.koth.storage;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.entity.ArenaClass;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.item.inventory.ItemStack;

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

    public boolean addArenaClass(final ArenaClass arenaClass)
    {
        final ConfigurationNode arenaClassNode = this.configNode.getNode("classes", arenaClass.getName());
        try
        {
            arenaClassNode.getNode("items").setValue(new TypeToken<List<ItemStack>>(){}, arenaClass.getItems());
        }
        catch(final ObjectMappingException e)
        {
            e.printStackTrace();
        }
        return saveChanges();
    }

    public boolean updateArenaClass(final ArenaClass arena) throws ObjectMappingException
    {
        return addArenaClass(arena);
    }

    public boolean deleteArenaClass(final String name)
    {
        this.configNode.getNode("classes").removeChild(name);
        return saveChanges();
    }

    public ArenaClass getArenaClass(final String name) throws ObjectMappingException
    {
        final ConfigurationNode arenaClassNode = this.configNode.getNode("classes", name);
        final List<ItemStack> items = arenaClassNode.getNode("items").getList(new TypeToken<ItemStack>(){});
        return new ArenaClass(name, items);
    }

    public List<ArenaClass> getArenaClasses() throws ObjectMappingException
    {
        final ConfigurationNode arenaClassesNode = this.configNode.getNode("classes");
        final Map<Object, ? extends ConfigurationNode> arenaClassesNodes =  arenaClassesNode.getChildrenMap();
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
