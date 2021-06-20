package io.github.aquerr.koth.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.model.ArenaClass;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.Scalars;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                Files.createDirectories(storageDirPath);
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


        this.configurationLoader = KothConfigurateHelper.loaderForPath(this.arenaClassesFile);
        try
        {
            this.configNode = this.configurationLoader.load(KothConfigurateHelper.getDefaultOptions());
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
            final List<String> itemStacksAsStrings = new LinkedList<>();

            for (final ItemStack itemStack : arenaClass.getItems())
            {
                itemStacksAsStrings.add(DataFormats.HOCON.get().write(itemStack.toContainer()));
            }

            arenaClassNode.node("items").setList(Scalars.STRING.type(), itemStacksAsStrings);
        }
        catch(final IOException e)
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

        final List<String> itemsAsStrings = arenaClassNode.node("items").getList(String.class);
        final List<ItemStack> items = new LinkedList<>();

        for (final String itemAsString : itemsAsStrings)
        {
            try
            {
                DataContainer dataContainer = DataFormats.HOCON.get().read(itemAsString);
                ItemStack itemStack = ItemStack.builder().fromContainer(dataContainer).build();
                items.add(itemStack);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return new ArenaClass(name, items);
    }

    public List<ArenaClass> getArenaClasses() throws SerializationException
    {
        final ConfigurationNode arenaClassesNode = this.configNode.node("classes");
        final Map<Object, ? extends ConfigurationNode> arenaClassesNodes =  arenaClassesNode.childrenMap();
        final Set<Object> arenaClassesNames = arenaClassesNodes.keySet();
        final List<ArenaClass> arenaClasses = new LinkedList<>();
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
