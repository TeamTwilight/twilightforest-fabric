package twilightforest.world.components.structures;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class YetiCaveComponent extends HollowHillComponent {

	public YetiCaveComponent(ServerLevel level, CompoundTag nbt) {
		super(TFFeature.TFYeti, nbt);
	}

	public YetiCaveComponent(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(TFFeature.TFYeti, feature, i, 2, x, y, z);
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



	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		int sn = 128;

		// fill in features

//		// ore or glowing stalactites! (smaller, less plentiful)
//		for (int i = 0; i < sn; i++)
//		{
//			int[] dest = getCoordsInHill2D(rand);
//			generateOreStalactite(world, dest[0], 1, dest[1], sbb);
//		}
		// stone stalactites!
		for (int i = 0; i < sn; i++) {
			int[] dest = randomCoordinatesInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// ice stalactites!
		for (int i = 0; i < sn; i++) {
			int[] dest = randomCoordinatesInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.ICE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// packed ice stalactites!
		for (int i = 0; i < sn; i++) {
			int[] dest = randomCoordinatesInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.PACKED_ICE, 0.9F, true, dest[0], 1, dest[1], sbb);
		}

		// spawn alpha yeti
		final BlockState yetiSpawner = TFBlocks.boss_spawner_alpha_yeti.defaultBlockState();
		setBlockStateRotated(world, yetiSpawner, radius, 1, radius, Rotation.NONE, sbb);

		return true;
	}
}
