package io.github.aquerr.koth.entity;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ArenaTeam
{
    private String name;
    private List<UUID> players;
    private Vector3i spawnPoint;

    public ArenaTeam(final String name)
    {
        this(name, new ArrayList<>(), Vector3i.ZERO);
    }

    public ArenaTeam(final String name, final List<UUID> players, final Vector3i spawnPoint)
    {
        this.name = name;
        this.players = players;
        this.spawnPoint = spawnPoint;
    }

    public String getName()
    {
        return this.name;
    }

    boolean addPlayer(final Player player)
    {
        return this.players.add(player.getUniqueId());
    }

    boolean removePlayer(final Player player)
    {
        return this.players.remove(player.getUniqueId());
    }

    public List<UUID> getPlayers()
    {
        return this.players;
    }

    public void setSpawnPoint(final Vector3i spawnPoint)
    {
        this.spawnPoint = spawnPoint;
    }

    public Vector3i getSpawnPoint()
    {
        return this.spawnPoint;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArenaTeam arenaTeam = (ArenaTeam)o;
        return name.equals(arenaTeam.name) && players.equals(arenaTeam.players) && spawnPoint.equals(arenaTeam.spawnPoint);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, players, spawnPoint);
    }

    @Override
    public String toString()
    {
        return "ArenaTeam{" + "name='" + name + '\'' + ", players=" + players + ", spawnPoint=" + spawnPoint + '}';
    }
}
