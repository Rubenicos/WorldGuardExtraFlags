package net.goldtreeservers.worldguardextraflags.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;

/**
 * Helper class to decide what features are supported by the server
 */
@SuppressWarnings("deprecation")
public class SupportedFeatures
{
	private static boolean frostwalkerSupported;
	private static boolean stopSoundSupported;
	private static boolean potionEffectEventSupported;
	private static boolean potionEffectParticles;
	private static boolean newMaterial;
	
	static
	{
		try
		{
			SupportedFeatures.frostwalkerSupported = Material.FROSTED_ICE != null;
		}
		catch (Throwable ignored)
		{
		}

		try
		{
			SupportedFeatures.stopSoundSupported = Player.class.getDeclaredMethod("stopSound", Sound.class) != null;
		}
		catch (Throwable ignored)
		{
		}

		try
		{
			SupportedFeatures.potionEffectEventSupported = EntityPotionEffectEvent.class != null;
		}
		catch (Throwable ignored)
		{
		}
		
		try
		{
			SupportedFeatures.potionEffectParticles = PotionEffect.class.getDeclaredMethod("hasParticles") != null;
		}
		catch(Throwable ignored)
		{
			
		}
		
		try
		{
			SupportedFeatures.newMaterial = Material.LEGACY_AIR != null;
		}
		catch(Throwable ignored)
		{
			
		}
	}

    public static boolean isFrostwalkerSupported() {
        return SupportedFeatures.frostwalkerSupported;
    }

    public static boolean isStopSoundSupported() {
        return SupportedFeatures.stopSoundSupported;
    }

    public static boolean isPotionEffectEventSupported() {
        return SupportedFeatures.potionEffectEventSupported;
    }

    public static boolean isPotionEffectParticles() {
        return SupportedFeatures.potionEffectParticles;
    }

    public static boolean isNewMaterial() {
        return SupportedFeatures.newMaterial;
    }
}
