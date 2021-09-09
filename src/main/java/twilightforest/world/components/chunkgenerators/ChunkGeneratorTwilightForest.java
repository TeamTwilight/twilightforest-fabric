package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.server.level.WorldGenRegion;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.biomes.BiomeKeys;
import twilightforest.block.TFBlocks;
import twilightforest.util.IntPair;

import java.util.function.Supplier;

// TODO: doc out all the vanilla copying
public class ChunkGeneratorTwilightForest extends ChunkGeneratorTwilightBase {
	public static final Codec<ChunkGeneratorTwilightForest> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
			Codec.LONG.fieldOf("seed").stable().forGetter((obj) -> obj.seed),
			NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(ChunkGeneratorTwilightForest::getDimensionSettings)
	).apply(instance, instance.stable(ChunkGeneratorTwilightForest::new)));

	public ChunkGeneratorTwilightForest(BiomeSource provider, long seed, Supplier<NoiseGeneratorSettings> settings) {
		super(provider, seed, settings);
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long l) {
		return new ChunkGeneratorTwilightForest(this.biomeSource.withSeed(l), l, this.dimensionSettings);
	}

	private Supplier<NoiseGeneratorSettings> getDimensionSettings() {
		return this.dimensionSettings;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion world, ChunkAccess chunk) {
		this.deformTerrainForFeature(world, chunk);

		super.buildSurfaceAndBedrock(world, chunk);

		this.addDarkForestCanopy(world, chunk);
	}

	/**
	 * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
	 */
	// Currently this is too sophisicated to be made into a SurfaceBuilder, it looks like
	private void addDarkForestCanopy(WorldGenRegion primer, ChunkAccess chunk) {
		BlockPos blockpos = primer.getCenter().getWorldPosition();
		int[] thicks = new int[5 * 5];
		boolean biomeFound = false;

		for (int dZ = 0; dZ < 5; dZ++) {
			for (int dX = 0; dX < 5; dX++) {
				for (int bx = -1; bx <= 1; bx++) {
					for (int bz = -1; bz <= 1; bz++) {
						BlockPos p = blockpos.offset((dX + bx) << 2, 0, (dZ + bz) << 2);
						Biome biome = biomeSource.getNoiseBiome(p.getX() >> 2, 0, p.getZ() >> 2);
						if (BiomeKeys.DARK_FOREST.location().equals(biome.getRegistryName()) || BiomeKeys.DARK_FOREST_CENTER.location().equals(biome.getRegistryName())) {
							thicks[dX + dZ * 5]++;
							biomeFound = true;
						}
					}
				}
			}
		}

		if (!biomeFound) return;

		IntPair nearCenter = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(primer.getCenter().x, primer.getCenter().z, primer, nearCenter);

		double d = 0.03125D;
		//depthBuffer = noiseGen4.generateNoiseOctaves(depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

		for (int dZ = 0; dZ < 16; dZ++) {
			for (int dX = 0; dX < 16; dX++) {
				int qx = dX >> 2;
				int qz = dZ >> 2;

				float xweight = (dX % 4) * 0.25F + 0.125F;
				float zweight = (dZ % 4) * 0.25F + 0.125F;

				float thickness = thicks[qx + (qz) * 5] * (1F - xweight) * (1F - zweight)
						+ thicks[qx + 1 + (qz) * 5] * (xweight) * (1F - zweight)
						+ thicks[qx + (qz + 1) * 5] * (1F - xweight) * (zweight)
						+ thicks[qx + 1 + (qz + 1) * 5] * (xweight) * (zweight)
						- 4;

				// make sure we're not too close to the tower
				if (nearFeature == TFFeature.DARK_TOWER) {
					int hx = nearCenter.x;
					int hz = nearCenter.z;

					int rx = dX - hx;
					int rz = dZ - hz;
					int dist = (int) Mth.sqrt(rx * rx + rz * rz);

					if (dist < 24) {
						thickness -= (24 - dist);
					}
				}

				// TODO Clean up this math
				if (thickness > 1) {
					// We can use the Deltas here because the methods called will just
					final int dY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, dX, dZ);
					final int oceanFloor = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, dX, dZ);
					BlockPos pos = primer.getCenter().getWorldPosition().offset(dX, dY, dZ);

					// Skip any blocks over water
					if (chunk.getBlockState(pos).getMaterial().isLiquid())
						continue;

					// just use the same noise generator as the terrain uses for stones
					//int noise = Math.min(3, (int) (depthBuffer[dZ & 15 | (dX & 15) << 4] / 1.25f));
					int noise = Math.min(3, (int) (this.surfaceNoise.getSurfaceNoiseValue((blockpos.getX() + dX) * 0.0625D, (blockpos.getZ() + dZ) * 0.0625D, 0.0625D, dX * 0.0625D) * 15F / 1.25F));

					// manipulate top and bottom
					int treeBottom = pos.getY() + 12 - (int) (thickness * 0.5F);
					int treeTop = treeBottom + (int) (thickness * 1.5F);

					treeBottom -= noise;

					BlockState darkLeaves = TFBlocks.hardened_dark_leaves.get().defaultBlockState();

					for (int y = treeBottom; y < treeTop; y++) {
						primer.setBlock(pos.atY(y), darkLeaves, 3);
					}

					// What are you gonna do, call the cops?
					forceHeightMapLevel(chunk, Heightmap.Types.WORLD_SURFACE_WG, pos, dY);
					forceHeightMapLevel(chunk, Heightmap.Types.WORLD_SURFACE, pos, dY);
					forceHeightMapLevel(chunk, Heightmap.Types.OCEAN_FLOOR_WG, pos, oceanFloor);
					forceHeightMapLevel(chunk, Heightmap.Types.OCEAN_FLOOR, pos, oceanFloor);
				}
			}
		}
	}
}
