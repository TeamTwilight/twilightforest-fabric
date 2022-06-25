package twilightforest.world.components.layer;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.traits.CastleTransformer;
import twilightforest.init.BiomeKeys;

public enum GenLayerTFStream implements CastleTransformer {

	INSTANCE;

	private Registry<Biome> registry;

	GenLayerTFStream() { }

	public GenLayerTFStream setup(Registry<Biome> registry) {
		this.registry = registry;
		return this;
	}
	
	@Override
	public int apply(Context iNoiseRandom, int up, int left, int down, int right, int mid) {
		if (shouldStream(mid, left) || shouldStream(mid, right) || shouldStream(mid, down) || shouldStream(mid, up)) {
			return TFBiomeProvider.getBiomeId(BiomeKeys.STREAM, registry);
		} else {
			return mid;
		}
	}

	
	boolean shouldStream(int biome1, int biome2) {
		if (biome1 == biome2) {
			return false;
		}

		if (biome1 == -biome2) {
			return false;
		}
		
		final int tfLake = TFBiomeProvider.getBiomeId(BiomeKeys.LAKE, registry);
		final int thornlands = TFBiomeProvider.getBiomeId(BiomeKeys.THORNLANDS, registry);
		
		return !(testEitherBiomeOR(biome1, biome2, tfLake, tfLake)
				|| testEitherBiomeOR(biome1, biome2, thornlands, thornlands)
				|| testEitherBiomeOR(biome1, biome2, TFBiomeProvider.getBiomeId(BiomeKeys.CLEARING, registry), TFBiomeProvider.getBiomeId(BiomeKeys.OAK_SAVANNAH, registry))
				|| testEitherBiomeAND(biome1, biome2, TFBiomeProvider.getBiomeId(BiomeKeys.SNOWY_FOREST, registry), TFBiomeProvider.getBiomeId(BiomeKeys.GLACIER, registry))
				|| testEitherBiomeAND(biome1, biome2, TFBiomeProvider.getBiomeId(BiomeKeys.MUSHROOM_FOREST, registry), TFBiomeProvider.getBiomeId(BiomeKeys.DENSE_MUSHROOM_FOREST, registry))
				|| testEitherBiomeAND(biome1, biome2, TFBiomeProvider.getBiomeId(BiomeKeys.SWAMP, registry), TFBiomeProvider.getBiomeId(BiomeKeys.FIRE_SWAMP, registry))
				|| testEitherBiomeAND(biome1, biome2, TFBiomeProvider.getBiomeId(BiomeKeys.DARK_FOREST, registry), TFBiomeProvider.getBiomeId(BiomeKeys.DARK_FOREST_CENTER, registry))
				|| testEitherBiomeAND(biome1, biome2, TFBiomeProvider.getBiomeId(BiomeKeys.HIGHLANDS, registry), TFBiomeProvider.getBiomeId(BiomeKeys.FINAL_PLATEAU, registry)));
	}

	private boolean testEitherBiomeAND(int test1, int test2, int predicate1, int predicate2) {
		return (test1 == predicate1 && test2 == predicate2) || (test2 == predicate1 && test1 == predicate2);
	}

	private boolean testEitherBiomeOR(int test1, int test2, int predicate1, int predicate2) {
		return (test1 == predicate1 || test2 == predicate2) || (test2 == predicate1 || test1 == predicate2);
	}
}
