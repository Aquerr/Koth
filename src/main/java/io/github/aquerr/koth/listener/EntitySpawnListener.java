package io.github.aquerr.koth.listener;

import org.spongepowered.api.entity.living.player.Player;

public class EntitySpawnListener
{
//	private final Map<String, Arena> arenasCache;


//	@Listener(order = Order.EARLY, beforeModifications = true)
//	public void onLivingMobSpawn(final SpawnEntityEvent event)
//	{
//		for(final Entity entity : event.getEntities())
//		{
//			if(entity instanceof Player)
//			{
//				final boolean canSpawn = canSpawnPlayer((Player)entity);
//				if(!canSpawn)
//				{
//					event.setCancelled(true);
//					return;
//				}
//			}
//
//			if(entity instanceof Living)
//			{
//				final boolean canSpawn = canSpawnMob((Living)entity);
//				if(!canSpawn)
//				{
//					event.setCancelled(true);
//					return;
//				}
//			}
//		}
//	}

	private boolean canSpawnPlayer(final Player player)
	{
		//TODO: Add more here if needed.
		return true;
	}

//	private boolean canSpawnMob(final Living entity)
//	{
//		for(final Arena arena : this.arenasCache.values())
//		{
//			final Location<World> entityLocation = entity.getLocation();
//			if(arena.intersects(entityLocation.getExtent().getUniqueId(), entityLocation.getBlockPosition()))
//			{
//				return false;
//			}
//			else if(arena.getLobby().intersects(entityLocation.getBlockPosition()))
//			{
//				return false;
//			}
//		}
//		return true;
//	}
}
