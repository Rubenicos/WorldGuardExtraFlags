package net.goldtreeservers.worldguardextraflags.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * Helper class to decide what features are supported by the server
 */
public class SupportedFeatures {
    private static boolean frostwalkerSupported;
    private static boolean stopSoundSupported;
    private static boolean potionEffectParticles;

    static {
        try {
            SupportedFeatures.frostwalkerSupported = Material.FROSTED_ICE != null;
        } catch (Throwable ignored) { }

        try {
            SupportedFeatures.stopSoundSupported = Player.class.getDeclaredMethod("stopSound", Sound.class) != null;
        } catch (Throwable ignored) { }

        try {
            SupportedFeatures.potionEffectParticles = PotionEffect.class.getDeclaredMethod("hasParticles") != null;
        } catch (Throwable ignored) { }
    }

    public static boolean isFrostwalkerSupported() {
        return SupportedFeatures.frostwalkerSupported;
    }

    public static boolean isStopSoundSupported() {
        return SupportedFeatures.stopSoundSupported;
    }

    public static boolean isPotionEffectParticles() {
        return SupportedFeatures.potionEffectParticles;
    }
}
