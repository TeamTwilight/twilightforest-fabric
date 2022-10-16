package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.init.TFEntities;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.feature.BlockSpikeFeature;

public class HollowHillComponent extends TFStructureComponentOld {
	private final static int[] stalactitesForSizes = {0, 128, 256, 512};
	private final static int[] spawnersForSizes = {0, 1, 4, 9};
	private final static int[] chestsForSizes = {0, 2, 6, 12};

	private final int hillSize;
	final int radius;
	final int hdiam;

	public HollowHillComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFHill.get(), nbt);
	}

	public HollowHillComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		hillSize = nbt.getInt("hillSize");
		this.radius = ((hillSize * 2 + 1) * 8) - 6;
		this.hdiam = (hillSize * 2 + 1) * 16;
	}

	public HollowHillComponent(StructurePieceType piece, TFLandmark feature, int i, int size, int x, int y, int z) {
		super(piece, feature, i, x, y, z);

		this.setOrientation(Direction.SOUTH);

		// get the size of this hill?
		this.hillSize = size;
		this.radius = ((hillSize * 2 + 1) * 8) - 6;
		this.hdiam = (hillSize * 2 + 1) * 16;

		// can we determine the size here?
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -radius, -(3 + hillSize), -radius, radius * 2, radius / (hillSize == 1 ? 2 : hillSize), radius * 2, Direction.SOUTH);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("hillSize", hillSize);
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int stalactiteCount = stalactitesForSizes[this.hillSize]; // number of stalactites mga = {0, 3, 9, 18}

		// fill in features

		// ore or glowing stalactites! (smaller, less plentiful)
		for (int i = 0; i < stalactiteCount; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, this.radius);
			this.generateOreStalactite(world, dest.move(0, 1, 0), sbb);
		}
		// stone stalactites!
		for (int i = 0; i < stalactiteCount; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, this.radius);
			this.generateBlockSpike(world, BlockSpikeFeature.STONE_STALACTITE, dest.getX(), dest.getY(), dest.getZ(), sbb, true);
			//this.setBlockStateRotated(world, Blocks.BEACON.defaultBlockState(), dest.getX(), dest.getY(), dest.getZ(), Rotation.NONE, sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < stalactiteCount; i++) {
			BlockPos.MutableBlockPos dest = this.randomFloorCoordinates(rand, this.radius);
			this.generateBlockSpike(world, BlockSpikeFeature.STONE_STALACTITE, dest.getX(), dest.getY(), dest.getZ(), sbb, false);
			//this.setBlockStateRotated(world, Blocks.SCAFFOLDING.defaultBlockState(), dest.getX(), dest.getY(), dest.getZ(), Rotation.NONE, sbb);
		}

		// Place these important blocks last so they don't get overwritten by generation

		// monster generators!
		for (int i = 0; i < spawnersForSizes[this.hillSize]; i++) {
			BlockPos.MutableBlockPos dest = this.randomFloorCoordinates(rand, this.radius);
			EntityType<?> mobID = this.getMobID(rand);

			this.setSpawner(world, dest.move(0, 1, 0), sbb, mobID);
		}
		// treasure chests!!
		for (int i = 0; i < chestsForSizes[this.hillSize]; i++) {
			BlockPos.MutableBlockPos dest = this.randomFloorCoordinates(rand, this.radius);
			this.generateTreasureChest(world, dest.move(0, 1, 0), sbb);
		}

		//DEBUG
//		this.setBlockStateRotated(world, Blocks.GLOWSTONE.defaultBlockState(), this.radius, 3, this.radius, Rotation.NONE, sbb);
//		this.setBlockStateRotated(world, Blocks.SEA_LANTERN.defaultBlockState(), this.boundingBox.getXSpan()/2, 4, this.boundingBox.getZSpan()/2, Rotation.NONE, sbb);
//
//		for (int i = 0; i < 30; i++) {
//			this.setBlockStateRotated(world, Blocks.WHITE_GLAZED_TERRACOTTA.defaultBlockState(), 0, i, 0, Rotation.NONE, sbb);
//			this.setBlockStateRotated(world, Blocks.ORANGE_GLAZED_TERRACOTTA.defaultBlockState(), this.boundingBox.maxX() - this.boundingBox.minX(), i, 0, Rotation.NONE, sbb);
//			this.setBlockStateRotated(world, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.defaultBlockState(), 0, i, this.boundingBox.maxZ() - this.boundingBox.minZ(), Rotation.NONE, sbb);
//			this.setBlockStateRotated(world, Blocks.MAGENTA_GLAZED_TERRACOTTA.defaultBlockState(), this.boundingBox.maxX() - this.boundingBox.minX(), i, this.boundingBox.maxZ() - this.boundingBox.minZ(), Rotation.NONE, sbb);
//		}
//
//		for (int i = 1; i < this.boundingBox.maxX() - this.boundingBox.minX(); i++) {
//			this.setBlockStateRotated(world, Blocks.GLOWSTONE.defaultBlockState(), i, this.boundingBox.maxY(), 0, Rotation.NONE, sbb);
//			this.setBlockStateRotated(world, Blocks.GLOWSTONE.defaultBlockState(), i, this.boundingBox.maxY(), this.boundingBox.maxZ() - this.boundingBox.minZ(), Rotation.NONE, sbb);
//		}
//
//		for (int i = 1; i < this.boundingBox.maxZ() - this.boundingBox.minZ(); i++) {
//			this.setBlockStateRotated(world, Blocks.GLOWSTONE.defaultBlockState(), 0, this.boundingBox.maxY(), i, Rotation.NONE, sbb);
//			this.setBlockStateRotated(world, Blocks.GLOWSTONE.defaultBlockState(), this.boundingBox.maxX() - this.boundingBox.minX(), this.boundingBox.maxY(), i, Rotation.NONE, sbb);
//		}
	}

	/**
	 * Make an RNG and attempt to use it to place a treasure chest
	 */

	protected void generateTreasureChest(WorldGenLevel world, Vec3i pos, BoundingBox sbb) {
		generateTreasureChest(world, pos.getX(), pos.getY(), pos.getZ(), sbb);
	}

	protected void generateTreasureChest(WorldGenLevel world, int x, int y, int z, BoundingBox sbb) {
		// generate an RNG for this chest
		RandomSource chestRNG = RandomSource.create(world.getSeed() + (long) x * z);

		// try placing it
		placeTreasureAtCurrentPosition(world, x, y, z, this.hillSize == 3 ? TFLootTables.LARGE_HOLLOW_HILL : (this.hillSize == 2 ? TFLootTables.MEDIUM_HOLLOW_HILL : TFLootTables.SMALL_HOLLOW_HILL), sbb);

		// make something for it to stand on, if necessary
		placeBlock(world, Blocks.COBBLESTONE.defaultBlockState(), x, y - 1, z, sbb);
	}

	protected void generateOreStalactite(WorldGenLevel world, Vec3i pos, BoundingBox sbb) {
		this.generateOreStalactite(world, pos.getX(), pos.getY(), pos.getZ(), sbb);
	}

	/**
	 * Generate a random ore stalactite
	 */
	protected void generateOreStalactite(WorldGenLevel world, int x, int y, int z, BoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isInside(pos) && world.getBlockState(pos).getBlock() != Blocks.SPAWNER) {
			// generate an RNG for this stalactite
			RandomSource stalRNG = RandomSource.create(world.getSeed() + (long) dx * dz);

			// make the actual stalactite
			Stalactite stalag = BlockSpikeFeature.makeRandomOreStalactite(stalRNG, this.hillSize);
			BlockSpikeFeature.startSpike(world, pos, stalag, stalRNG, true);
		}
	}

	protected void generateBlockSpike(WorldGenLevel world, Stalactite config, int x, int y, int z, BoundingBox sbb, boolean hanging) {
		// are the coordinates in our bounding box?
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isInside(pos) && world.getBlockState(pos).getBlock() != Blocks.SPAWNER) {
			// generate an RNG for this stalactite
			RandomSource stalRNG = RandomSource.create(world.getSeed() + (long) dx * dz);

			// make the actual stalactite
			BlockSpikeFeature.startSpike(world, pos, config, stalRNG, hanging);
		}
	}

	/**
	 * @param cx
	 * @param cz
	 * @return true if the coordinates would be inside the hill on the "floor" of the hill
	 */
	boolean isInHill(int cx, int cz) {
		int dx = radius - cx;
		int dz = radius - cz;

		return Mth.sqrt(dx * dx + dz * dz) < radius;
	}

	@Deprecated
		// Use randomPolarCoordinates
	int[] randomCoordinatesInHill2D(RandomSource rand) {
		return this.randomCoordinatesInHill2D(rand, radius);
	}

	/**
	 * @return a two element array containing some coordinates in the hill
	 */
	@Deprecated
	// Use randomPolarCoordinates
	int[] randomCoordinatesInHill2D(RandomSource rand, int maximumRadius) {
		Vec3i pos = this.randomFloorCoordinates(rand, maximumRadius);

		return new int[]{pos.getX(), pos.getZ()};
	}

	BlockPos.MutableBlockPos randomFloorCoordinates(RandomSource rand, float maximumRadius) {
		float degree = rand.nextFloat() * Mth.TWO_PI;
		// The full radius isn't actually hollow. Not feeling like doing the math to find the intersections of the curves involved
		float radius = rand.nextFloat() * 0.9f * maximumRadius;
		// Nonetheless the floor-carving curve is one-third the top-level terrain curve
		float dist = Mth.sqrt(radius * radius);
		float height = (this.hillSize * 2) - Mth.cos(dist / this.hdiam * Mth.PI) * (this.hdiam / 20f) + 1;

		return new BlockPos.MutableBlockPos(maximumRadius - Mth.cos(degree) * radius, height, maximumRadius - Mth.sin(degree) * radius);
	}

	BlockPos.MutableBlockPos randomCeilingCoordinates(RandomSource rand, float maximumRadius) {
		float degree = rand.nextFloat() * Mth.TWO_PI;
		// The full radius isn't actually hollow. Not feeling like doing the math to find the intersections of the curves involved
		float radius = rand.nextFloat() * 0.9f * maximumRadius;
		// Nonetheless the floor-carving curve is one-third the top-level terrain curve
		float dist = Mth.sqrt(radius * radius);
		float height = Mth.cos(dist / this.hdiam * Mth.PI) * (this.hdiam / 4f);

		return new BlockPos.MutableBlockPos(maximumRadius - Mth.cos(degree) * radius, height, maximumRadius - Mth.sin(degree) * radius);
	}

	/**
	 * Gets the id of a mob appropriate to the current hill size.
	 */
	protected EntityType<?> getMobID(RandomSource rand) {
		return getMobID(rand, this.hillSize);
	}

	/**
	 * Gets the id of a mob appropriate to the specified hill size.
	 *
	 * @param level
	 * @return
	 */
	protected EntityType<?> getMobID(RandomSource rand, int level) {
		if (level == 1) {
			return getLevel1Mob(rand);
		}
		if (level == 2) {
			return getLevel2Mob(rand);
		}
		if (level == 3) {
			return getLevel3Mob(rand);
		}

		return EntityType.SPIDER;
	}

	/**
	 * Returns a mob string appropriate for a level 1 hill
	 */
	public EntityType<?> getLevel1Mob(RandomSource rand) {
		return switch (rand.nextInt(10)) {
			case 3, 4, 5 -> EntityType.SPIDER;
			case 6, 7 -> EntityType.ZOMBIE;
			case 8 -> EntityType.SILVERFISH;
			case 9 -> TFEntities.REDCAP.get();
			default -> TFEntities.SWARM_SPIDER.get();
		};
	}

	/**
	 * Returns a mob string appropriate for a level 2 hill
	 */
	public EntityType<?> getLevel2Mob(RandomSource rand) {
		return switch (rand.nextInt(10)) {
			case 3, 4, 5 -> EntityType.ZOMBIE;
			case 6, 7 -> EntityType.SKELETON;
			case 8 -> TFEntities.SWARM_SPIDER.get();
			case 9 -> EntityType.CAVE_SPIDER;
			default -> TFEntities.REDCAP.get();
		};
	}

	/**
	 * Returns a mob string appropriate for a level 3 hill.  The level 3 also has 2 mid-air wraith spawners.
	 */
	public EntityType<?> getLevel3Mob(RandomSource rand) {
		return switch (rand.nextInt(11)) {
			case 0 -> TFEntities.SLIME_BEETLE.get();
			case 1 -> TFEntities.FIRE_BEETLE.get();
			case 2 -> TFEntities.PINCH_BEETLE.get();
			case 3, 4, 5 -> EntityType.SKELETON;
			case 6, 7, 8 -> EntityType.CAVE_SPIDER;
			case 9 -> EntityType.CREEPER;
			default -> TFEntities.WRAITH.get();
		};
	}
}
