package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;


public class MazeMoundComponent extends TFStructureComponentOld {

	public MazeMoundComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMMound.get(), nbt);
	}

	public static final int DIAMETER = 35;
	private final int averageGroundLevel = Integer.MIN_VALUE;

	private MazeUpperEntranceComponent mazeAbove;

	public MazeMoundComponent(TFLandmark feature, int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMMound.get(), feature, i, new BoundingBox(x, y, z, x + DIAMETER, y + 12, z + DIAMETER));
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(structurecomponent, list, random);

		// add aboveground maze entrance building
		mazeAbove = new MazeUpperEntranceComponent(getFeatureType(), 3, random, boundingBox.minX() + 10, boundingBox.minY(), boundingBox.minZ() + 10);
		list.addPiece(mazeAbove);
		mazeAbove.addChildren(this, list, random);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		/*if (this.averageGroundLevel < generator.getMinY()) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, generator, sbb);

			if (this.averageGroundLevel < generator.getMinY()) {
				return true;
			}

			int offset = this.averageGroundLevel - this.boundingBox.maxY() + 8 - 1;

			this.boundingBox.move(0, offset, 0);

			if (this.mazeAbove != null) {
				mazeAbove.getBoundingBox().encapsulate(blockPos.atY(offset));
			}
		}*/

		//this.fillWithBlocks(world, sbb, 0, 0, 0, 25, 8, 25, Blocks.DIRT, 0, false);

		for (int x = 0; x < DIAMETER; x++) {
			for (int z = 0; z < DIAMETER; z++) {
				int cx = x - DIAMETER / 2;
				int cz = z - DIAMETER / 2;

				int dist = (int) Mth.sqrt(cx * cx + cz * cz);
				int hheight = (int) (Mth.cos((float) dist / DIAMETER * Mth.PI) * (DIAMETER / 3));

				// leave a hole in the middle
				if (!(cx <= 2 && cx >= -1 && cz <= 2 && cz >= -1) && ((!(cx <= 2 && cx >= -1) && !(cz <= 2 && cz >= -1)) || hheight > 6)) {
					this.placeBlock(world, Blocks.GRASS_BLOCK.defaultBlockState(), x, hheight, z, sbb);

					// only fill to the bottom when we're not in the entrances
					if (!(cx <= 2 && cx >= -1) && !(cz <= 2 && cz >= -1)) {
						this.placeBlock(world, Blocks.DIRT.defaultBlockState(), x, hheight - 1, z, sbb);
					} else if (hheight > 6) {
						this.generateBox(world, sbb, x, 6, z, x, hheight - 1, z, Blocks.DIRT.defaultBlockState(), AIR, false);
					}
				}
			}
		}
	}
}
