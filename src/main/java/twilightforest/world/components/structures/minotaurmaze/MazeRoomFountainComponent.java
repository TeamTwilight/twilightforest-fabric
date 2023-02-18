package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;


public class MazeRoomFountainComponent extends MazeRoomComponent {

	public MazeRoomFountainComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMRF.get(), nbt);
	}

	public MazeRoomFountainComponent(int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMRF.get(), i, rand, x, y, z);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		this.generateBox(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 6, 1, 6, 9, 1, 9, Blocks.WATER.defaultBlockState(), AIR, false);
	}
}
