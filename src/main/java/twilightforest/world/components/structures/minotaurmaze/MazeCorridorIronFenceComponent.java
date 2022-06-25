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
import twilightforest.init.TFBlocks;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;


public class MazeCorridorIronFenceComponent extends MazeCorridorComponent {

	public MazeCorridorIronFenceComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMCIF.get(), nbt);
	}

	public MazeCorridorIronFenceComponent(TFLandmark feature, int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFMMCIF.get(), feature, i, x, y, z, rotation);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		this.generateBox(world, sbb, 1, 4, 2, 4, 4, 3, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 1, 1, 2, 4, 3, 3, TFBlocks.CUT_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 2, 1, 2, 3, 3, 3, Blocks.IRON_BARS.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
	}
}
