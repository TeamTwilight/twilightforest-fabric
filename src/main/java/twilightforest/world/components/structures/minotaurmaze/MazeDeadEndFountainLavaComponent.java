package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;


public class MazeDeadEndFountainLavaComponent extends MazeDeadEndFountainComponent {

	public MazeDeadEndFountainLavaComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMDEFL.get(), nbt);
	}

	public MazeDeadEndFountainLavaComponent(TFLandmark feature, int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFMMDEFL.get(), feature, i, x, y, z, rotation);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// normal fountain
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// remove water
		this.placeBlock(world, AIR, 2, 3, 4, sbb);
		this.placeBlock(world, AIR, 3, 3, 4, sbb);

		// lava instead of water
		this.placeBlock(world, Blocks.LAVA.defaultBlockState(), 2, 3, 4, sbb);
		this.placeBlock(world, Blocks.LAVA.defaultBlockState(), 3, 3, 4, sbb);
	}
}
