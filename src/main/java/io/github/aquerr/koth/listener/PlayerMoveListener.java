package io.github.aquerr.koth.listener;

public class PlayerMoveListener
{
//    private final Map<String, Arena> arenasCache;

//        this.arenasCache = plugin.getArenaManager().getArenas();

//    @Listener
//    @Exclude(MoveEntityEvent.Teleport.class)
//    public void onPlayerMove(final MoveEntityEvent event, final @Root Player player)
//    {
//        final World world = player.getWorld();
//
//        final Transform<World> fromTransform = event.getFromTransform();
//        final Transform<World> toTransform = event.getToTransform();
//
//        final Vector3i fromBlockPosition = fromTransform.getLocation().getBlockPosition();
//        final Vector3i toBlockPosition = toTransform.getLocation().getBlockPosition();
//
//        if (fromBlockPosition.equals(toBlockPosition))
//            return;
//
//        //Check if player entered arena
//        for (final Arena arena : this.arenasCache.values())
//        {
//            //Enters/leaves arena
//            if(super.getPlugin().getPlayersCreatingArena().containsKey(player.getUniqueId()) || super.getPlugin().getPlayersEditingArena().containsKey(player.getUniqueId()))
//                continue;
//
//            if(!arena.intersects(world.getUniqueId(), fromBlockPosition) && arena.intersects(world.getUniqueId(), toBlockPosition))
//            {
//                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't enter the arena " + arena.getName() + "!"));
//                event.setCancelled(true);
//            }
//            else if(arena.intersects(world.getUniqueId(), fromBlockPosition) && !arena.intersects(world.getUniqueId(), toBlockPosition))
//            {
//                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't leave the arena " + arena.getName() + "!"));
//                event.setCancelled(true);
//            }
//
//            //Enters/leaves lobby
//            if(!arena.getLobby().intersects(fromBlockPosition) && arena.getLobby().intersects(toBlockPosition))
//            {
//                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't enter the arena " + arena.getName() + "!"));
//                event.setCancelled(true);
//            }
//            //Enters/leaves lobby
//            else if(arena.getLobby().intersects(fromBlockPosition) && !arena.getLobby().intersects(toBlockPosition))
//            {
//                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, "You can't leave the arena " + arena.getName() + "!"));
//                event.setCancelled(true);
//            }
//        }
//    }
}
