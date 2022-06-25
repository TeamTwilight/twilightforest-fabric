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
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;


public class MazeRoomCollapseComponent extends MazeRoomComponent {

	public MazeRoomCollapseComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMRC.get(), nbt);
	}

	public MazeRoomCollapseComponent(TFLandmark feature, int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMRC.get(), feature, i, rand, x, y, z);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		//
		for (int x = 1; x < 14; x++) {
			for (int z = 1; z < 14; z++) {
				// calculate distance from middle
				int dist = (int) Math.round(7 / Math.sqrt((7.5 - x) * (7.5 - x) + (7.5 - z) * (7.5 - z)));
				int gravel = rand.nextInt(dist);
				int root = rand.nextInt(dist);

				if (gravel > 0) {
					gravel++; // get it out of the floor
					this.generateBox(world, sbb, x, 1, z, x, gravel, z, Blocks.GRAVEL.defaultBlockState(), AIR, false);
					this.generateAirBox(world, sbb, x, gravel, z, x, gravel + 5, z);
				} else if (root > 0) {
					this.generateBox(world, sbb, x, 5, z, x, 5 + root, z, Blocks.DIRT.defaultBlockState(), AIR, true);
					this.generateBox(world, sbb, x, 5 - rand.nextInt(5), z, x, 5, z, TFBlocks.ROOT_STRAND.get().defaultBlockState(), AIR, false);
				} else if (rand.nextInt(dist + 1) > 0) {
					// remove ceiling
					this.generateAirBox(world, sbb, x, 5, z, x, 5, z);
				}
			}
		}
	}
}
