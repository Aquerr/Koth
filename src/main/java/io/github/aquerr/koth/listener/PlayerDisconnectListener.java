package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Optional;

public class PlayerDisconnectListener extends AbstractListener
{
    public PlayerDisconnectListener(final Koth plugin)
    {
        super(plugin);
    }

    @Listener
    public void onPlayerLeave(final ClientConnectionEvent.Disconnect event, final @Root Player player)
    {
        super.getPlugin().getPlayersCreatingArena().remove(player.getUniqueId());
        super.getPlugin().getPlayersEditingArena().remove(player.getUniqueId());

        final Optional<Arena> optionalArena = super.getPlugin().getArenaManager().getArenaForUser(player);
        optionalArena.ifPresent(arena -> arena.removePlayer(player));
    }
}
