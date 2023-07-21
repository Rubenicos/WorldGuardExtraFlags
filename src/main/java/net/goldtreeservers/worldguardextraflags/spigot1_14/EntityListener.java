package net.goldtreeservers.worldguardextraflags.spigot1_14;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import net.goldtreeservers.worldguardextraflags.AbstractWorldGuardExtraFlagsPlugin;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class EntityListener implements Listener
{
	private final AbstractWorldGuardExtraFlagsPlugin plugin;

    public EntityListener(AbstractWorldGuardExtraFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
	public void onPortalCreateEvent(PortalCreateEvent event)
	{
		for(BlockState block : event.getBlocks())
		{
			ApplicableRegionSet regions = this.plugin.getWorldGuardCommunicator().getRegionContainer().createQuery().getApplicableRegions(block.getLocation());
			if (regions.queryValue(null, Flags.NETHER_PORTALS) == State.DENY)
			{
				event.setCancelled(true);
				break;
			}
		}
	}

    public AbstractWorldGuardExtraFlagsPlugin getPlugin() {
        return this.plugin;
    }
}
