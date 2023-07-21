package net.goldtreeservers.worldguardextraflags;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import net.goldtreeservers.worldguardextraflags.essentials.EssentialsHelper;
import net.goldtreeservers.worldguardextraflags.fawe.FAWEHelper;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import net.goldtreeservers.worldguardextraflags.listeners.*;
import net.goldtreeservers.worldguardextraflags.protocollib.ProtocolLibHelper;
import net.goldtreeservers.worldguardextraflags.utils.SupportedFeatures;
import net.goldtreeservers.worldguardextraflags.wg.WorldGuardUtils;
import net.goldtreeservers.worldguardextraflags.wg.wrappers.WorldGuardCommunicator;
import net.goldtreeservers.worldguardextraflags.wg.wrappers.v6.WorldGuardSixCommunicator;
import net.goldtreeservers.worldguardextraflags.wg.wrappers.v7.WorldGuardSevenCommunicator;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class WorldGuardExtraFlagsPlugin extends AbstractWorldGuardExtraFlagsPlugin {
    private static WorldGuardExtraFlagsPlugin plugin;

    private WorldGuardPlugin worldGuardPlugin;
    private WorldEditPlugin worldEditPlugin;

    private EssentialsHelper essentialsHelper;
    private FAWEHelper faweHelper;
    private ProtocolLibHelper protocolLibHelper;

    public WorldGuardExtraFlagsPlugin() {
        WorldGuardExtraFlagsPlugin.plugin = this;
    }

    public static WorldGuardExtraFlagsPlugin getPlugin() {
        return WorldGuardExtraFlagsPlugin.plugin;
    }

    @Override
    public void onLoad() {
        this.worldEditPlugin = (WorldEditPlugin)this.getServer().getPluginManager().getPlugin("WorldEdit");
        this.worldGuardPlugin = (WorldGuardPlugin)this.getServer().getPluginManager().getPlugin("WorldGuard");

        this.worldGuardCommunicator = WorldGuardExtraFlagsPlugin.createWorldGuardCommunicator();
        if (this.worldGuardCommunicator == null) {
            throw new RuntimeException("Unsupported WorldGuard version: " + this.worldGuardPlugin.getDescription().getVersion());
        }

        WorldGuardUtils.setCommunicator(this.worldGuardCommunicator);

        try {
            this.worldGuardCommunicator.onLoad(this);
        } catch (Exception e) {
            this.getServer().getPluginManager().disablePlugin(this);

            throw new RuntimeException("Failed to load WorldGuard communicator", e);
        }

        //Soft dependencies, due to some compatibility issues or add flags related to a plugin
        try {
            Plugin essentialsPlugin = WorldGuardExtraFlagsPlugin.getPlugin().getServer().getPluginManager().getPlugin("Essentials");
            if (essentialsPlugin != null) {
                this.essentialsHelper = new EssentialsHelper(this, essentialsPlugin);
            }
        } catch (Throwable ignore) { }

        try {
            Plugin fastAsyncWorldEditPlugin = this.getServer().getPluginManager().getPlugin("FastAsyncWorldEdit");
            if (fastAsyncWorldEditPlugin != null) {
                this.faweHelper = new FAWEHelper(this, fastAsyncWorldEditPlugin);
            }
        } catch (Throwable ignore) { }

        try {
            Plugin protocolLibPlugin = this.getServer().getPluginManager().getPlugin("ProtocolLib");
            if (protocolLibPlugin != null) {
                this.protocolLibHelper = new ProtocolLibHelper(this, protocolLibPlugin);
            }
        } catch (Throwable ignore) { }
    }

    @Override
    public void onEnable() {
        if (this.worldGuardCommunicator == null) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            this.worldGuardCommunicator.onEnable(this);
        } catch (Exception e) {
            this.getServer().getPluginManager().disablePlugin(this);

            throw new RuntimeException("Failed to enable WorldGuard communicator", e);
        }

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(this), this);

        if (this.worldGuardCommunicator.isLegacy()) {
            this.getServer().getPluginManager().registerEvents(new BlockListenerWG(this), this);
        }

        try {
            if (EntityToggleGlideEvent.class != null) {
                //LOL, Just making it look nice xD
                this.getServer().getPluginManager().registerEvents(new EntityListenerOnePointNine(this), this);
            }
        } catch (NoClassDefFoundError ignored) { }

        try {
            ParameterizedType type = (ParameterizedType)PortalCreateEvent.class.getDeclaredField("blocks").getGenericType();
            Class<?> clazz = (Class<?>)type.getActualTypeArguments()[0];
            if (clazz == BlockState.class) {
                this.getServer().getPluginManager().registerEvents(new net.goldtreeservers.worldguardextraflags.spigot1_14.EntityListener(this), this);
            } else {
                this.getServer().getPluginManager().registerEvents(new EntityListener(this), this);
            }
        } catch (Throwable t) {
            this.getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        }

        if (this.faweHelper != null) {
            this.faweHelper.onEnable();
        } else {
            this.worldEditPlugin.getWorldEdit().getEventBus().register(new WorldEditListener(this));
        }

        if (this.essentialsHelper != null) {
            this.essentialsHelper.onEnable();
        }

        if (this.protocolLibHelper != null) {
            this.protocolLibHelper.onEnable();
        } else if (SupportedFeatures.isPotionEffectEventSupported()) {
            this.getServer().getPluginManager().registerEvents(new EntityPotionEffectEventListener(this), this);
        }

        for (World world : this.getServer().getWorlds()) {
            this.getWorldGuardCommunicator().doUnloadChunkFlagCheck(world);
        }
    }

    private Set<Flag<?>> getPluginFlags() {
        Set<Flag<?>> flags = new HashSet<>();

        for (Field field : Flags.class.getFields()) {
            try {
                flags.add((Flag<?>)field.get(null));
            } catch (IllegalArgumentException | IllegalAccessException e) { }
        }

        return flags;
    }

    public static WorldGuardCommunicator createWorldGuardCommunicator() {
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard"); //Only exists in WG 7

            return new WorldGuardSevenCommunicator();
        } catch (Throwable ignored) { }

        try {
            Class<?> clazz = Class.forName("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
            if (clazz.getMethod("getFlagRegistry") != null) {
                return new WorldGuardSixCommunicator();
            }
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }

        return null;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        return this.worldGuardPlugin;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return this.worldEditPlugin;
    }

    public EssentialsHelper getEssentialsHelper() {
        return this.essentialsHelper;
    }

    public FAWEHelper getFaweHelper() {
        return this.faweHelper;
    }

    public ProtocolLibHelper getProtocolLibHelper() {
        return this.protocolLibHelper;
    }
}
