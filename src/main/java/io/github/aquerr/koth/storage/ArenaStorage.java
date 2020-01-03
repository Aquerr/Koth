package io.github.aquerr.koth.storage;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.entity.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Singleton
public class ArenaStorage
{
    private final Koth plugin;

    private final Path arenasFile;
    private final ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private CommentedConfigurationNode configNode;

    @Inject
    public ArenaStorage(final Koth plugin)
    {
        this.plugin = plugin;
        this.arenasFile = plugin.getConfigDir().resolve("storage").resolve("arenas.conf");
        if(Files.notExists(this.arenasFile))
        {
            try
            {
                Files.createFile(arenasFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        this.configurationLoader = HoconConfigurationLoader.builder().setPath(this.arenasFile).setDefaultOptions(ConfigurationOptions.defaults()).build();
        try
        {
            this.configNode = this.configurationLoader.load();
            this.configurationLoader.save(this.configNode);
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean addArena(final Arena arena) throws ObjectMappingException
    {
        final ConfigurationNode arenaNode = this.configNode.getNode("arenas", arena.getName());
        arenaNode.getNode("worldUUID").setValue(TypeToken.of(UUID.class), arena.getWorldUniqueId());
        arenaNode.getNode("firstPoint").setValue(TypeToken.of(Vector3i.class), arena.getFirstPoint());
        arenaNode.getNode("secondPoint").setValue(TypeToken.of(Vector3i.class), arena.getSecondPoint());
        arenaNode.getNode("lobby").setValue(new TypeToken<Lobby>() {}, arena.getLobby());
        arenaNode.getNode("hills").setValue(new TypeToken<List<Hill>>() {}, new ArrayList<>(arena.getHills()));
        arenaNode.getNode("teams").setValue(new TypeToken<List<ArenaTeam>>() {}, new ArrayList<>(arena.getTeams()));
        arenaNode.getNode("maxPlayers").setValue(arena.getMaxPlayers());
        arenaNode.getNode("isRoundBased").setValue(arena.isRoundBased());
        arenaNode.getNode("roundTime").setValue(arena.getRoundTime().getSeconds());
        arenaNode.getNode("type").setValue(TypeToken.of(ArenaType.class), arena.getType());
        arenaNode.getNode("properties").setValue(TypeToken.of(ArenaProperties.class), arena.getProperties());

        return saveChanges();
    }

    public boolean updateArena(final Arena arena) throws ObjectMappingException
    {
        return addArena(arena);
    }

    public boolean deleteArena(final String name)
    {
        this.configNode.getNode("arenas").removeChild(name);
        return saveChanges();
    }

    public Arena getArena(final String name) throws ObjectMappingException
    {
        final ConfigurationNode arenaNode = this.configNode.getNode("arenas", name);
        final UUID worldUUID = arenaNode.getNode("worldUUID").getValue(TypeToken.of(UUID.class));
        final Vector3i firstPoint = arenaNode.getNode("firstPoint").getValue(TypeToken.of(Vector3i.class));
        final Vector3i secondPoint = arenaNode.getNode("secondPoint").getValue(TypeToken.of(Vector3i.class));
		final Lobby lobby = arenaNode.getNode("lobby").getValue(TypeToken.of(Lobby.class));
		final ArenaProperties arenaProperties = arenaNode.getNode("properties").getValue(TypeToken.of(ArenaProperties.class));

        //TODO: Do we really need a Set here?
        final Set<Hill> hills = new HashSet<>(arenaNode.getNode("hills").getList(TypeToken.of(Hill.class)));
        final Set<ArenaTeam> teams = new HashSet<>(arenaNode.getNode("teams").getList(TypeToken.of(ArenaTeam.class)));

        final ArenaType type = arenaNode.getNode("type").getValue(TypeToken.of(ArenaType.class));
        final Arena arena = new Arena(name, type , worldUUID, firstPoint, secondPoint, hills, teams, lobby, arenaProperties);
        return arena;
    }

    public List<Arena> getArenas() throws ObjectMappingException
    {
        final ConfigurationNode arenasNode = this.configNode.getNode("arenas");
        final Map<Object, ? extends ConfigurationNode> arenaNodes =  arenasNode.getChildrenMap();
        final Set<Object> arenaNames = arenaNodes.keySet();
        final List<Arena> arenas = new ArrayList<>();
        for (final Object arenaName : arenaNames)
        {
            if(!(arenaName instanceof String))
                continue;
            final Arena arena = getArena(String.valueOf(arenaName));
            arenas.add(arena);
        }
        return arenas;
    }

    private boolean saveChanges()
    {
        try
        {
            configurationLoader.save(configNode);
            return true;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
