package twilightforest.data.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import twilightforest.TwilightForestMod;

/**
 * Codex Fabric port of upstream {@code twilightforest.data.tags.FluidTagGenerator}.
 * Currently only declares the {@code FIRE_JET_FUEL} tag (used by FireJet detection).
 * Add more upstream fluid tags here as their consumer code ports.
 */
public final class FluidTagGenerator {
    public static final TagKey<Fluid> FIRE_JET_FUEL = create("fire_jet_fuel");

    private FluidTagGenerator() {
    }

    private static TagKey<Fluid> create(String path) {
        return TagKey.create(Registries.FLUID, TwilightForestMod.prefix(path));
    }
}
