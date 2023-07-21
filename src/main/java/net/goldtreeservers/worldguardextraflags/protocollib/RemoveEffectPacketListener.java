package net.goldtreeservers.worldguardextraflags.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.sk89q.worldguard.session.Session;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;
import net.goldtreeservers.worldguardextraflags.wg.handlers.GiveEffectsFlagHandler;
import org.bukkit.entity.Player;

public class RemoveEffectPacketListener extends PacketAdapter {

    public RemoveEffectPacketListener() {
        super(WorldGuardExtraFlagsPlugin.getPlugin(), PacketType.Play.Server.REMOVE_ENTITY_EFFECT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
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
    }
}
