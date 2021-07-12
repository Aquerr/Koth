package io.github.aquerr.koth.storage.hocon.handler;

import io.github.aquerr.koth.model.ArenaClass;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArenaClassConfigurationFileHandler implements ConfigurationFileHandler<ArenaClass>
{
    private static final String ITEMS_NODE_KEY = "items";

    private final String arenaClassName;
    private final ConfigurationLoader<?> configurationLoader;
    private ConfigurationNode root;

    public ArenaClassConfigurationFileHandler(final String arenaClassName, final ConfigurationLoader<?> configurationLoader, final ConfigurationNode root)
    {
        this.arenaClassName = arenaClassName;
        this.configurationLoader = configurationLoader;
        this.root = root;
    }

    @Override
    public void save(ArenaClass arenaClass)
    {
        try
        {
            setItems(arenaClass.getItems());
            this.configurationLoader.save(this.root);
        }
        catch (ConfigurateException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public ArenaClass get()
    {
        reload();
        final List<ItemStack> itemStacks = getItems();
        return new ArenaClass(arenaClassName, itemStacks);
    }

    @Override
    public void reload()
    {
        try
        {
            this.root = this.configurationLoader.load();
        }
        catch (ConfigurateException e)
        {
            e.printStackTrace();
        }
    }

    private void setItems(final List<ItemStack> items)
    {
        try
        {
            final List<String> stringItems = new ArrayList<>(items.size());
            for (final ItemStack itemStack : items)
            {
                final DataContainer dataContainer = itemStack.toContainer();
                final String stringItem = DataFormats.HOCON.get().write(dataContainer);
                stringItems.add(stringItem);
            }
            this.root.node(ITEMS_NODE_KEY).setList(String.class, stringItems);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private List<ItemStack> getItems()
    {
        try
        {
            final List<ItemStack> itemStacks = new LinkedList<>();
            final List<String> stringItems = this.root.node(ITEMS_NODE_KEY).getList(String.class, Collections.emptyList());
            for (final String stringItem : stringItems)
            {
                final DataContainer dataContainer = DataFormats.HOCON.get().read(stringItem);
                final ItemStack itemStack = ItemStack.builder().fromContainer(dataContainer).build();
                itemStacks.add(itemStack);
            }
            return itemStacks;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }
}
