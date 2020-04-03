package io.github.aquerr.koth.storage.serializer;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import io.github.aquerr.koth.entity.ArenaTeam;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaTeamTypeSerializer implements TypeSerializer<ArenaTeam>
{
    @Nullable
    @Override
    public ArenaTeam deserialize(@NonNull final TypeToken<?> type, @NonNull final ConfigurationNode value) throws ObjectMappingException
    {
        final String name = value.getNode("name").getString();
//        final List<UUID> players = value.getList(TypeToken.of(UUID.class));
        final Vector3i spawnPoint = value.getNode("spawnPoint").getValue(TypeToken.of(Vector3i.class));
        return new ArenaTeam(name, new ArrayList<>(), spawnPoint);
    }

    @Override
    public void serialize(@NonNull final TypeToken<?> type, @Nullable final ArenaTeam obj, @NonNull final ConfigurationNode value) throws ObjectMappingException
    {
        value.getNode("name").setValue(obj.getName());
//        value.getNode("players").setValue(obj.getPlayers());
        value.getNode("spawnPoint").setValue(TypeToken.of(Vector3i.class), obj.getSpawnPoint());
    }
}
