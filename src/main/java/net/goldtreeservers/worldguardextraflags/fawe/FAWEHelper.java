package net.goldtreeservers.worldguardextraflags.fawe;

import com.boydti.fawe.FaweAPI;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import org.bukkit.plugin.Plugin;

public class FAWEHelper
{
	private final WorldGuardExtraFlagsPlugin plugin;
	private final Plugin fawePlugin;

	public FAWEHelper(WorldGuardExtraFlagsPlugin plugin, Plugin fawePlugin) {
		this.plugin = plugin;
		this.fawePlugin = fawePlugin;
	}

	public void onEnable()
	{
		FaweAPI.addMaskManager(new FaweWorldEditFlagMaskManager(this.plugin));
	}

	public WorldGuardExtraFlagsPlugin getPlugin() {
		return this.plugin;
	}

	public Plugin getFawePlugin() {
		return this.fawePlugin;
	}
}
