package io.github.aquerr.koth.model;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;

/**
 * A class representing a class that can be chosen by a player. E.g. Warrior or Archer
 */
public class ArenaClass
{
    private final String name;
    private final List<ItemStack> items;

    public ArenaClass(final String name, final List<ItemStack> items)
    {
        this.name = name;
        this.items = items;
    }

    public String getName()
    {
        return this.name;
    }

    public List<ItemStack> getItems()
    {
        return this.items;
    }
}
