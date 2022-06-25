package twilightforest.world.components.layer;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.traits.CastleTransformer;
import twilightforest.init.BiomeKeys;

public enum GenLayerTFCompanionBiomes implements CastleTransformer {
	INSTANCE;

	GenLayerTFCompanionBiomes() { }

	private Registry<Biome> registry;

	public GenLayerTFCompanionBiomes setup(Registry<Biome> registry) {
		this.registry = registry;
		return this;
	}

	@Override
	public int apply(Context noise, int up, int left, int down, int right, int center) {
		int fireSwamp        = TFBiomeProvider.getBiomeId(BiomeKeys.FIRE_SWAMP, registry);
		int swamp            = TFBiomeProvider.getBiomeId(BiomeKeys.SWAMP, registry);
		int glacier          = TFBiomeProvider.getBiomeId(BiomeKeys.GLACIER, registry);
		int snowyForest      = TFBiomeProvider.getBiomeId(BiomeKeys.SNOWY_FOREST, registry);
		int darkForestCenter = TFBiomeProvider.getBiomeId(BiomeKeys.DARK_FOREST_CENTER, registry);
		int darkForest       = TFBiomeProvider.getBiomeId(BiomeKeys.DARK_FOREST, registry);
		int highlandsCenter  = TFBiomeProvider.getBiomeId(BiomeKeys.FINAL_PLATEAU, registry);
		int highlands        = TFBiomeProvider.getBiomeId(BiomeKeys.HIGHLANDS, registry);

		if (isKey(fireSwamp, center, right, left, up, down)) {
			return swamp;
		} else if (isKey(glacier, center, right, left, up, down)) {
			return snowyForest;
		} else if (isKey(darkForestCenter, center, right, left, up, down)) {
			return darkForest;
		} else if (isKey(highlandsCenter, center, right, left, up, down)) {
			return highlands;
		} else {
			return center;
		}
	}

	/**
	 * Returns true if any of the surrounding biomes is the specified biome
	 */
	boolean isKey(int biome, int center, int right, int left, int up, int down) {
		return center != biome && (right == biome || left == biome || up == biome || down == biome);
	}
}
