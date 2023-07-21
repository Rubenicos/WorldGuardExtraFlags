package net.goldtreeservers.worldguardextraflags.wg.wrappers;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.Set;

public abstract class HandlerWrapper extends Handler {
    private final Plugin plugin;

    protected HandlerWrapper(Plugin plugin, Session session) {
        super(session);

        this.plugin = plugin;
    }

    public void initialize(Player player, Location current, ApplicableRegionSet set) {
    }

    public boolean onCrossBoundary(Player player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        return true;
    }

    public void tick(Player player, ApplicableRegionSet set) {
    }

    public State getInvincibility(Player player) {
        return null;
    }

    //@Override
    public void initialize(LocalPlayer localPlayer, com.sk89q.worldedit.util.Location current, ApplicableRegionSet set) {
        this.initialize(((BukkitPlayer)localPlayer).getPlayer(), adaptLoc(current), set);
    }

    // @Override
    public boolean onCrossBoundary(LocalPlayer localPlayer, com.sk89q.worldedit.util.Location from, com.sk89q.worldedit.util.Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        //It turns out that this is fired every time player moves
        //The plugin flags assume already that nothing changes unless region is crossed, so ignore when there isn't a region change
        //This optimization is already in place in the FVCH
        if (entered.isEmpty() && exited.isEmpty() && from.getExtent().equals(to.getExtent())) {
            return true;
        }

        return this.onCrossBoundary(((BukkitPlayer)localPlayer).getPlayer(), adaptLoc(from), adaptLoc(to), toSet, entered, exited, moveType);
    }

    // @Override
    public void tick(LocalPlayer localPlayer, ApplicableRegionSet set) {
        this.tick(((BukkitPlayer)localPlayer).getPlayer(), set);
    }

    // @Override
    public State getInvincibility(LocalPlayer localPlayer) {
        return this.getInvincibility(((BukkitPlayer)localPlayer).getPlayer());
    }

    public Plugin getHandlerPlugin() {
        return this.plugin;
    }

    // Method body taken from WorldEdit
    private static Location adaptLoc(com.sk89q.worldedit.util.Location location) {
        Vector position = location.toVector();
        return new Location(adaptWorld((com.sk89q.worldedit.world.World) location.getExtent()), position.getX(), position.getY(), position.getZ(), location.getYaw(), location.getPitch());
    }

    // Method body taken from WorldEdit
    private static World adaptWorld(com.sk89q.worldedit.world.World world) {
        Objects.requireNonNull(world);
        if (world instanceof BukkitWorld) {
            return ((BukkitWorld) world).getWorld();
        } else {
            World match = Bukkit.getServer().getWorld(world.getName());
            if (match != null) {
                return match;
            } else {
                throw new IllegalArgumentException("Can't find a Bukkit world for " + world.getName());
            }
        }
    }

    public abstract static class Factory<T extends HandlerWrapper> extends Handler.Factory<T> {
        private final Plugin plugin;

        public Factory(Plugin plugin) {
            this.plugin = plugin;
        }

        public Plugin getPlugin() {
            return this.plugin;
        }
    }
}
