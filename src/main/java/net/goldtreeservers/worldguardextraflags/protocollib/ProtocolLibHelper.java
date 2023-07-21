package net.goldtreeservers.worldguardextraflags.protocollib;

import com.comphenix.protocol.ProtocolLibrary;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import org.bukkit.plugin.Plugin;

public class ProtocolLibHelper
{
	private final WorldGuardExtraFlagsPlugin plugin;
	private final Plugin protocolLibPlugin;
	
	public ProtocolLibHelper(WorldGuardExtraFlagsPlugin plugin, Plugin protocolLibPlugin)
	{
		this.plugin = plugin;
		this.protocolLibPlugin = protocolLibPlugin;
	}

	public void onEnable()
	{
		ProtocolLibrary.getProtocolManager().addPacketListener(new RemoveEffectPacketListener());
	}

    public WorldGuardExtraFlagsPlugin getPlugin() {
        return this.plugin;
    }

    public Plugin getProtocolLibPlugin() {
        return this.protocolLibPlugin;
    }
}
