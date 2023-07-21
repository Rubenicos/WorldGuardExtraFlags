package net.goldtreeservers.worldguardextraflags.we;

import com.sk89q.worldedit.Location;
import com.sk89q.worldedit.bukkit.BukkitUtil;

public class WorldEditUtils {

    public static org.bukkit.Location toLocation(Object location) {
        return BukkitUtil.toLocation((Location) location);
    }
}
