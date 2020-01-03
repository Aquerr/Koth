package io.github.aquerr.koth.entity;

import java.util.HashMap;
import java.util.Map;

public class ArenaProperties
{
    private Map<PropertyKey, Object> properties = new HashMap<>();

    public void put(final PropertyKey propertyKey, final Object value)
    {
        this.properties.put(propertyKey, value);
    }

    public Object get(final PropertyKey key)
    {
        return this.properties.get(key);
    }

    public Map<PropertyKey, Object> getProperties()
    {
        return this.properties;
    }

    public static ArenaProperties getDefaultProperties()
    {
        final ArenaProperties arenaProperties = new ArenaProperties();
        arenaProperties.put(PropertyKey.SHUFFLE, false);
        arenaProperties.put(PropertyKey.DROP_ITEMS_AFTER_DEATH, false);
        return arenaProperties;
    }

    public enum PropertyKey
    {
        SHUFFLE,
        DROP_ITEMS_AFTER_DEATH,
        ROUND_BASED,
        ROUND_TIME;
    }
}
