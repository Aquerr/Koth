//package io.github.aquerr.koth.storage.serializer;
//
//import io.leangen.geantyref.TypeToken;
//import org.checkerframework.checker.nullness.qual.NonNull;
//import org.checkerframework.checker.nullness.qual.Nullable;
//import org.spongepowered.configurate.ConfigurationNode;
//import org.spongepowered.configurate.serialize.SerializationException;
//import org.spongepowered.configurate.serialize.TypeSerializer;
//import org.spongepowered.math.vector.Vector3i;
//
//import java.lang.reflect.Type;
//
//public class HillTypeSerializer implements TypeSerializer<Hill>
//{
//    @Nullable
//    @Override
//    public Hill deserialize(@NonNull final TypeToken<?> type, @NonNull final ConfigurationNode value) throws SerializationException
//    {
//        final String name = value.node("name").getString();
//        final Vector3i firstPoint = value.node("firstPoint").get(TypeToken.get(Vector3i.class));
//        final Vector3i secondPoint = value.node("secondPoint").get(TypeToken.get(Vector3i.class));
//        return new Hill(name, firstPoint, secondPoint);
//    }
//
//    @Override
//    public void serialize(@NonNull final TypeToken<?> type, @Nullable final Hill obj, @NonNull final ConfigurationNode value) throws SerializationException
//    {
//        value.getNode("name").setValue(obj.getName());
//        value.getNode("firstPoint").setValue(TypeToken.of(Vector3i.class), obj.getFirstPoint());
//        value.getNode("secondPoint").setValue(TypeToken.of(Vector3i.class), obj.getSecondPoint());
//    }
//
//    @Override
//    public Hill deserialize(Type type, ConfigurationNode node) throws SerializationException
//    {
//        return null;
//    }
//
//    @Override
//    public void serialize(Type type, @Nullable Hill obj, ConfigurationNode node) throws SerializationException
//    {
//
//    }
//}
