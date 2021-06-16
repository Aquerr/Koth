package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.manager.SelectionManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class PlayerDisconnectListener
{
    private final SelectionManager selectionManager;

    public PlayerDisconnectListener(final SelectionManager selectionManager)
    {
        this.selectionManager = selectionManager;
    }

    @Listener
    public void onPlayerLeave(final ServerSideConnectionEvent.Disconnect event, final @Root Player player)
    {
//        super.getPlugin().getPlayersCreatingArena().remove(player.uniqueId());
//        super.getPlugin().getPlayersEditingArena().remove(player.uniqueId());

//        final Optional<Arena> optionalArena = super.getPlugin().getArenaManager().getArenaForUser(player);
//        optionalArena.ifPresent(arena -> arena.removePlayer(player));

        this.selectionManager.removeSelectionPointsForPlayer(player);
    }
}
