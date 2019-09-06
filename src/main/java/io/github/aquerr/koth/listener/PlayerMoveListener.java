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
    public void onPlayerMove(final MoveEntityEvent event, final @Root Player player)
    {
        final Transform<World> fromTransform = event.getFromTransform();
        final Transform<World> toTransform = event.getToTransform();

        final Vector3i fromBlockPosition = fromTransform.getLocation().getBlockPosition();
        final Vector3i toBlockPosition = toTransform.getLocation().getBlockPosition();

        if (fromBlockPosition.equals(toBlockPosition))
            return;

        //TODO: This is a debug code and should be removed in the future.
        //TODO: Normally we should check if player goes outside the arena during game and if he/she goes outside then we should move them back or kick from the arena.
        //Check if player entered arena
        for (final Arena arena : this.arenasCache.values())
        {
            //Enters
            if(!arena.intersects(fromBlockPosition) && arena.intersects(toBlockPosition))
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You entered arena " + arena.getName() + "!"));
            }
            else if(arena.intersects(fromBlockPosition) && !arena.intersects(toBlockPosition))
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You left arena " + arena.getName() + "!"));
            }
        }
    }
}
