package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.feature.BlockSpikeFeature;

public class YetiCaveComponent extends HollowHillComponent {
	private static final Stalactite BLUE_ICE_SPIKE = new Stalactite(Blocks.BLUE_ICE, 1.0F, 8, 1);
	private static final Stalactite PACKED_ICE_SPIKE = new Stalactite(Blocks.PACKED_ICE, 0.5F, 9, 1);
	private static final Stalactite ICE_SPIKE = new Stalactite(Blocks.ICE, 0.6F, 10, 1);

	public YetiCaveComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFYeti.get(), nbt);
	}

	public YetiCaveComponent(TFLandmark feature, int i, int x, int y, int z) {
		super(TFStructurePieceTypes.TFYeti.get(), feature, i, 2, x, y, z);
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int sn = 64;

		// fill in features

//		// ore or glowing stalactites! (smaller, less plentiful)
//		for (int i = 0; i < sn; i++)
//		{
//			int[] dest = getCoordsInHill2D(rand);
//			generateOreStalactite(world, dest[0], 1, dest[1], sbb);
//		}
		// blue ice stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, BLUE_ICE_SPIKE, dest.getX(), dest.getY(), dest.getZ(), sbb, true);
		}
		// packed ice stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, PACKED_ICE_SPIKE, dest.getX(), dest.getY(), dest.getZ(), sbb, true);
		}
		// ice stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, ICE_SPIKE, dest.getX(), dest.getY(), dest.getZ(), sbb, true);
		}
		// stone stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, BlockSpikeFeature.STONE_STALACTITE, dest.getX(), dest.getY(), dest.getZ(), sbb, true);
		}

		// spawn alpha yeti
		final BlockState yetiSpawner = TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().defaultBlockState();
		this.setBlockStateRotated(world, yetiSpawner, this.radius, 1, this.radius, Rotation.NONE, sbb);
	}

	/**
	 * @param cx
	 * @param cz
	 * @return true if the coordinates would be inside the hill on the "floor" of the hill
	 */
	@Override
	boolean isInHill(int cx, int cz) {
		// yeti cave is square
		return cx < this.radius * 2 && cx > 0 && cz < this.radius * 2 && cz > 0;
	}

	@Override
	BlockPos.MutableBlockPos randomCeilingCoordinates(RandomSource rand, float maximumRadius) {
		int rad = (int) maximumRadius;
		int x = rand.nextInt(rad * 2) - rad;
		int z = rand.nextInt(rad * 2) - rad;

		int dist = Math.min(Math.abs(x), Math.abs(z));

		return new BlockPos.MutableBlockPos(this.radius + x, 17 - dist / 6, this.radius + z);
	}
}