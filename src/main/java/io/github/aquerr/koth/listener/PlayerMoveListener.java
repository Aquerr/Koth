package io.github.aquerr.koth.listener;

import com.flowpowered.math.vector.Vector3i;
import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Map;

public class PlayerMoveListener extends AbstractListener
{
    private final Map<String, Arena> arenasCache;

    public PlayerMoveListener(final Koth plugin)
    {
        super(plugin);
        this.arenasCache = plugin.getArenaManager().getArenas();
    }

    @Listener
    @Exclude(MoveEntityEvent.Teleport.class)
    public void onPlayerMove(final MoveEntityEvent event, final @Root Player player)
    {
        final World world = player.getWorld();

        final Transform<World> fromTransform = event.getFromTransform();
        final Transform<World> toTransform = event.getToTransform();

        final Vector3i fromBlockPosition = fromTransform.getLocation().getBlockPosition();
        final Vector3i toBlockPosition = toTransform.getLocation().getBlockPosition();

        if (fromBlockPosition.equals(toBlockPosition))
            return;

        //Check if player entered arena
        for (final Arena arena : this.arenasCache.values())
        {
            //Enters/leaves arena
            if(super.getPlugin().getPlayersCreatingArena().containsKey(player.getUniqueId()) || super.getPlugin().getPlayersEditingArena().containsKey(player.getUniqueId()))
                continue;

            if(!arena.intersects(world.getUniqueId(), fromBlockPosition) && arena.intersects(world.getUniqueId(), toBlockPosition))
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't enter the arena " + arena.getName() + "!"));
                event.setCancelled(true);
            }
            else if(arena.intersects(world.getUniqueId(), fromBlockPosition) && !arena.intersects(world.getUniqueId(), toBlockPosition))
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't leave the arena " + arena.getName() + "!"));
                event.setCancelled(true);
            }

            //Enters/leaves lobby
            if(!arena.getLobby().intersects(fromBlockPosition) && arena.getLobby().intersects(toBlockPosition))
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't enter the arena " + arena.getName() + "!"));
                event.setCancelled(true);
            }
            //Enters/leaves lobby
            else if(arena.getLobby().intersects(fromBlockPosition) && !arena.getLobby().intersects(toBlockPosition))
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't leave the arena " + arena.getName() + "!"));
                event.setCancelled(true);
            }
        }
    }
}
