package io.github.aquerr.koth.entity;

import com.flowpowered.math.vector.Vector3i;
import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.event.*;
import io.github.aquerr.koth.scheduling.KothScheduler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Arena implements Runnable
{
    private static final KothScheduler KOTH_SCHEDULER = KothScheduler.getInstance();
    private static final Random RANDOM = new Random();

    private String name;
    private Vector3i firstPoint;
    private Vector3i secondPoint;
    private UUID worldUUID;
    private ArenaType type;

    private int minPlayers = 1;
    private int maxPlayers = 10;
    private final Set<Hill> hills;
    private final Map<String, ArenaTeam> teams;
    private ArenaStatus status;

    private Map<String, Integer> teamPoints = new HashMap<>();

    private Map<Hill, Set<Player>> hillUnderCapture = new HashMap<>();

    private Lobby lobby;
    private ArenaProperties arenaProperties;

    public Arena(final String name, final ArenaType type ,final UUID worldUUID, final Vector3i firstPoint, final Vector3i secondPoint)
    {
        this(name, type, worldUUID, firstPoint, secondPoint, new HashSet<>(), new HashSet<>(), new Lobby(Vector3i.ZERO,Vector3i.ZERO,Vector3i.ZERO), ArenaProperties.getDefaultProperties());
    }

    public Arena(final String name, final ArenaType type, final UUID worldUUID, final Vector3i firstPoint, final Vector3i secondPoint, final Set<Hill> hills, final Set<ArenaTeam> teams, final Lobby lobby, final ArenaProperties arenaProperties)
    {
        this.name = name;
        this.worldUUID = worldUUID;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.hills = hills;
        this.type = type;
        this.lobby = lobby;
        this.arenaProperties = arenaProperties;
        this.status = ArenaStatus.IDLE;

        this.teams = teams.stream().collect(Collectors.toMap(ArenaTeam::getName, arenaTeam -> arenaTeam));

        if(this.type == ArenaType.TEAMS)
        {
            //Precreate two teams
            final ArenaTeam arenaTeamRed = new ArenaTeam("red");
            final ArenaTeam arenaTeamBlue = new ArenaTeam("blue");
            this.teams.put(arenaTeamBlue.getName(), arenaTeamBlue);
            this.teams.put(arenaTeamRed.getName(), arenaTeamRed);
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

    public int getMinPlayers()
    {
        return this.minPlayers;
    }

    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public Set<Hill> getHills()
    {
        return this.hills;
    }

    public Map<String, ArenaTeam> getTeams()
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

    public Object getProperty(final ArenaProperties.PropertyKey propertyKey)
    {
        return this.arenaProperties.get(propertyKey);
    }

    public void putProperty(final ArenaProperties.PropertyKey propertyKey, final Object value)
    {
        this.arenaProperties.put(propertyKey, value);
    }

    public ArenaProperties getProperties()
    {
        return this.arenaProperties;
    }

    public Set<UUID> getPlayers()
    {
        return this.teams.values().stream().map(ArenaTeam::getPlayers).collect(HashSet::new, AbstractCollection::addAll, AbstractCollection::addAll);
    }

    public boolean isRoundBased()
    {
        return (boolean)this.arenaProperties.get(ArenaProperties.PropertyKey.ROUND_BASED);
    }

    public void setRoundBased(final boolean isRoundBased)
    {
        this.arenaProperties.put(ArenaProperties.PropertyKey.ROUND_BASED, isRoundBased);
    }

    public int getRoundTime()
    {
        return this.arenaProperties.get(ArenaProperties.PropertyKey.ROUND_TIME);
    }

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
        this.teams.put(team.getName(), team);
        return true;
    }

    public boolean removeTeam(final ArenaTeam team)
    {
        this.teams.remove(team.getName());
        return true;
    }

    public boolean addPlayer(final Player player)
    {
        if (!canJoin())
            return false;

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
            for(final ArenaTeam arenaTeam : this.teams.values())
            {
                if(arenaTeam.getPlayers().contains(player.getUniqueId()))
                    arenaTeamToRemove = arenaTeam;
            }
            return removeTeam(arenaTeamToRemove);
        }
        else if(this.type == ArenaType.TEAMS)
        {
            ArenaTeam arenaTeamToRemove = null;
            for(final ArenaTeam arenaTeam : this.teams.values())
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
        return this.teams.containsKey(name);
    }

    private ArenaTeam getTeamWithLeastPlayers()
    {
        ArenaTeam resultArena = null;
        for(final ArenaTeam arenaTeam : this.teams.values())
        {
            if(resultArena == null)
                resultArena = arenaTeam;
            if(arenaTeam.getPlayers().size() < resultArena.getPlayers().size())
                resultArena = arenaTeam;
        }
        return resultArena;
    }

    public boolean intersects(final UUID worldUUID, final Vector3i position)
    {
        if (!this.worldUUID.equals(worldUUID))
            return false;

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

    public int getPointsForTeam(final ArenaTeam arenaTeam)
    {
        return this.teamPoints.get(arenaTeam.getName());
    }

    public boolean setPointsForTeam(final ArenaTeam arenaTeam, final int points)
    {
        this.teamPoints.put(arenaTeam.getName(), points);
        return true;
    }

    @Override
    public void run()
    {
        this.status = ArenaStatus.IDLE;

        //Fires every 5 seconds and sets appropriate arena status
        KOTH_SCHEDULER.runAsyncWithInterval(5, TimeUnit.SECONDS, this::playerCountChecker);

        //Start queue;
    }

    private void startQueue()
    {
        //We don't want to start queue multiple times.
        if (this.status == ArenaStatus.QUEUE)
            return;

        if (this.getPlayers().size() >= this.minPlayers)
        {
            this.status = ArenaStatus.QUEUE;
            KOTH_SCHEDULER.runAsyncWithInterval(1, TimeUnit.SECONDS, new ArenaQueueTask(this));
            notifyPlayers(Text.of("Queue has been started! The game will start in 60 seconds!"));
        }
    }

    private void startGame()
    {
        if (this.status == ArenaStatus.RUNNING)
            return;
        this.status = ArenaStatus.RUNNING;

        for (final ArenaTeam arenaTeam : this.teams.values())
        {
            this.teamPoints.put(arenaTeam.getName(), 0);
        }

        KOTH_SCHEDULER.runAsyncWithInterval(1, TimeUnit.SECONDS, new ArenaTimerTask(this));

        // Spawn players in proper spawn points
        KOTH_SCHEDULER.getSyncExecutor().execute(this::spawnPlayers);
    }

    private void endGame()
    {
        // Calculate winner
        final Set<Player> winners = calculateWinners();
        KOTH_SCHEDULER.getSyncExecutor().execute(() -> awardPlayers(winners));

        // Move all players to lobby.
        KOTH_SCHEDULER.getSyncExecutor().execute(this::movePlayersToLobby);

        // Cleanup
        this.teamPoints.clear();
        this.status = ArenaStatus.IDLE;
    }

    private Set<Player> calculateWinners()
    {
        Map.Entry<String, Integer> maxEntry = null;
        for (final Map.Entry<String, Integer> entry : this.teamPoints.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        if (maxEntry == null)
            return new HashSet<>();

        final ArenaTeam arenaTeam = this.teams.get(maxEntry.getKey());
        return getSpongePlayers(arenaTeam.getPlayers());
    }

    private void awardPlayers(final Set<Player> players)
    {
        for (final Player player : players)
        {
            //TODO: Setup reward for winner.
            player.getInventory().offer(ItemStack.builder().itemType(ItemTypes.GOLDEN_APPLE).quantity(1).build());
            player.sendMessage(PluginInfo.PLUGIN_PREFIX.concat(Text.of(TextColors.GREEN, "You won! A reward has been placed in your inventory.")));
        }
    }

    private Set<Player> getSpongePlayers(final Collection<UUID> playerUUIDs)
    {
        final Set<Player> players = new HashSet<>();
        for (final UUID playerUUID : playerUUIDs)
        {
            final Optional<Player> optionalPlayer = Sponge.getServer().getPlayer(playerUUID);
            optionalPlayer.ifPresent(players::add);
        }
        return players;
    }

    private void movePlayersToLobby()
    {
        final World world = Sponge.getServer().getWorld(this.worldUUID).get();
        final Location<World> lobbyLocation = new Location<>(world, this.lobby.getSpawnPoint());
        for (final Player player : getSpongePlayers(getPlayers()))
        {
            player.setLocation(lobbyLocation);
        }
    }

//    private void movePlayerToLocation(final Set<Player> players, final Location<World> location)
//    {
//        for (final Player player : players)
//            player.setLocation(location);
//    }

    private void spawnPlayers()
    {
        final World world = Sponge.getServer().getWorld(this.worldUUID).get();
        for (final ArenaTeam arenaTeam : this.teams.values())
        {
            final Location<World> teamSpawnLocation = new Location<>(world, arenaTeam.getSpawnPoint());
            for (final UUID playerUUID : arenaTeam.getPlayers())
            {
                final Optional<Player> optionalPlayer = Sponge.getServer().getPlayer(playerUUID);
                if (!optionalPlayer.isPresent())
                {
                    // Illegal state! Player who is offline should not be in player list!
                    Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, "Illegal arena state! Offline player exists in arena team! Player UUID: " + playerUUID));
                    continue;
                }
                final Player player = optionalPlayer.get();
                player.setLocation(teamSpawnLocation);
                player.sendTitle(Title.builder().title(Text.of(TextColors.RED, "KOTH game started!")).build());
            }
        }
    }

    public void playerCountChecker()
    {
        if (this.status == ArenaStatus.IDLE)
        {
            if (getPlayers().size() >= this.minPlayers)
            {
                startQueue();
            }
        }
        else if (this.status == ArenaStatus.QUEUE)
        {
            if (getPlayers().size() < this.minPlayers)
                this.status = ArenaStatus.IDLE;
        }
        else if (this.status == ArenaStatus.RUNNING)
        {
            if (getPlayers().size() < this.minPlayers)
            {
                this.status = ArenaStatus.ENDING;
                //Prepare to end game...
            }
        }
        else if (this.status == ArenaStatus.ENDING)
        {
            //What can we put here?
        }
    }

    public boolean canJoin()
    {
        if (this.status == ArenaStatus.RUNNING || this.status == ArenaStatus.ENDING)
            return false;

        if (this.status == ArenaStatus.IDLE || this.status == ArenaStatus.QUEUE)
            return this.getPlayers().size() < this.maxPlayers;

        return false;
    }

    private void notifyPlayers(final Text text)
    {
        for (final UUID playerUUID : getPlayers())
        {
            final Optional<Player> optionalPlayer = Sponge.getServer().getPlayer(playerUUID);
            if (!optionalPlayer.isPresent())
            {
                // Illegal state! Player who is offline should not be in player list!
                Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_ERROR, "Illegal arena state! Offline player exists in arena team! Player UUID: " + playerUUID));
                continue;
            }
            final Player player = optionalPlayer.get();
            player.sendMessage(PluginInfo.PLUGIN_PREFIX.concat(text));
        }
    }

    public void onArenaStartEvent(final ArenaStartEvent event)
    {

    }

    public void onArenaStopEvent(final ArenaStopEvent event)
    {

    }

    //Occurs before the actual join
    public void onArenaJoinEvent(final ArenaJoinEvent event)
    {

    }

    //Occurs before the actual leave
    public void onArenaLeaveEvent(final ArenaLeaveEvent event)
    {

    }

    public void onArenaStatusChangeEvent(final ArenaStatusChangeEvent event)
    {

    }

    public void onArenaHillCaptureEvent(final ArenaHillCaptureEvent event)
    {

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return name.equals(arena.name) &&
                firstPoint.equals(arena.firstPoint) &&
                secondPoint.equals(arena.secondPoint) &&
                worldUUID.equals(arena.worldUUID) &&
                Objects.equals(arenaProperties, arena.arenaProperties);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, firstPoint, secondPoint, worldUUID);
    }

    public static class ArenaQueueTask implements Consumer<Task>
    {
        private final Arena arena;

        private int seconds = 0;
        private int maxQueueTime = 60; // 60 seconds

        public ArenaQueueTask(final Arena arena)
        {
            this.arena = arena;
        }

        @Override
        public void accept(Task task)
        {
            seconds++;

            if (seconds >= maxQueueTime)
            {
                task.cancel();
                CompletableFuture.runAsync(arena::startGame);
                return;
            }
            else if (seconds > maxQueueTime - 10)
            {
                arena.notifyPlayers(Text.of("Game starts in " + seconds));
            }

            //Stop queue if number of players is less than minimum.
            if (this.arena.getPlayers().size() < this.arena.getMinPlayers())
            {
                arena.status = ArenaStatus.IDLE;
                task.cancel();
                return;
            }
        }
    }

    public static class ArenaTimerTask implements Consumer<Task>
    {
        private final Arena arena;
        private int seconds = 0;
        private int maxGameTime = 900;

        public ArenaTimerTask(final Arena arena)
        {
            this.arena = arena;
        }

        @Override
        public void accept(Task task)
        {
            if (seconds >= maxGameTime || this.arena.getStatus() != ArenaStatus.RUNNING)
            {
                task.cancel();
                CompletableFuture.runAsync(arena::endGame);
                return;
            }
            else if (seconds > maxGameTime - 10)
            {
                arena.notifyPlayers(Text.of("Game ends in " + seconds));
            }
            seconds++;
        }
    }

    public static class CaptureHillTask implements Consumer<Task>
    {
        private final Arena arena;

        public CaptureHillTask(final Arena arena)
        {
            this.arena = arena;
        }

        @Override
        public void accept(Task task)
        {

        }
    }
}
