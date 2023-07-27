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
import org.jetbrains.annotations.NotNull;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.init.TFEntities;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.RectangleLatticeIterator;
import twilightforest.world.components.feature.BlockSpikeFeature;

public class HollowHillComponent extends TFStructureComponentOld {
	// Triangle-grid settings for placing features inside (Stalactites, Stalagmites, Chests, & Spawners)
	private static final float SPACING = 3.75f;
	private static final float X_OFFSET = Mth.cos(Mth.PI/6f) * SPACING;
	private static final float Z_OFFSET = Mth.sin(Mth.PI/6f) * SPACING;
	private static final float X_SPACING = X_OFFSET * 2f;
	private static final float Z_SPACING = SPACING;

	private static final float CHEST_SPAWN_CHANCE = 0.025f;
	private static final float SPAWNER_SPAWN_CHANCE = 0.025f;
	private static final float SPECIAL_SPAWN_CHANCE = CHEST_SPAWN_CHANCE + SPAWNER_SPAWN_CHANCE;
	private static final float ORE_STALACTITE_CHANCE = 0.85f;

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

	public HollowHillComponent(StructurePieceType piece, int i, int size, int x, int y, int z) {
		super(piece, i, x, y, z);

		this.setOrientation(Direction.SOUTH);

		// get the size of this hill?
		this.hillSize = size;
		this.radius = ((hillSize * 2 + 1) * 8) - 6;
		this.hdiam = (hillSize * 2 + 1) * 16;

		// can we determine the size here?
		this.boundingBox = TFLandmark.getComponentToAddBoundingBox(x, y, z, -radius, -(3 + hillSize), -radius, radius * 2, radius / (hillSize == 1 ? 2 : hillSize), radius * 2, Direction.SOUTH, true);
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
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox writeableBounds, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockPos locatorPos = this.getLocatorPosition();
		float shortenedRadiusSq = this.radius * this.radius * 0.85f;

		// Use two rectangle-grid lattices to simulate a triangular-grid lattice, simulating an optimal hexagonal-packing pattern for filling this structure
		// with stalactites, stalagmites, chests, and spawners

		// RectangleLatticeIterator enables for approximately-even spacing across chunks
		for (BlockPos.MutableBlockPos latticePos : RectangleLatticeIterator.boundedGrid(writeableBounds, 0, X_SPACING, Z_SPACING, 0, 0)) {
			int distSq = getDistSqFromCenter(locatorPos, latticePos);

			if (distSq > shortenedRadiusSq) continue;

			this.setFeatures(world, rand, writeableBounds, latticePos, distSq);
		}

		for (BlockPos.MutableBlockPos latticePos : RectangleLatticeIterator.boundedGrid(writeableBounds, 0, X_SPACING, Z_SPACING, X_OFFSET, Z_OFFSET)) {
			int distSq = getDistSqFromCenter(locatorPos, latticePos);

			if (distSq > shortenedRadiusSq) continue;

			this.setFeatures(world, rand, writeableBounds, latticePos, distSq);
		}
	}

	private void setFeatures(WorldGenLevel world, RandomSource rand, BoundingBox writeableBounds, BlockPos.MutableBlockPos pos, int distSq) {
		rand.setSeed(rand.nextLong() ^ pos.asLong());
		this.placeCeilingFeature(world, rand, pos, distSq);
		this.placeFloorFeature(world, rand, writeableBounds, pos, distSq);
	}

	private void placeFloorFeature(WorldGenLevel world, RandomSource rand, BoundingBox writeableBounds, BlockPos.MutableBlockPos pos, int distSq) {

		int y = this.getWorldY(Mth.floor(this.getFloorHeight(Mth.sqrt(distSq)) + 0.25f));

		float floatChance = rand.nextFloat();

		if (floatChance < SPECIAL_SPAWN_CHANCE) {
			// Random direction for offset from lattice-grid, this isn't applied to stalagmites to reduce chances of burying these
			float angle = rand.nextFloat() * Mth.TWO_PI;
			int x = Math.round(Mth.cos(angle) * Mth.SQRT_OF_TWO) + pos.getX();
			int z = Math.round(Mth.sin(angle) * Mth.SQRT_OF_TWO) + pos.getZ();
			pos.set(x, y, z);

			if (floatChance < SPAWNER_SPAWN_CHANCE) {
				setSpawnerInWorld(world, writeableBounds, this.getMobID(rand), v -> {}, pos.above());
			} else {
				this.placeTreasureAtWorldPosition(world, this.getTreasureType(), false, writeableBounds, pos.above());
			}

			world.setBlock(pos.below(), Blocks.COBBLESTONE.defaultBlockState(), 50);
			world.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 50);
		} else {
			pos.setY(y);
			BlockSpikeFeature.startSpike(world, pos, BlockSpikeFeature.STONE_STALACTITE, rand, false);
		}
	}

	private void placeCeilingFeature(WorldGenLevel world, RandomSource rand, BlockPos.MutableBlockPos pos, int distSq) {
		BlockPos ceiling = pos.atY(this.getWorldY(Mth.ceil(this.getCeilingHeight(Mth.sqrt(distSq)))));

		Stalactite stalag = rand.nextFloat() < ORE_STALACTITE_CHANCE ? BlockSpikeFeature.makeRandomOreStalactite(rand, this.hillSize) : BlockSpikeFeature.STONE_STALACTITE;
		BlockSpikeFeature.startSpike(world, ceiling, stalag, rand, true);
	}

	@NotNull
	private TFLootTables getTreasureType() {
		return this.hillSize == 3 ? TFLootTables.LARGE_HOLLOW_HILL : (this.hillSize == 2 ? TFLootTables.MEDIUM_HOLLOW_HILL : TFLootTables.SMALL_HOLLOW_HILL);
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
		float height = this.getFloorHeight(dist);

		return new BlockPos.MutableBlockPos(maximumRadius - Mth.cos(degree) * radius, height, maximumRadius - Mth.sin(degree) * radius);
	}

	private float getFloorHeight(float dist) {
		return (this.hillSize * 2) - Mth.cos(dist / this.hdiam * Mth.PI) * (this.hdiam / 20f) + 1;
	}

	BlockPos.MutableBlockPos randomCeilingCoordinates(RandomSource rand, float maximumRadius) {
		float degree = rand.nextFloat() * Mth.TWO_PI;
		// The full radius isn't actually hollow. Not feeling like doing the math to find the intersections of the curves involved
		float radius = rand.nextFloat() * 0.9f * maximumRadius;
		// Nonetheless the floor-carving curve is one-third the top-level terrain curve
		float dist = Mth.sqrt(radius * radius);
		float height = getCeilingHeight(dist);

		return new BlockPos.MutableBlockPos(maximumRadius - Mth.cos(degree) * radius, height, maximumRadius - Mth.sin(degree) * radius);
	}

	private float getCeilingHeight(float dist) {
		return Mth.cos(dist / this.hdiam * Mth.PI) * (this.hdiam / 4f);
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

	public static int getDistSqFromCenter(BlockPos center, BlockPos to) {
		int x = to.getX() - center.getX();
		int z = to.getZ() - center.getZ();

		return x * x + z * z;
	}
}
