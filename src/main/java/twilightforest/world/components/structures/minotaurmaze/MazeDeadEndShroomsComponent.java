package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;


public class MazeDeadEndShroomsComponent extends MazeDeadEndRootsComponent {

	public MazeDeadEndShroomsComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMDES.get(), nbt);
	}

	public MazeDeadEndShroomsComponent(TFLandmark feature, int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFMMDES.get(), feature, i, x, y, z, rotation);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// no door

		// mycelium & mushrooms going back
		for (int x = 1; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				if (rand.nextInt(z + 2) > 0) {
					this.placeBlock(world, Blocks.MYCELIUM.defaultBlockState(), x, 0, z, sbb);
				}
				if (rand.nextInt(z + 2) > 0) {
					this.placeBlock(world, rand.nextBoolean() ? Blocks.RED_MUSHROOM.defaultBlockState() : Blocks.BROWN_MUSHROOM.defaultBlockState(), x, 1, z, sbb);
				}
			}
		}

		// brackets?
		boolean mushFlag = rand.nextBoolean();
		BlockState mushType = (mushFlag ? Blocks.RED_MUSHROOM_BLOCK : Blocks.BROWN_MUSHROOM_BLOCK).defaultBlockState();
		BlockState fullStem = Blocks.MUSHROOM_STEM.defaultBlockState();
		BlockState stem = fullStem.setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false);
		int mushY = rand.nextInt(4) + 1;
		int mushZ = rand.nextInt(3) + 1;
		this.placeBlock(world, fullStem, 1, mushY - 1, mushZ, sbb);
		this.generateBox(world, sbb, 1, 1, mushZ, 1, mushY, mushZ, stem, AIR, false);
		this.generateBox(world, sbb, 1, mushY, mushZ - 1, 2, mushY, mushZ + 1, mushType, AIR, false);

		mushType = (mushFlag ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK).defaultBlockState();
		mushY = rand.nextInt(4) + 1;
		mushZ = rand.nextInt(3) + 1;
		this.generateBox(world, sbb, 4, 1, mushZ, 4, mushY, mushZ, stem, AIR, false);
		this.generateBox(world, sbb, 3, mushY, mushZ - 1, 4, mushY, mushZ + 1, mushType, AIR, false);

		mushType = (rand.nextBoolean() ? Blocks.RED_MUSHROOM_BLOCK : Blocks.BROWN_MUSHROOM_BLOCK).defaultBlockState();
		mushY = rand.nextInt(4) + 1;
		int mushX = rand.nextInt(3) + 2;
		this.generateBox(world, sbb, mushX, 1, 4, mushX, mushY, 4, stem, AIR, false);
		this.generateBox(world, sbb, mushX - 1, mushY, 3, mushX + 1, mushY, 4, mushType, AIR, false);
	}
}
