package io.github.aquerr.koth.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ArenaProperties
{
    private Map<PropertyKey<?>, Object> properties = new HashMap<>();

    public <T> void put(final PropertyKey<T> propertyKey, final T value)
    {
        this.properties.put(propertyKey, value);
    }

    public <T> T get(final PropertyKey<T> key)
    {
        return (T)this.properties.get(key);
    }

    public Map<PropertyKey<?>, Object> getProperties()
    {
        return this.properties;
    }

    public static ArenaProperties getDefaultProperties()
    {
        final ArenaProperties arenaProperties = new ArenaProperties();
        arenaProperties.put(PropertyKey.SHUFFLE, false);
        arenaProperties.put(PropertyKey.DROP_ITEMS_AFTER_DEATH, false);
        arenaProperties.put(PropertyKey.ROUND_TIME, 300);
        arenaProperties.put(PropertyKey.ROUND_BASED, true);
        return arenaProperties;
    }

    public static class PropertyKey<T>
    {
        public static final PropertyKey<Boolean> SHUFFLE = new PropertyKey<>();
        public static final PropertyKey<Boolean> DROP_ITEMS_AFTER_DEATH = new PropertyKey<>();
        public static final PropertyKey<Integer> ROUND_TIME = new PropertyKey<>();
        public static final PropertyKey<Boolean> ROUND_BASED = new PropertyKey<>();

        @Override
        public String toString()
        {
            final Class clazz = PropertyKey.class;
            final Field[] fields = clazz.getFields();
            String fieldName = "";
            for (final Field field : fields)
            {
                try
                {
                    final Object fieldObject = field.get(null);
                    if (fieldObject.equals(this))
                    {
                        fieldName = field.getName();
                        break;
                    }
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
            return fieldName;
        }
    }
}
