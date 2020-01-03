package io.github.aquerr.koth.entity;

import com.flowpowered.math.vector.Vector3i;
import com.google.inject.internal.cglib.core.$ProcessArrayCallback;
import org.spongepowered.api.entity.living.player.Player;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Arena
{
    private static final Random RANDOM = new Random();

    private String name;
    private Vector3i firstPoint;
    private Vector3i secondPoint;
    private UUID worldUUID;
    private ArenaType type;

    private int maxPlayers = 10;
    private final Set<Hill> hills;
    private final Set<ArenaTeam> teams;
    private ArenaStatus status;

    private boolean isRoundBased = false;
    private Duration roundTime = Duration.of(3, ChronoUnit.MINUTES);

    private Lobby lobby;

    public Arena(final String name, final ArenaType type ,final UUID worldUUID, final Vector3i firstPoint, final Vector3i secondPoint)
    {
        this(name, type, worldUUID, firstPoint, secondPoint, new HashSet<>(), new HashSet<>(), new Lobby(Vector3i.ZERO,Vector3i.ZERO,Vector3i.ZERO));
    }

    public Arena(final String name, final ArenaType type, final UUID worldUUID, final Vector3i firstPoint, final Vector3i secondPoint, final Set<Hill> hills, final Set<ArenaTeam> teams, final Lobby lobby)
    {
        this.name = name;
        this.worldUUID = worldUUID;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.hills = hills;
        this.teams = teams;
        this.type = type;
        this.lobby = lobby;
        this.status = ArenaStatus.IDLE;

        if(this.type == ArenaType.TEAMS)
        {
            //Precreate two teams
            final ArenaTeam arenaTeamRed = new ArenaTeam("red");
            final ArenaTeam arenaTeamBlue = new ArenaTeam("blue");
            this.teams.add(arenaTeamBlue);
            this.teams.add(arenaTeamRed);
        }
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

    public void setLobbyPoints(final Vector3i firstPoint, final Vector3i secondPoint)
    {
        this.lobby = new Lobby(firstPoint, secondPoint, Vector3i.ZERO);
    }

    public Lobby getLobby()
    {
        return this.lobby;
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

    public ArenaStatus getStatus()
    {
        return this.status;
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

    public boolean addPlayer(final Player player)
    {
        //Add player to correct team
        if(this.type == ArenaType.FFA)
        {
            final ArenaTeam arenaTeam = new ArenaTeam(player.getName() + "'s team");
            arenaTeam.addPlayer(player);
            return addTeam(arenaTeam);
        }
        else if(this.type == ArenaType.TEAMS)
        {
            final ArenaTeam arenaTeam = getTeamWithLeastPlayers();
            return arenaTeam.addPlayer(player);
        }

        return false;
    }


    public boolean removePlayer(Player player)
    {
        //Add player to correct team
        if(this.type == ArenaType.FFA)
        {
            ArenaTeam arenaTeamToRemove = null;
            for(final ArenaTeam arenaTeam : this.teams)
            {
                if(arenaTeam.getPlayers().contains(player.getUniqueId()))
                    arenaTeamToRemove = arenaTeam;
            }
            return removeTeam(arenaTeamToRemove);
        }
        else if(this.type == ArenaType.TEAMS)
        {
            ArenaTeam arenaTeamToRemove = null;
            for(final ArenaTeam arenaTeam : this.teams)
            {
                if(arenaTeam.getPlayers().contains(player.getUniqueId()))
                    arenaTeamToRemove = arenaTeam;
            }

            return arenaTeamToRemove.removePlayer(player);
            //TODO: Shuffle teams...
        }

        return false;
    }

    public boolean hasTeam(final String name)
    {
        for(final ArenaTeam arenaTeam : this.teams)
        {
            if(arenaTeam.getName().equals(name))
                return true;
        }
        return false;
    }

    private ArenaTeam getTeamWithLeastPlayers()
    {
        ArenaTeam resultArena = null;
        for(final ArenaTeam arenaTeam : this.teams)
        {
            if(resultArena == null)
                resultArena = arenaTeam;
            if(arenaTeam.getPlayers().size() < resultArena.getPlayers().size())
                resultArena = arenaTeam;
        }
        return resultArena;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena)o;
        return maxPlayers == arena.maxPlayers && isRoundBased == arena.isRoundBased && name.equals(arena.name) && firstPoint.equals(arena.firstPoint) && secondPoint.equals(arena.secondPoint) && worldUUID.equals(arena.worldUUID) && type == arena.type && hills.equals(arena.hills) && teams.equals(arena.teams) && status == arena.status && roundTime.equals(arena.roundTime) && lobby.equals(arena.lobby);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, firstPoint, secondPoint, worldUUID, type, maxPlayers, hills, teams, status, isRoundBased, roundTime, lobby);
    }
}
