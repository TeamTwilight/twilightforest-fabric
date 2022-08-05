package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WallPillarBlock extends ConnectableRotatedPillarBlock implements SimpleWaterloggedBlock {

	private static final VoxelShape TOP_X = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
	private static final VoxelShape BOTTOM_X = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape PILLAR_X = Block.box(0.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);
	private static final VoxelShape NO_TOP_X = Shapes.or(PILLAR_X, BOTTOM_X);
	private static final VoxelShape NO_BOTTOM_X = Shapes.or(PILLAR_X, TOP_X);
	private static final VoxelShape FULL_X = Shapes.or(PILLAR_X, BOTTOM_X, TOP_X);

	private static final VoxelShape TOP_Y = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape BOTTOM_Y = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
	private static final VoxelShape PILLAR_Y = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	private static final VoxelShape NO_TOP_Y = Shapes.or(PILLAR_Y, BOTTOM_Y);
	private static final VoxelShape NO_BOTTOM_Y = Shapes.or(PILLAR_Y, TOP_Y);
	private static final VoxelShape FULL_Y = Shapes.or(PILLAR_Y, BOTTOM_Y, TOP_Y);

	private static final VoxelShape TOP_Z = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
	private static final VoxelShape BOTTOM_Z = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape PILLAR_Z = Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 16.0D);
	private static final VoxelShape NO_TOP_Z = Shapes.or(PILLAR_Z, BOTTOM_Z);
	private static final VoxelShape NO_BOTTOM_Z = Shapes.or(PILLAR_Z, TOP_Z);
	private static final VoxelShape FULL_Z = Shapes.or(PILLAR_Z, BOTTOM_Z, TOP_Z);

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public WallPillarBlock(Material material, double width, double height) {
		super(Properties.of(material).strength(1.5F, 6.0F).requiresCorrectToolForDrops().noOcclusion(), width, height);
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(WallPillarBlock.AXIS)) {
			case X -> state.getValue(PipeBlock.WEST) && state.getValue(PipeBlock.EAST) ? PILLAR_X :
					state.getValue(PipeBlock.WEST) ? NO_TOP_X : state.getValue(PipeBlock.EAST) ? NO_BOTTOM_X : FULL_X;
			case Z -> state.getValue(PipeBlock.NORTH) && state.getValue(PipeBlock.SOUTH) ? PILLAR_Z :
					state.getValue(PipeBlock.NORTH) ? NO_TOP_Z : state.getValue(PipeBlock.SOUTH) ? NO_BOTTOM_Z : FULL_Z;
			default -> state.getValue(PipeBlock.UP) && state.getValue(PipeBlock.DOWN) ? PILLAR_Y :
					state.getValue(PipeBlock.UP) ? NO_TOP_Y : state.getValue(PipeBlock.DOWN) ? NO_BOTTOM_Y : FULL_Y;
		};
	}

	@Override
	public boolean canConnectTo(BlockState state, boolean solidSide) {
		return state.getBlock() instanceof WallPillarBlock;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		boolean flag = fluidstate.getType() == Fluids.WATER;
		return super.getStateForPlacement(context).setValue(WATERLOGGED, flag);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}
}
