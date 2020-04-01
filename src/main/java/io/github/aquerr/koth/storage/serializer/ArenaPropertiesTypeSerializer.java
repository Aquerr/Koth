package io.github.aquerr.koth.storage.serializer;

import com.google.common.reflect.TypeToken;
import io.github.aquerr.koth.entity.ArenaProperties;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public class ArenaPropertiesTypeSerializer implements TypeSerializer<ArenaProperties>
{
    @Nullable
    @Override
    public ArenaProperties deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException
    {
        final ArenaProperties arenaProperties = new ArenaProperties();

        Map<Object, ? extends ConfigurationNode> map = value.getChildrenMap();
        for (Map.Entry<Object, ? extends ConfigurationNode> mapEntry : map.entrySet())
        {
            arenaProperties.put((ArenaProperties.PropertyKey)mapEntry.getKey(), mapEntry.getValue().getValue());
        }
        return arenaProperties;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable ArenaProperties obj, @NonNull ConfigurationNode value) throws ObjectMappingException
    {
        if (obj == null)
            return;

        for (Map.Entry<ArenaProperties.PropertyKey<?>, Object> property : obj.getProperties().entrySet())
        {
            value.getNode(property.getKey().toString()).setValue(property.getValue());
        }
    }
}
