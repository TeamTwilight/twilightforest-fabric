package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class MazeMoundComponent extends TFStructureComponentOld {

	public MazeMoundComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMMound, nbt);
	}

	public static final int DIAMETER = 35;
	private int averageGroundLevel = Integer.MIN_VALUE;

	private MazeUpperEntranceComponent mazeAbove;

	public MazeMoundComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMMound, feature, i, new BoundingBox(x, y, z, x + DIAMETER, y + 12, z + DIAMETER));
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, Random random) {
		super.addChildren(structurecomponent, list, random);

		// add aboveground maze entrance building
		mazeAbove = new MazeUpperEntranceComponent(getFeatureType(), 3, random, boundingBox.minX() + 10, boundingBox.minY(), boundingBox.minZ() + 10);
		list.addPiece(mazeAbove);
		mazeAbove.addChildren(this, list, random);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		if (this.averageGroundLevel < generator.getMinY()) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, generator, sbb);

			if (this.averageGroundLevel < generator.getMinY()) {
				return true;
			}

			int offset = this.averageGroundLevel - this.boundingBox.maxY() + 8 - 1;

			this.boundingBox.move(0, offset, 0);

			if (this.mazeAbove != null) {
				mazeAbove.getBoundingBox().encapsulate(blockPos.atY(offset));
			}
		}

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

		return true;
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(WorldGenLevel world, ChunkGenerator generator, BoundingBox boundingBox) {
		int totalHeight = 0;
		int totalMeasures = 0;
		int yStart = Mth.clamp(generator.getSeaLevel(), this.boundingBox.minY(), this.boundingBox.maxY());

		for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
			for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
				BlockPos pos = new BlockPos(x, yStart, z);

				if (boundingBox.isInside(pos)) {
					final BlockPos topPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
					totalHeight += Math.max(topPos.getY(), generator.getSeaLevel());
					++totalMeasures;
				}
			}
		}

		if (totalMeasures == 0) {
			return Integer.MIN_VALUE;
		} else {
			return totalHeight / totalMeasures;
		}
	}
}
