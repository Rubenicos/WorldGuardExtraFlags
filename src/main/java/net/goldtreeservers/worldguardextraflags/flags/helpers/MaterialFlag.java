package net.goldtreeservers.worldguardextraflags.flags.helpers;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import net.goldtreeservers.worldguardextraflags.utils.SupportedFeatures;
import org.bukkit.Material;

public class MaterialFlag extends Flag<Material> {
    public MaterialFlag(String name) {
        super(name);
    }

    @Override
    public Object marshal(Material o) {
        return o.name();
    }

    @Override
    public Material parseInput(FlagContext context) throws InvalidFlagFormat {
        Material material = Material.matchMaterial(context.getUserInput());
        if (material != null) {
            return material;
        } else {
            throw new InvalidFlagFormat("Unable to find the material! Please refer to https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for valid ids");
        }
    }

    @Override
    public Material unmarshal(Object o) {
        Material material = Material.matchMaterial(o.toString());
        //Fallback to legacy on unmarshal only
        if (material == null && SupportedFeatures.isNewMaterial()) {
            material = Material.matchMaterial(o.toString(), true);
        }

        return material;
    }
}
