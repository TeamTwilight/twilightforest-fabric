package twilightforest.world.components.layer;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.vanillalegacy.context.Context;

public enum GenLayerTFThornBorder implements IThornsTransformer {
	INSTANCE;

	private HolderGetter<Biome> registry;

	GenLayerTFThornBorder() { }

	public GenLayerTFThornBorder setup(HolderGetter<Biome> registry) {
		this.registry = registry;
		return this;
	}

	@Override
	public int apply(Context noise, int up, int left, int down, int right, int center, int nw, int sw, int se, int ne) {
		int highlandsCenter = TFBiomeProvider.getBiomeId(TFBiomes.FINAL_PLATEAU, registry);
		int thornlands      = TFBiomeProvider.getBiomeId(TFBiomes.THORNLANDS, registry);

		if (onBorder(highlandsCenter, center, right, left, up, down)) {
			return thornlands;
		} else if (onBorder(highlandsCenter, center, ne, nw, se, sw)) {
			return thornlands;
		} else {
			return center;
		}
	}

	/**
	 * Returns true if the center biome is not the specified biome and any of the surrounding biomes are the specified biomes
	 */
	private boolean onBorder(int biomeID, int center, int right, int left, int up, int down) {
		if (center == biomeID) {
			return false;
		} else if (right == biomeID) {
			return true;
		} else if (left == biomeID) {
			return true;
		} else if (up == biomeID) {
			return true;
		} else return down == biomeID;
	}
}
