package net.goldtreeservers.worldguardextraflags.listeners;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import net.ess3.api.IUser;
import net.ess3.api.events.GodStatusChangeEvent;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import net.goldtreeservers.worldguardextraflags.wg.WorldGuardUtils;
import net.goldtreeservers.worldguardextraflags.wg.handlers.FlyFlagHandler;
import net.goldtreeservers.worldguardextraflags.wg.handlers.GodmodeFlagHandler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class EssentialsListener implements Listener
{
	private final WorldGuardExtraFlagsPlugin plugin;
	private final Essentials essentialsPlugin;

    public EssentialsListener(WorldGuardExtraFlagsPlugin plugin, Essentials essentialsPlugin) {
        this.plugin = plugin;
        this.essentialsPlugin = essentialsPlugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onGodStatusChangeEvent(GodStatusChangeEvent event)
	{
		IUser user = event.getAffected();
		Player player = user.getBase();
		
		ApplicableRegionSet regions = this.plugin.getWorldGuardCommunicator().getRegionContainer().createQuery().getApplicableRegions(player.getLocation());
		
		State state = WorldGuardUtils.queryState(player, player.getWorld(), regions.getRegions(), Flags.GODMODE);
		if (state != null)
		{
			if (this.plugin.getWorldGuardCommunicator().getSessionManager().get(player).getHandler(GodmodeFlagHandler.class).getIsGodmodeEnabled() != null)
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event)
	{
		Player player = event.getPlayer();
		
		if (player.getGameMode() != GameMode.CREATIVE && !this.essentialsPlugin.getUser(player).isAuthorized("essentials.fly"))
		{
			//Essentials now turns off flight, fuck him
			Boolean value = this.plugin.getWorldGuardCommunicator().getSessionManager().get(player).getHandler(FlyFlagHandler.class).getCurrentValue();
			if (value != null)
			{
				player.setAllowFlight(value);
			}
		}
	}

    public WorldGuardExtraFlagsPlugin getPlugin() {
        return this.plugin;
    }

    public Essentials getEssentialsPlugin() {
        return this.essentialsPlugin;
    }
}
