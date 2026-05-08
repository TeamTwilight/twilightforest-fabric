package twilightforest.enums.extensions;

import me.shedaniel.mm.api.ClassTinkerers;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

/**
 * Accessors for the Manningham Mills enum-subclass values installed by
 * {@code CodexGrassColorEarlyRiser}.
 */
public final class TFGrassColorModifierEnumExtension {
	public static final BiomeSpecialEffects.GrassColorModifier ENCHANTED_FOREST = get("TWILIGHTFOREST_ENCHANTED_FOREST");
	public static final BiomeSpecialEffects.GrassColorModifier SWAMP = get("TWILIGHTFOREST_SWAMP");
	public static final BiomeSpecialEffects.GrassColorModifier DARK_FOREST = get("TWILIGHTFOREST_DARK_FOREST");
	public static final BiomeSpecialEffects.GrassColorModifier DARK_FOREST_CENTER = get("TWILIGHTFOREST_DARK_FOREST_CENTER");
	public static final BiomeSpecialEffects.GrassColorModifier SPOOKY_FOREST = get("TWILIGHTFOREST_SPOOKY_FOREST");

	private TFGrassColorModifierEnumExtension() {
	}

	private static BiomeSpecialEffects.GrassColorModifier get(String name) {
		return ClassTinkerers.getEnum(BiomeSpecialEffects.GrassColorModifier.class, name);
	}
}
