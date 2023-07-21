package net.goldtreeservers.worldguardextraflags.wg.wrappers.v6;

import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import net.goldtreeservers.worldguardextraflags.wg.wrappers.AbstractRegionQueryWrapper;
import org.bukkit.Location;

public class RegionQueryWrapper extends AbstractRegionQueryWrapper
{
	private final RegionQuery regionQuery;

    public RegionQueryWrapper(RegionQuery regionQuery) {
        this.regionQuery = regionQuery;
    }

    @Override
	public ApplicableRegionSet getApplicableRegions(Location location)
	{
		return this.regionQuery.getApplicableRegions(location);
	}
}
