//package io.github.aquerr.koth.storage.hocon.serializer;
//
//import com.google.common.reflect.TypeToken;
//import io.github.aquerr.koth.entity.ArenaTeam;
//import org.checkerframework.checker.nullness.qual.NonNull;
//import org.checkerframework.checker.nullness.qual.Nullable;
//import org.spongepowered.configurate.serialize.TypeSerializer;
//
//import java.util.ArrayList;
//
//public class ArenaTeamTypeSerializer implements TypeSerializer<ArenaTeam>
//{
//    @Nullable
//    @Override
//    public ArenaTeam deserialize(@NonNull final TypeToken<?> type, @NonNull final ConfigurationNode value) throws ObjectMappingException
//    {
//        final String name = value.getNode("name").getString();
////        final List<UUID> players = value.getList(TypeToken.of(UUID.class));
//        final Vector3i spawnPoint = value.getNode("spawnPoint").getValue(TypeToken.of(Vector3i.class));
//        return new ArenaTeam(name, new ArrayList<>(), spawnPoint);
//    }
//
//    @Override
//    public void serialize(@NonNull final TypeToken<?> type, @Nullable final ArenaTeam obj, @NonNull final ConfigurationNode value) throws ObjectMappingException
//    {
//        value.getNode("name").setValue(obj.getName());
////        value.getNode("players").setValue(obj.getPlayers());
//        value.getNode("spawnPoint").setValue(TypeToken.of(Vector3i.class), obj.getSpawnPoint());
//    }
//}
