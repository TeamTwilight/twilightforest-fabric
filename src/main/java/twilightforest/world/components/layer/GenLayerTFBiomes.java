package twilightforest.world.components.layer;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.vanillalegacy.context.Context;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer0;

import java.util.List;

/**
 * Applies the twilight forest biomes to the map
 *
 * @author Ben
 */
public enum GenLayerTFBiomes implements AreaTransformer0 {
	INSTANCE;
	private static final int RARE_BIOME_CHANCE = 15;

	private static final List<ResourceKey<Biome>> commonBiomes = ImmutableList.of(
			TFBiomes.FOREST,
			TFBiomes.DENSE_FOREST,
			TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH,
			TFBiomes.FIREFLY_FOREST
	);
	private static final List<ResourceKey<Biome>> rareBiomes = ImmutableList.of(
			TFBiomes.LAKE,
			TFBiomes.DENSE_MUSHROOM_FOREST,
			TFBiomes.ENCHANTED_FOREST,
			TFBiomes.CLEARING,
			TFBiomes.SPOOKY_FOREST
	);

	private HolderGetter<Biome> registry;

	public GenLayerTFBiomes setup(HolderGetter<Biome> registry) {
		this.registry = registry;
		return this;
	}

	GenLayerTFBiomes() {

	}

	@Override
	public int applyPixel(Context iNoiseRandom, int x, int y) {
		//return 0; //getRandomBiome(iNoiseRandom, commonBiomes));

		if (iNoiseRandom.nextRandom(RARE_BIOME_CHANCE) == 0) {
			// make specialBiomes biome
			return getRandomBiome(iNoiseRandom, rareBiomes);
		} else {
			// make common biome
			return getRandomBiome(iNoiseRandom, commonBiomes);
		}
	}

	private int getRandomBiome(Context random, List<ResourceKey<Biome>> biomes) {
		return TFBiomeProvider.getBiomeId(biomes.get(random.nextRandom(biomes.size())), registry);
	}
}
