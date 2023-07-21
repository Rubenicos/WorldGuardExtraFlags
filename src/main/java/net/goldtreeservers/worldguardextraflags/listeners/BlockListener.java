package net.goldtreeservers.worldguardextraflags.listeners;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import net.goldtreeservers.worldguardextraflags.utils.SupportedFeatures;
import net.goldtreeservers.worldguardextraflags.wg.WorldGuardUtils;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

public class BlockListener implements Listener
{
	private final WorldGuardExtraFlagsPlugin plugin;

    public BlockListener(WorldGuardExtraFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
	public void onEntityBlockFormEvent(EntityBlockFormEvent event)
	{
		if (SupportedFeatures.isFrostwalkerSupported())
		{
			BlockState newState = event.getNewState();
			if (newState.getType() == Material.FROSTED_ICE)
			{
				ApplicableRegionSet regions = this.plugin.getWorldGuardCommunicator().getRegionContainer().createQuery().getApplicableRegions(newState.getLocation());
				
				Entity entity = event.getEntity();
				if (entity instanceof Player)
				{
					Player player = (Player)entity;
					if (WorldGuardUtils.queryValue(player, player.getWorld(), regions.getRegions(), Flags.FROSTWALKER) == State.DENY)
					{
						event.setCancelled(true);
					}
				}
				else
				{
					if (regions.queryValue(null, Flags.FROSTWALKER) == State.DENY)
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}

    public WorldGuardExtraFlagsPlugin getPlugin() {
        return this.plugin;
    }
}
