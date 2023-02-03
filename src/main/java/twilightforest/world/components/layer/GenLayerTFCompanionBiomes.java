package twilightforest.world.components.layer;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.traits.CastleTransformer;

public enum GenLayerTFCompanionBiomes implements CastleTransformer {
	INSTANCE;

	GenLayerTFCompanionBiomes() { }

	private HolderGetter<Biome> registry;

	public GenLayerTFCompanionBiomes setup(HolderGetter<Biome> registry) {
		this.registry = registry;
		return this;
	}

	@Override
	public int apply(Context noise, int up, int left, int down, int right, int center) {
		int fireSwamp        = TFBiomeProvider.getBiomeId(TFBiomes.FIRE_SWAMP, registry);
		int swamp            = TFBiomeProvider.getBiomeId(TFBiomes.SWAMP, registry);
		int glacier          = TFBiomeProvider.getBiomeId(TFBiomes.GLACIER, registry);
		int snowyForest      = TFBiomeProvider.getBiomeId(TFBiomes.SNOWY_FOREST, registry);
		int darkForestCenter = TFBiomeProvider.getBiomeId(TFBiomes.DARK_FOREST_CENTER, registry);
		int darkForest       = TFBiomeProvider.getBiomeId(TFBiomes.DARK_FOREST, registry);
		int highlandsCenter  = TFBiomeProvider.getBiomeId(TFBiomes.FINAL_PLATEAU, registry);
		int highlands        = TFBiomeProvider.getBiomeId(TFBiomes.HIGHLANDS, registry);

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
