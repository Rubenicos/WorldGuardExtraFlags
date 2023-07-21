package net.goldtreeservers.worldguardextraflags.wg.wrappers;

import org.bukkit.World;

import java.util.List;

public abstract class AbstractRegionContainerWrapper {
    public abstract AbstractRegionQueryWrapper createQuery();
    public abstract AbstractRegionManagerWrapper get(World world);

    public abstract List<AbstractRegionManagerWrapper> getLoaded();
}
