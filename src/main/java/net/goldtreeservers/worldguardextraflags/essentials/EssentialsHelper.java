package net.goldtreeservers.worldguardextraflags.essentials;

import com.earth2me.essentials.Essentials;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import net.goldtreeservers.worldguardextraflags.listeners.EssentialsListener;
import org.bukkit.plugin.Plugin;

public class EssentialsHelper {
    private final WorldGuardExtraFlagsPlugin plugin;
    private final Essentials essentialsPlugin;

    public EssentialsHelper(WorldGuardExtraFlagsPlugin plugin, Plugin essentialsPlugin) {
        this(plugin, (Essentials)essentialsPlugin);
    }

    public EssentialsHelper(WorldGuardExtraFlagsPlugin plugin, Essentials essentialsPlugin) {
        this.plugin = plugin;
        this.essentialsPlugin = essentialsPlugin;
    }

    public void onEnable() {
        this.plugin.getServer().getPluginManager().registerEvents(new EssentialsListener(this.plugin, this.essentialsPlugin), this.plugin);
    }

    public WorldGuardExtraFlagsPlugin getPlugin() {
        return this.plugin;
    }

    public Essentials getEssentialsPlugin() {
        return this.essentialsPlugin;
    }
}
