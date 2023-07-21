package net.goldtreeservers.worldguardextraflags.wg.wrappers;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Location;

public abstract class AbstractRegionQueryWrapper
{
	public abstract ApplicableRegionSet getApplicableRegions(Location location);
}
