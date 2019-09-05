package io.github.aquerr.koth.entity;

import org.spongepowered.api.item.inventory.Inventory;

/**
 * A class representing a class that can be chosen by a player. E.g. Warrior or Archer
 */
public class ArenaClass
{
    private final String name;
    private final Inventory inventory;
//    private final int cost;

    public ArenaClass(final String name, final Inventory inventory)
    {
        this.name = name;
        this.inventory = inventory;
    }

    public String getName()
    {
        return this.name;
    }

    public Inventory getInventory()
    {
        return this.inventory;
    }

    //Maybe we should have fields for helment, chestplate, leggins and boots respectively?
}
