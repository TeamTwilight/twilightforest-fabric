package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;


public class MazeDeadEndTorchesComponent extends MazeDeadEndComponent {

	public MazeDeadEndTorchesComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMDET.get(), nbt);
	}

	public MazeDeadEndTorchesComponent(int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFMMDET.get(), i, x, y, z, rotation);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// normal doorway
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// torches!
		this.generateBox(world, sbb, 2, 1, 4, 3, 4, 4, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH), AIR, false);
		this.generateBox(world, sbb, 1, 1, 1, 1, 4, 4, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST), AIR, false);
		this.generateBox(world, sbb, 4, 1, 1, 4, 4, 4, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), AIR, false);
	}
}
