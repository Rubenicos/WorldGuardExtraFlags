package net.goldtreeservers.worldguardextraflags;

import net.goldtreeservers.worldguardextraflags.wg.wrappers.WorldGuardCommunicator;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractWorldGuardExtraFlagsPlugin extends JavaPlugin {
    protected WorldGuardCommunicator worldGuardCommunicator;

    public WorldGuardCommunicator getWorldGuardCommunicator() {
        return this.worldGuardCommunicator;
    }
}
