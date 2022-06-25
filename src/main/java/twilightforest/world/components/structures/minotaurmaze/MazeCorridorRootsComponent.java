package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;


public class MazeCorridorRootsComponent extends MazeCorridorComponent {

	public MazeCorridorRootsComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMCR.get(), nbt);
	}

	public MazeCorridorRootsComponent(TFLandmark feature, int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFMMCR.get(), feature, i, x, y, z, rotation);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		for (int x = 1; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				int freq = x;
				if (rand.nextInt(freq + 2) > 0) {
					int length = rand.nextInt(6);

					//place dirt above ceiling
					this.placeBlock(world, Blocks.DIRT.defaultBlockState(), x, 6, z, sbb);

					// roots
					for (int y = 6 - length; y < 6; y++) {
						this.placeBlock(world, TFBlocks.ROOT_STRAND.get().defaultBlockState(), x, y, z, sbb);
					}

					// occasional gravel
					if (rand.nextInt(freq + 1) > 1) {
						this.placeBlock(world, Blocks.GRAVEL.defaultBlockState(), x, 1, z, sbb);

						if (rand.nextInt(freq + 1) > 1) {
							this.placeBlock(world, Blocks.GRAVEL.defaultBlockState(), x, 2, z, sbb);
						}
					}
				}
			}
		}
	}
}
