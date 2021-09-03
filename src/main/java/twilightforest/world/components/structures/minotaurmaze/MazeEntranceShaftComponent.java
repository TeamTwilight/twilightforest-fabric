package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class MazeEntranceShaftComponent extends TFStructureComponentOld {

	public MazeEntranceShaftComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMES, nbt);
	}

	private int averageGroundLevel = -1;

	public MazeEntranceShaftComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMES, feature, i, new BoundingBox(x, y, z, x + 6 - 1, y, z + 6 - 1)); // Flat, expand later
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, Random random) {
		// NO-OP
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockPos.MutableBlockPos pos = chunkPosIn.getWorldPosition().mutable().setX(this.boundingBox.minX()).setZ(this.boundingBox.minZ());

		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, generator, sbb);

			if (this.averageGroundLevel < 0) {
				return true;
			}

			pos.setY(this.averageGroundLevel);

			this.boundingBox.encapsulate(pos);
		}

		this.boundingBox.encapsulate(pos.setY(generator.getSeaLevel() - 11));

		this.generateBox(world, sbb, 0, 0, 0, 5, this.boundingBox.getYSpan(), 5, TFBlocks.maze_stone_brick.defaultBlockState(), AIR, true);
		this.generateAirBox(world, sbb, 1, 0, 1, 4, this.boundingBox.getYSpan(), 4);

		return true;
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(WorldGenLevel world, ChunkGenerator generator, BoundingBox boundingBox) {
		int yTotal = 0;
		int count = 0;

		for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
			for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
				BlockPos pos = new BlockPos(x, 64, z);
				if (boundingBox.isInside(pos)) {
					final BlockPos topBlock = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
					yTotal += Math.max(topBlock.getY(), generator.getSpawnHeight(world));
					++count;
				}
			}
		}

		if (count == 0) {
			return -1;
		} else {
			return yTotal / count;
		}
	}
}
