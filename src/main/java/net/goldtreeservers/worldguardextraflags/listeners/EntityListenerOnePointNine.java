package net.goldtreeservers.worldguardextraflags.listeners;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import net.goldtreeservers.worldguardextraflags.flags.helpers.ForcedStateFlag.ForcedState;
import net.goldtreeservers.worldguardextraflags.wg.WorldGuardUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class EntityListenerOnePointNine implements Listener
{
	private final WorldGuardExtraFlagsPlugin plugin;

    public EntityListenerOnePointNine(WorldGuardExtraFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
	public void onEntityToggleGlideEvent(EntityToggleGlideEvent event)
	{
		Entity entity = event.getEntity();
		if (entity instanceof Player)
		{
			Player player = (Player)entity;
			
			ApplicableRegionSet regions = this.plugin.getWorldGuardCommunicator().getRegionContainer().createQuery().getApplicableRegions(player.getLocation());

			ForcedState state = WorldGuardUtils.queryValue(player, player.getWorld(), regions.getRegions(), Flags.GLIDE);
			switch(state)
			{
				case ALLOW:
					break;
				case DENY:
				{
					if (!event.isGliding())
					{
						return;
					}

					event.setCancelled(true);
					
					//Prevent the player from being allowed to glide by spamming space
					player.teleport(player.getLocation());
					
					break;
				}
				case FORCE:
				{
					if (event.isGliding())
					{
						return;
					}

					event.setCancelled(true);
					
					break;
				}
			}
		}
	}

    public WorldGuardExtraFlagsPlugin getPlugin() {
        return this.plugin;
    }
}
