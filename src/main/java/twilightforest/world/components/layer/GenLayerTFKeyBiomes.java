package twilightforest.world.components.layer;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.vanillalegacy.area.Area;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer1;
import twilightforest.init.BiomeKeys;

import java.util.Random;

/**
 * Puts key biomes in the proper positions
 *
 * @author Ben
 */
public enum GenLayerTFKeyBiomes implements AreaTransformer1 {
	INSTANCE;

	private Registry<Biome> registry;
	private long seed;
	private static final Random RANDOM = new Random();

	GenLayerTFKeyBiomes() { }

	public GenLayerTFKeyBiomes setup(Registry<Biome> registry, long seed) {
		this.registry = registry;
		this.seed = seed;
		return this;
	}

	@Override
	public int getParentX(int x) {
		return x | 3;
	}

	@Override
	public int getParentY(int z) {
		return z | 3;
	}

	@Override
	public int applyPixel(BigContext<?> random, Area iArea, int x, int z) {
		RANDOM.setSeed(seed + (x & -4) * 25117L + (z & -4) * 151121L);
		int ox = RANDOM.nextInt(2) + 1;
		int oz = RANDOM.nextInt(2) + 1;
		RANDOM.setSeed(seed + (x / 8) * 25117L + (z / 8) * 151121L);
		int offset = RANDOM.nextInt(3);
		if ((x & 3) == ox && (z & 3) == oz) {
			// determine which of the 4
			if ((x & 4) == 0) {
				if ((z & 4) == 0) {
					return getKeyBiomeFor(offset);
				} else {
					return getKeyBiomeFor(offset + 1);
				}
			} else {
				if ((z & 4) == 0) {
					return getKeyBiomeFor(offset + 2);
				} else {
					return getKeyBiomeFor(offset + 3);
				}
			}

		} else {
			return iArea.get(x, z);
		}
	}

	/**
	 * Determine which map "region" the specified points are in.  Assign the 0-3 of the index to the key biomes based on that region.
	 */
	private int getKeyBiomeFor(int index) {
		// do we need to shuffle this better?
		// the current version just "rotates" the 4 key biomes
		return switch ((index) & 0b11) {
			case 1 -> TFBiomeProvider.getBiomeId(BiomeKeys.FIRE_SWAMP, registry);
			case 2 -> TFBiomeProvider.getBiomeId(BiomeKeys.DARK_FOREST_CENTER, registry);
			case 3 -> TFBiomeProvider.getBiomeId(BiomeKeys.FINAL_PLATEAU, registry);
			default -> TFBiomeProvider.getBiomeId(BiomeKeys.GLACIER, registry);
		};
	}
}
