package twilightforest.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores custom grass color modifiers for Twilight Forest biomes.
 * Replaces NeoForge's enum extension for GrassColorModifier.
 * Custom modifiers are registered by {@link twilightforest.init.TFBiomes} during bootstrap.
 * The {@link twilightforest.mixin.BiomeMixin} applies these modifiers at runtime.
 */
public class TFGrassColorModifiers {

	private static final Map<ResourceKey<Biome>, GrassColorModifier> CUSTOM_MODIFIERS = new HashMap<>();

	/**
	 * Register a custom grass color modifier for a biome.
	 * Called during TFBiomes.bootstrap().
	 */
	public static void register(ResourceKey<Biome> biomeKey, GrassColorModifier modifier) {
		CUSTOM_MODIFIERS.put(biomeKey, modifier);
	}

	/**
	 * Get the custom grass color modifier for a biome, or null if none.
	 */
	public static GrassColorModifier getModifier(ResourceKey<Biome> biomeKey) {
		return CUSTOM_MODIFIERS.get(biomeKey);
	}

	@FunctionalInterface
	public interface GrassColorModifier {
		int modifyColor(double x, double z, int originalColor);
	}
}