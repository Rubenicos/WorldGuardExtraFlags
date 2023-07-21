package net.goldtreeservers.worldguardextraflags.wg.wrappers.v6;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import net.goldtreeservers.worldguardextraflags.wg.wrappers.AbstractRegionManagerWrapper;
import org.bukkit.Location;

public class RegionManagerWrapper extends AbstractRegionManagerWrapper {
    public RegionManagerWrapper(RegionManager regionManager) {
        super(regionManager);
    }

    @Override
    public ApplicableRegionSet getApplicableRegions(Location location) {
        return this.regionManager.getApplicableRegions(BukkitUtil.toVector(location));
    }
}
