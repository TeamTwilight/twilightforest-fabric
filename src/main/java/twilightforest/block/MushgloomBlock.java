package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.util.PlantType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.world.registration.features.TFConfiguredFeatures;

public class MushgloomBlock extends MushroomBlock {

	private static final VoxelShape MUSHGLOOM_SHAPE = box(2, 0, 2, 14, 8, 14);

	public MushgloomBlock(Properties props) {
		super(props, () -> TFConfiguredFeatures.BIG_MUSHGLOOM);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.UP) || world.getBlockState(pos.below()).is(TFBlocks.UBEROUS_SOIL.get());
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter access, BlockPos pos, CollisionContext context) {
		return MUSHGLOOM_SHAPE;
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos pos, BlockState state, boolean isClientSide) {
		return false;
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return PlantType.CAVE;
	}
}
