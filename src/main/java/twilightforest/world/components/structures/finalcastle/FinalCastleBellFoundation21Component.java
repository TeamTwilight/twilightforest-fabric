package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;

/**
 * A larger foundation that comes all the way from the top of a tower
 *
 * @author benma_000
 */
public class FinalCastleBellFoundation21Component extends FinalCastleFoundation13Component {

	public FinalCastleBellFoundation21Component(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCBeF21.get(), nbt);
	}

	public FinalCastleBellFoundation21Component(int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(TFStructurePieceTypes.TFFCBeF21.get(), i, sideTower, x, y, z);

		this.boundingBox = new BoundingBox(sideTower.getBoundingBox().minX() - 2, sideTower.getBoundingBox().maxY() - 1, sideTower.getBoundingBox().minZ() - 2, sideTower.getBoundingBox().minX() + 2, sideTower.getBoundingBox().maxY(), sideTower.getBoundingBox().maxZ() + 2);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int mid = 16;
		int low = 32;

		// assume square
		int size = this.boundingBox.maxX() - this.boundingBox.minX();

		for (Rotation rotation : RotationUtil.ROTATIONS) {
			// do corner
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -1, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -mid, 0, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -1, 2, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -mid, 2, rotation, sbb);

			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -low, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -low, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -low, 0, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -low, 0, rotation, sbb);

			for (int x = 6; x < (size - 3); x += 4) {
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -1, 1, rotation, sbb);
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -mid, 0, rotation, sbb);
			}
		}
	}
}
