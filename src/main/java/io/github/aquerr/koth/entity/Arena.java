package io.github.aquerr.koth.entity;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.entity.living.player.Player;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Arena
{
    private String name;
    private Vector3i firstPoint;
    private Vector3i secondPoint;
    private UUID worldUUID;
    private ArenaType type;

    private int maxPlayers = 10;
//    private final Set<UUID> players;
    private final Set<Hill> hills;
    private final Set<ArenaTeam> teams;

    private boolean isRoundBased = false;
    private Duration roundTime = Duration.of(3, ChronoUnit.MINUTES);

    public Arena(final String name, final ArenaType type ,final UUID worldUUID, final Vector3i firstPoint, final Vector3i secondPoint)
    {
        this(name, type, worldUUID, firstPoint, secondPoint, new HashSet<>(), new HashSet<>());
    }

    public Arena(final String name, final ArenaType type, final UUID worldUUID, final Vector3i firstPoint, final Vector3i secondPoint, final Set<Hill> hills, final Set<ArenaTeam> teams)
    {
        this.name = name;
        this.worldUUID = worldUUID;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.hills = hills;
        this.teams = teams;
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public Vector3i getFirstPoint()
    {
        return this.firstPoint;
    }

    public Vector3i getSecondPoint()
    {
        return this.secondPoint;
    }

    public UUID getWorldUniqueId()
    {
        return this.worldUUID;
    }

    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public Set<Hill> getHills()
    {
        return this.hills;
    }

    public Set<ArenaTeam> getTeams()
    {
        return this.teams;
    }

    public ArenaType getType()
    {
        return this.type;
    }

    public Set<UUID> getPlayers()
    {
        return this.teams.stream().map(ArenaTeam::getPlayers).collect(HashSet::new, AbstractCollection::addAll, AbstractCollection::addAll);
    }

    public boolean isRoundBased()
    {
        return this.isRoundBased;
    }

    public void setRoundBased(final boolean isRoundBased)
    {
        this.isRoundBased = isRoundBased;
    }

    public Duration getRoundTime()
    {
        return this.roundTime;
    }

//    public boolean addPlayer(final Player player)
//    {
//        return this.players.add(player.getUniqueId());
//    }

//    public boolean removePlayer(final Player player)
//    {
//        return this.players.remove(player.getUniqueId());
//    }

    public boolean addHill(final Hill hill)
    {
        return this.hills.add(hill);
    }

    public boolean removeHill(final Hill hill)
    {
        return this.hills.remove(hill);
    }

    public boolean addTeam(final ArenaTeam team)
    {
        return this.teams.add(team);
    }

    public boolean removeTeam(final ArenaTeam team)
    {
        return this.teams.remove(team);
    }

    public boolean intersects(final Vector3i position)
    {
        boolean intersectX = false;
        boolean intersectY = false;
        boolean intersectZ = false;

        //Check X
        if (this.firstPoint.getX() <= this.secondPoint.getX() && (position.getX() <= this.secondPoint.getX() && position.getX() >= this.firstPoint.getX()))
        {
            intersectX = true;
        }
        else if (this.firstPoint.getX() >= this.secondPoint.getX() && (position.getX() <= this.firstPoint.getX() && position.getX() >= this.secondPoint.getX()))
        {
            intersectX = true;
        }

        //Check Y
        if (this.firstPoint.getY() < this.secondPoint.getY() && (position.getY() <= this.secondPoint.getY() && position.getY() >= this.firstPoint.getY()))
        {
            intersectY = true;
        }
        else if (this.firstPoint.getY() >= this.secondPoint.getY() && (position.getY() <= this.firstPoint.getY() && position.getY() >= this.secondPoint.getY()))
        {
            intersectY = true;
        }

        //Check Z
        if (this.firstPoint.getZ() <= this.secondPoint.getZ() && (position.getZ() <= this.secondPoint.getZ() && position.getZ() >= this.firstPoint.getZ()))
        {
            intersectZ = true;
        }
        else if (this.firstPoint.getZ() >= this.secondPoint.getZ() && (position.getZ() <= this.firstPoint.getZ() && position.getZ() >= this.secondPoint.getZ()))
        {
            intersectZ = true;
        }

        return intersectX && intersectY && intersectZ;
    }

    public void startGame()
    {
        //TODO: Start game on arena...

        //Cleanup points

        //Spawn players in proper lobbies

        //Start lobby timer

        //End lobby timer

        //Start game and start game timer

        //End game if timer is done or if points reached theirs limit.
    }
}
