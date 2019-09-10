package io.github.aquerr.koth.storage.serializer;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import io.github.aquerr.koth.entity.Hill;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HillTypeSerializer implements TypeSerializer<Hill>
{
    @Nullable
    @Override
    public Hill deserialize(@NonNull final TypeToken<?> type, @NonNull final ConfigurationNode value) throws ObjectMappingException
    {
        final Vector3i firstPoint = value.getNode("firstPoint").getValue(TypeToken.of(Vector3i.class));
        final Vector3i secondPoint = value.getNode("secondPoint").getValue(TypeToken.of(Vector3i.class));
        return new Hill(firstPoint, secondPoint);
    }

    @Override
    public void serialize(@NonNull final TypeToken<?> type, @Nullable final Hill obj, @NonNull final ConfigurationNode value) throws ObjectMappingException
    {
        value.getNode("firstPoint").setValue(TypeToken.of(Vector3i.class), obj.getFirstPoint());
        value.getNode("secondPoint").setValue(TypeToken.of(Vector3i.class), obj.getSecondPoint());
    }
}
