package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 1:1 port of upstream {@code twilightforest.block.HugeWaterLilyBlock} — vanilla
 * WaterlilyBlock subclass with an inflated voxel shape (1.6→14.4 instead of vanilla's
 * 1→15) so the huge variant occupies the proper visual footprint.
 */
public class HugeWaterLilyBlock extends WaterlilyBlock {

	private static final VoxelShape AABB = Block.box(1.6, 1.6, 1.6, 14.4, 14.4, 14.4);

	public HugeWaterLilyBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}
}
