package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThornRoseBlock extends Block {

	private static final float RADIUS = 0.4F;
	private static final VoxelShape AABB = Shapes.create(new AABB(0.5F - RADIUS, 0.5F - RADIUS, 0.5F - RADIUS, 0.5F + RADIUS, .5F + RADIUS, 0.5F + RADIUS));

	public ThornRoseBlock(Properties properties) {
		super(properties);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}
}
