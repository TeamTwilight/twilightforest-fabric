package twilightforest.world.components.structures;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class HydraLairComponent extends HollowHillComponent {

	public HydraLairComponent(ServerLevel level, CompoundTag nbt) {
		super(TFFeature.TFHydra, nbt);
	}

	public HydraLairComponent(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(TFFeature.TFHydra, feature, i, 2, x, y + 2, z);
	}

	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor accessor, Random random) {
		// NO-OP
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int stalacts = 64;
		int stalags = 8;

		// fill in features
		// ore or glowing stalactites! (smaller, less plentiful)
		for (int i = 0; i < stalacts; i++) {
			int[] dest = randomCoordinatesInHill2D(rand);
			generateOreStalactite(world, generator, manager, dest[0], 1, dest[1], sbb);
		}
		// stone stalactites!
		for (int i = 0; i < stalacts; i++) {
			int[] dest = randomCoordinatesInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < stalags; i++) {
			int[] dest = randomCoordinatesInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.STONE, 0.9F, false, dest[0], 1, dest[1], sbb);
		}

		// boss spawner seems important
		placeBlock(world, TFBlocks.boss_spawner_hydra.defaultBlockState(), 27, 3, 27, sbb);

		return true;
	}
}
