package io.github.aquerr.koth.listener;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.entity.Arena;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import java.util.Optional;

public class PlayerAttackListener extends AbstractListener
{
	public PlayerAttackListener(final Koth plugin)
	{
		super(plugin);
	}

	@Listener
	public void onPlayerAttack(final AttackEntityEvent event, final @Root Player player)
	{
		final Entity entity = event.getTargetEntity();
		if(!(entity instanceof Player))
			return;

		final Player attackedPlayer = (Player)entity;

		//Check if player is playing arena
		final Optional<Arena> arena = super.getPlugin().getArenaManager().getArenaForPlayer(attackedPlayer);
		if(!arena.isPresent())
			return;

		//Players should not be able to attack each other inside lobby
		if(arena.get().getLobby().intersects(attackedPlayer.getLocation().getBlockPosition()))
		{
			event.setCancelled(true);
			return;
		}
	}
}
