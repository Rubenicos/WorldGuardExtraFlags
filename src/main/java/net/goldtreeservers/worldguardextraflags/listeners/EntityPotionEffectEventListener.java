package net.goldtreeservers.worldguardextraflags.listeners;

import com.sk89q.worldguard.session.Session;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import net.goldtreeservers.worldguardextraflags.wg.handlers.GiveEffectsFlagHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public class EntityPotionEffectEventListener implements Listener {
    private final WorldGuardExtraFlagsPlugin plugin;

    public EntityPotionEffectEventListener(WorldGuardExtraFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
        if (event.getAction() != EntityPotionEffectEvent.Action.REMOVED) {
            return;
        }

        if (event.getCause() != EntityPotionEffectEvent.Cause.PLUGIN) {
            return;
        }

        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player)entity;
        //Work around, getIfPresent is broken inside WG due to using LocalPlayer as key instead of CacheKey
        if (!player.isValid()) {
            return;
        }

        try {
            Session session = WorldGuardExtraFlagsPlugin.getPlugin().getWorldGuardCommunicator().getSessionManager().get(player);

            GiveEffectsFlagHandler giveEffectsHandler = session.getHandler(GiveEffectsFlagHandler.class);
            if (giveEffectsHandler.isSupressRemovePotionPacket()) {
                event.setCancelled(true);
            }
        } catch(IllegalStateException ignored) { }
    }

    public WorldGuardExtraFlagsPlugin getPlugin() {
        return this.plugin;
    }
}
