package twilightforest.world.components.chunkgenerators;

import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.util.IntPair;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

// TODO: doc out all the vanilla copying
// Actually, figure out how to get this back up again
//TODO: PORT (Maybe make a wrapper?)
public abstract class ChunkGeneratorTwilightBase extends NoiseBasedChunkGenerator {
	protected final long seed;
	protected final Supplier<NoiseGeneratorSettings> dimensionSettings;
	public final NoiseGeneratorSettings settings;

	public final ConcurrentHashMap<ChunkPos, TFFeature> featureCache = new ConcurrentHashMap<>();

	public ChunkGeneratorTwilightBase(BiomeSource provider, long seed, Supplier<NoiseGeneratorSettings> settings) {
		super(provider, seed, settings);
		this.seed = seed;
		this.dimensionSettings = settings;
		this.settings = this.dimensionSettings;
	}

	@Deprecated // Keep until Vanilla gets their stuff together
	@Override
	public int getSeaLevel() {
		return this.settings.seaLevel();
	}

	@Deprecated
	@Override
	public int getSpawnHeight(LevelHeightAccessor accessor) {
		return 0;
	}

	// TODO Is there a way we can make a beard instead of making hard terrain shapes?
	protected final void deformTerrainForFeature(WorldGenRegion primer, ChunkAccess chunk) {
		IntPair featureRelativePos = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(primer.getCenter().x, primer.getCenter().z, primer, featureRelativePos);

		//Optional<StructureStart<?>> structureStart = TFGenerationSettings.locateTFStructureInRange(primer.getLevel(), nearFeature, chunk.getPos().getWorldPosition(), nearFeature.size + 1);

		if (!nearFeature.requiresTerraforming) {
			return;
		}

		final int relativeFeatureX = featureRelativePos.x;
		final int relativeFeatureZ = featureRelativePos.z;

		switch (nearFeature) {
			case SMALL_HILL, MEDIUM_HILL, LARGE_HILL, HYDRA_LAIR -> {
				int hdiam = (nearFeature.size * 2 + 1) * 16;

				for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
					for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
						int featureDX = xInChunk - relativeFeatureX;
						int featureDZ = zInChunk - relativeFeatureZ;

						float dist = (int) Mth.sqrt(featureDX * featureDX + featureDZ * featureDZ);
						float hheight = (int) (Mth.cos(dist / hdiam * Mth.PI) * (hdiam / 3F));
						this.raiseHills(primer, chunk, nearFeature, hdiam, xInChunk, zInChunk, featureDX, featureDZ, hheight);
					}
				}
			}
			case HEDGE_MAZE, NAGA_COURTYARD, QUEST_GROVE -> {
				for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
					for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
						int featureDX = xInChunk - relativeFeatureX;
						int featureDZ = zInChunk - relativeFeatureZ;

						this.flattenTerrainForFeature(primer, nearFeature, xInChunk, zInChunk, featureDX, featureDZ);
					}
				}
			}
			case YETI_CAVE -> {
				for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
					for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
						int featureDX = xInChunk - relativeFeatureX;
						int featureDZ = zInChunk - relativeFeatureZ;

						this.deformTerrainForYetiLair(primer, nearFeature, xInChunk, zInChunk, featureDX, featureDZ);
					}
				}
			}
			case TROLL_CAVE -> {
				// troll cloud, more like
				this.deformTerrainForTrollCloud2(primer, chunk, nearFeature, relativeFeatureX, relativeFeatureZ);
			}
		}

		// done!
	}

	//TODO: Parameter "nearFeature" is unused. Remove?
	private void deformTerrainForTrollCloud2(WorldGenRegion primer, ChunkAccess chunkAccess, TFFeature nearFeature, int hx, int hz) {
		for (int bx = 0; bx < 4; bx++) {
			for (int bz = 0; bz < 4; bz++) {
				int dx = bx * 4 - hx - 2;
				int dz = bz * 4 - hz - 2;

				// generate several centers for other clouds
				int regionX = primer.getCenter().x + 8 >> 4;
				int regionZ = primer.getCenter().z + 8 >> 4;

				long seed = regionX * 3129871L ^ regionZ * 116129781L;
				seed = seed * seed * 42317861L + seed * 7L;

				int num0 = (int) (seed >> 12 & 3L);
				int num1 = (int) (seed >> 15 & 3L);
				int num2 = (int) (seed >> 18 & 3L);
				int num3 = (int) (seed >> 21 & 3L);
				int num4 = (int) (seed >> 9 & 3L);
				int num5 = (int) (seed >> 6 & 3L);
				int num6 = (int) (seed >> 3 & 3L);
				int num7 = (int) (seed >> 0 & 3L);

				int dx2 = dx + num0 * 5 - num1 * 4;
				int dz2 = dz + num2 * 4 - num3 * 5;
				int dx3 = dx + num4 * 5 - num5 * 4;
				int dz3 = dz + num6 * 4 - num7 * 5;

				// take the minimum distance to any center
				float dist0 = Mth.sqrt(dx * dx + dz * dz) / 4.0f;
				float dist2 = Mth.sqrt(dx2 * dx2 + dz2 * dz2) / 3.5f;
				float dist3 = Mth.sqrt(dx3 * dx3 + dz3 * dz3) / 4.5f;

				double dist = Math.min(dist0, Math.min(dist2, dist3));

				float pr = primer.getRandom().nextFloat();
				double cv = dist - 7F - pr * 3.0F;

				// randomize depth and height
				int y = 166;
				int depth = 4;

				if (pr < 0.1F) {
					y++;
				}
				if (pr > 0.6F) {
					depth++;
				}
				if (pr > 0.9F) {
					depth++;
				}

				// generate cloud
				for (int sx = 0; sx < 4; sx++) {
					for (int sz = 0; sz < 4; sz++) {
						int lx = bx * 4 + sx;
						int lz = bz * 4 + sz;

						BlockPos.MutableBlockPos movingPos = primer.getCenter().getWorldPosition().mutable().move(lx, 0, lz);

						final int dY = primer.getHeight(Heightmap.Types.WORLD_SURFACE_WG, movingPos.getX(), movingPos.getZ());
						final int oceanFloor = primer.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, movingPos.getX(), movingPos.getZ());

						if (dist < 7 || cv < 0.05F) {
							primer.setBlock(movingPos.setY(y), TFBlocks.wispy_cloud.defaultBlockState(), 3);
							for (int d = 1; d < depth; d++) {
								primer.setBlock(movingPos.setY(y - d), TFBlocks.fluffy_cloud.defaultBlockState(), 3);
							}
							primer.setBlock(movingPos.setY(y - depth), TFBlocks.wispy_cloud.defaultBlockState(), 3);
						} else if (dist < 8 || cv < 1F) {
							for (int d = 1; d < depth; d++) {
								primer.setBlock(movingPos.setY(y - d), TFBlocks.fluffy_cloud.defaultBlockState(), 3);
							}
						}

						// What are you gonna do, call the cops?
						forceHeightMapLevel(chunkAccess, Heightmap.Types.WORLD_SURFACE_WG, movingPos, dY);
						forceHeightMapLevel(chunkAccess, Heightmap.Types.WORLD_SURFACE, movingPos, dY);
						forceHeightMapLevel(chunkAccess, Heightmap.Types.OCEAN_FLOOR_WG, movingPos, oceanFloor);
						forceHeightMapLevel(chunkAccess, Heightmap.Types.OCEAN_FLOOR, movingPos, oceanFloor);
					}
				}
			}
		}
	}

	/**
	 * Raises up and hollows out the hollow hills.
	 */ // TODO Add some surface noise
	private void raiseHills(WorldGenRegion world, ChunkAccess chunk, TFFeature nearFeature, int hdiam, int xInChunk, int zInChunk, int featureDX, int featureDZ, float hillHeight) {
		BlockPos.MutableBlockPos movingPos = world.getCenter().getWorldPosition().offset(xInChunk, 0, zInChunk).mutable();

		// raise the hill
		int groundHeight = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, movingPos.getX(), movingPos.getZ());
		float noiseRaw = (float) (this.surfaceNoise.getSurfaceNoiseValue(movingPos.getX() / 64f, movingPos.getZ() / 64f, 1.0f, 256) * 32f);
		float totalHeightRaw = groundHeight * 0.5f + this.getSeaLevel() * 0.5f + hillHeight + noiseRaw;
		int totalHeight = (int) (((int) totalHeightRaw >> 1) * 0.375f + totalHeightRaw * 0.625f);

		for (int y = groundHeight; y <= totalHeight; y++) {
			world.setBlock(movingPos.setY(y), this.defaultBlock, 3);
		}

		// add the hollow part. Also turn water into stone below that
		int hollow = (int) hillHeight - 4 - nearFeature.size;

		// hydra lair has a piece missing
		if (nearFeature == TFFeature.HYDRA_LAIR) {
			int mx = featureDX + 16;
			int mz = featureDZ + 16;
			int mdist = (int) Mth.sqrt(mx * mx + mz * mz);
			int mheight = (int) (Mth.cos(mdist / (hdiam / 1.5f) * Mth.PI) * (hdiam / 1.5f));

			hollow = Math.max(mheight - 4, hollow);
		}

		if (hollow < 0) {
			hollow = 0;
		}

		// hollow out the hollow parts
		int hollowFloor = nearFeature == TFFeature.HYDRA_LAIR ? this.getSeaLevel() : this.getSeaLevel() - 5 - (hollow >> 3);

		for (int y = hollowFloor + 1; y < hollowFloor + hollow; y++) {
			world.setBlock(movingPos.setY(y), Blocks.AIR.defaultBlockState(), 3);
		}
	}

	private void flattenTerrainForFeature(WorldGenRegion primer, TFFeature nearFeature, int xInChunk, int zInChunk, int featureDX, int featureDZ) {
		float squishFactor = 0f;
		int featureHeight = this.getSeaLevel() + 1;
		final int FEATURE_BOUNDARY = (nearFeature.size * 2 + 1) * 8 - 8;

		if (featureDX <= -FEATURE_BOUNDARY) {
			squishFactor = (-featureDX - FEATURE_BOUNDARY) / 8.0f;
		} else if (featureDX >= FEATURE_BOUNDARY) {
			squishFactor = (featureDX - FEATURE_BOUNDARY) / 8.0f;
		}

		if (featureDZ <= -FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (-featureDZ - FEATURE_BOUNDARY) / 8.0f);
		} else if (featureDZ >= FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (featureDZ - FEATURE_BOUNDARY) / 8.0f);
		}

		BlockPos.MutableBlockPos movingPos = primer.getCenter().getWorldPosition().offset(xInChunk, 0, zInChunk).mutable();

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height

			featureHeight += (primer.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, movingPos.getX(), movingPos.getZ()) - featureHeight) * squishFactor;
		}

		// sets the ground level to the maze height
		for (int y = primer.getMinBuildHeight(); y < featureHeight; y++) {
			Block b = primer.getBlockState(movingPos.setY(y)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlock(movingPos.setY(y), this.defaultBlock, 3);
			}
		}
		for (int y = featureHeight; y <= primer.getMaxBuildHeight(); y++) {
			Block b = primer.getBlockState(movingPos.setY(y)).getBlock();
			if (b != Blocks.AIR && b != Blocks.WATER) {
				primer.setBlock(movingPos.setY(y), Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}

	private void deformTerrainForYetiLair(WorldGenRegion primer, TFFeature nearFeature, int xInChunk, int zInChunk, int featureDX, int featureDZ) {
		float squishFactor = 0f;
		int topHeight = this.getSeaLevel() + 24;
		int outerBoundary = (nearFeature.size * 2 + 1) * 8 - 8;

		// outer boundary
		if (featureDX <= -outerBoundary) {
			squishFactor = (-featureDX - outerBoundary) / 8.0f;
		} else if (featureDX >= outerBoundary) {
			squishFactor = (featureDX - outerBoundary) / 8.0f;
		}

		if (featureDZ <= -outerBoundary) {
			squishFactor = Math.max(squishFactor, (-featureDZ - outerBoundary) / 8.0f);
		} else if (featureDZ >= outerBoundary) {
			squishFactor = Math.max(squishFactor, (featureDZ - outerBoundary) / 8.0f);
		}

		// inner boundary
		int caveBoundary = nearFeature.size * 2 * 8 - 8;
		int hollowCeiling;

		int offset = Math.min(Math.abs(featureDX), Math.abs(featureDZ));
		hollowCeiling = this.getSeaLevel() + 40 - offset * 4;

		// center square cave
		if (featureDX >= -caveBoundary && featureDZ >= -caveBoundary && featureDX <= caveBoundary && featureDZ <= caveBoundary) {
			hollowCeiling = this.getSeaLevel() + 16;
		}

		// slope ceiling slightly
		hollowCeiling -= offset / 6;

		// max out ceiling 8 blocks from roof
		hollowCeiling = Math.min(hollowCeiling, this.getSeaLevel() + 16);

		// floor, also with slight slope
		int hollowFloor = this.getSeaLevel() - 4 + offset / 6;

		BlockPos.MutableBlockPos movingPos = primer.getCenter().getWorldPosition().offset(xInChunk, 0, zInChunk).mutable();

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height
			for (int y = primer.getMinBuildHeight(); y <= primer.getMaxBuildHeight(); y++) {
				if (!this.defaultBlock.equals(primer.getBlockState(movingPos.setY(y)))) {
					// we found the lowest chunk of earth
					topHeight += (y - topHeight) * squishFactor;
					hollowFloor += (y - hollowFloor) * squishFactor;
					break;
				}
			}
		}

		// carve the cave into the stone

		// add stone
		for (int y = primer.getMinBuildHeight(); y < topHeight; y++) {
			Block b = primer.getBlockState(movingPos.setY(y)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlock(movingPos.setY(y), this.defaultBlock, 3);
			}
		}

		// hollow out inside
		for (int y = hollowFloor + 1; y < hollowCeiling; ++y) {
			primer.setBlock(movingPos.setY(y), Blocks.AIR.defaultBlockState(), 3);
		}

		// ice floor
		if (hollowFloor < hollowCeiling && hollowFloor < this.getSeaLevel() + 3) {
			primer.setBlock(movingPos.setY(hollowFloor), Blocks.PACKED_ICE.defaultBlockState(), 3);
		}
	}

	@Override
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Biome biome, StructureFeatureManager structureManager, MobCategory classification, BlockPos pos) {
		List<MobSpawnSettings.SpawnerData> potentialStructureSpawns = TFStructureStart.gatherPotentialSpawns(structureManager, classification, pos);
		if (potentialStructureSpawns != null)
			return WeightedRandomList.create(potentialStructureSpawns);
		WeightedRandomList<MobSpawnSettings.SpawnerData> spawns = StructureSpawnManager.getStructureSpawns(structureManager, classification, pos);
		if (spawns != null)
			return spawns;
		return classification == MobCategory.MONSTER && pos.getY() >= TFGenerationSettings.SEALEVEL ? WeightedRandomList.create() : super.getMobsAt(biome, structureManager, classification, pos);
	}

	public TFFeature getFeatureCached(final ChunkPos chunk, final WorldGenLevel world) {
		return this.featureCache.computeIfAbsent(chunk, chunkPos -> TFFeature.generateFeature(chunkPos.x, chunkPos.z, world));
	}

	static void forceHeightMapLevel(ChunkAccess chunk, Heightmap.Types type, BlockPos pos, int dY) {
		chunk.getOrCreateHeightmapUnprimed(type).setHeight(pos.getX() & 15, pos.getZ() & 15, dY + 1);
	}
}
