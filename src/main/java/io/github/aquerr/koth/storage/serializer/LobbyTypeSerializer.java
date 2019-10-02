package io.github.aquerr.koth.storage.serializer;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import io.github.aquerr.koth.entity.Lobby;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LobbyTypeSerializer implements TypeSerializer<Lobby>
{

	@Nullable
	@Override
	public Lobby deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException
	{
		final Vector3i firstPoint = value.getNode("firstPoint").getValue(TypeToken.of(Vector3i.class));
		final Vector3i secondPoint = value.getNode("secondPoint").getValue(TypeToken.of(Vector3i.class));
		final Vector3i spawnPoint = value.getNode("spawnPoint").getValue(TypeToken.of(Vector3i.class));
		return new Lobby(firstPoint, secondPoint, spawnPoint);
	}

	@Override
	public void serialize(@NonNull TypeToken<?> type, @Nullable Lobby obj, @NonNull ConfigurationNode value) throws ObjectMappingException
	{
		value.getNode("firstPoint").setValue(TypeToken.of(Vector3i.class), obj.getFirstPoint());
		value.getNode("secondPoint").setValue(TypeToken.of(Vector3i.class), obj.getSecondPoint());
		value.getNode("spawnPoint").setValue(TypeToken.of(Vector3i.class), obj.getSpawnPoint());
	}
}
