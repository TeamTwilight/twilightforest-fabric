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
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;


public class MazeDeadEndPaintingComponent extends MazeDeadEndComponent {

	public MazeDeadEndPaintingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMDEP.get(), nbt);
	}

	public MazeDeadEndPaintingComponent(TFLandmark feature, int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFMMDEP.get(), feature, i, x, y, z, rotation);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// normal doorway
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// torches
		this.placeBlock(world, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST), 1, 3, 3, sbb);
		this.placeBlock(world, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), 4, 3, 3, sbb);

//		// painting
//		EntityPainting painting = new EntityPainting(world, pCoords.posX, pCoords.posY, pCoords.posZ, this.get); 
//		painting.art = getPaintingOfSize(rand, minSize);
//		painting.setDirection(direction);
//		
//		world.addEntity(painting);
	}
}
