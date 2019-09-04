package io.github.aquerr.koth.entity;

import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaTeam
{
    private String name;
    private List<UUID> players = new ArrayList<>();

    public ArenaTeam(final String name)
    {
        this.name = name;
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
}
