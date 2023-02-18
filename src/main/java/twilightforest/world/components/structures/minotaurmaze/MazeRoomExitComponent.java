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


public class MazeRoomExitComponent extends MazeRoomComponent {

	public MazeRoomExitComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMRE.get(), nbt);
	}

	public MazeRoomExitComponent(int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMRE.get(), i, rand, x, y, z);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// shaft down
		this.generateBox(world, sbb, 5, -5, 5, 10, 0, 10, TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS.defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 4, 5, 10, 4, 10, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateAirBox(world, sbb, 6, -5, 6, 9, 4, 9);
	}
}
