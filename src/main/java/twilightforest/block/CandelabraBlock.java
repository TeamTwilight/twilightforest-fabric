package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;
import java.util.List;

// The code is flexible to allow colors but I'm not sure if they'd look good on Candelabra
public class CandelabraBlock extends AbstractLightableBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty ON_WALL = BooleanProperty.create("on_wall");
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final VoxelShape CANDLES_NORTH = Shapes.or(Block.box(1, 7, 2, 15, 15, 6), Block.box(1, 1, 3.5, 15, 7, 4.5), Block.box(7.5, 1, 1, 8.5, 7, 7), Block.box(6, 2, 0, 10, 6, 1));
	public static final VoxelShape CANDLES_SOUTH = Shapes.or(Block.box(1, 7, 10, 15, 15, 14), Block.box(1, 1, 11.5, 15, 7, 12.5), Block.box(7.5, 1, 9, 8.5, 7, 15), Block.box(6, 2, 15, 10, 6, 16));

	public static final VoxelShape CANDLES_WEST = Shapes.or(Block.box(2, 7, 1, 6, 15, 15), Block.box(3.5, 1, 1, 4.5, 7, 15), Block.box(1, 1, 7.5, 7, 7, 8.5), Block.box(0, 2, 6, 1, 6, 10));
	public static final VoxelShape CANDLES_EAST = Shapes.or(Block.box(10, 7, 1, 14, 15, 15), Block.box(11.5, 1, 1, 12.5, 7, 15), Block.box(9, 1, 7.5, 15, 7, 8.5), Block.box(15, 2, 6, 16, 6, 10));

	public static final VoxelShape CANDLES_X = Shapes.or(Block.box(6, 7, 1, 10, 15, 15), Block.box(7.5, 1, 1, 8.5, 7, 15), Block.box(5, 1, 7.5, 11, 7, 8.5), Block.box(6, 0, 6, 10, 1, 10));
	public static final VoxelShape CANDLES_Z = Shapes.or(Block.box(1, 7, 6, 15, 15, 10), Block.box(1, 1, 7.5, 15, 7, 8.5), Block.box(7.5, 1, 5, 8.5, 7, 11), Block.box(6, 0, 6, 10, 1, 10));

	public static final List<Vec3> NORTH_OFFSETS = List.of(new Vec3(0.1875D, 0.875D, 0.25D), new Vec3(0.5D, 0.875D, 0.25D), new Vec3(0.8125D, 0.875D, 0.25D));
	public static final List<Vec3> SOUTH_OFFSETS = List.of(new Vec3(0.1875D, 0.875D, 0.75D), new Vec3(0.5D, 0.875D, 0.75D), new Vec3(0.8125D, 0.875D, 0.75D));

	public static final List<Vec3> WEST_OFFSETS = List.of(new Vec3(0.25D, 0.875D, 0.1875D), new Vec3(0.25D, 0.875D, 0.5D), new Vec3(0.25D, 0.875D, 0.8125D));
	public static final List<Vec3> EAST_OFFSETS = List.of(new Vec3(0.75D, 0.875D, 0.1875D), new Vec3(0.75D, 0.875D, 0.5D), new Vec3(0.75D, 0.875D, 0.8125D));

	public static final List<Vec3> X_OFFSETS = List.of(new Vec3(0.5D, 0.875D, 0.1875D), new Vec3(0.5D, 0.875D, 0.5D), new Vec3(0.5D, 0.875D, 0.8125D));
	public static final List<Vec3> Z_OFFSETS = List.of(new Vec3(0.1875D, 0.875D, 0.5D), new Vec3(0.5D, 0.875D, 0.5D), new Vec3(0.8125D, 0.875D, 0.5D));

	public CandelabraBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ON_WALL, false).setValue(LIGHTING, Lighting.NONE).setValue(WATERLOGGED, false));
	}

	@Override
	protected Iterable<Vec3> getParticleOffsets(BlockState state, LevelAccessor accessor, BlockPos pos) {
		if (state.getValue(ON_WALL)) {
			return switch (state.getValue(FACING)) {
				case SOUTH -> SOUTH_OFFSETS;
				case WEST -> WEST_OFFSETS;
				case EAST -> EAST_OFFSETS;
				default -> NORTH_OFFSETS;
			};
		} else {
			return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_OFFSETS : Z_OFFSETS;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		if (state.getValue(ON_WALL)) {
			return switch (state.getValue(FACING)) {
				case SOUTH -> CANDLES_SOUTH;
				case WEST -> CANDLES_WEST;
				case EAST -> CANDLES_EAST;
				default -> CANDLES_NORTH;
			};
		} else {
			return state.getValue(FACING).getAxis() == Direction.Axis.X ? CANDLES_X : CANDLES_Z;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction clickDirection = context.getClickedFace();
		boolean onBottomBlock = clickDirection == Direction.UP;
		Direction[] placements = context.getNearestLookingDirections();
		BlockPos placePos = context.getClickedPos();
		Level level = context.getLevel();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		boolean flag = fluidstate.is(Fluids.WATER);

		// If placer is clicking the bottom block, then we want to test for the bottom block first
		//  before we cycle the walls for possible placements
		// Otherwise we test wall placements before testing the bottom block
		if (onBottomBlock) {
			if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
				return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false).setValue(WATERLOGGED, flag);

			for (Direction nextSide : placements)
				if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
					return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, true).setValue(WATERLOGGED, flag);
		} else {
			for (Direction nextSide : placements)
				if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
					return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, true).setValue(WATERLOGGED, flag);

			if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
				return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false).setValue(WATERLOGGED, flag);
		}

		// Fail
		return null;
	}

	@Override
	protected boolean canBeLit(BlockState state) {
		return super.canBeLit(state) && !state.getValue(WATERLOGGED);
	}

	@Override
	public boolean placeLiquid(LevelAccessor accessor, BlockPos pos, BlockState state, FluidState fluid) {
		if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluid.is(Fluids.WATER)) {
			boolean flag = state.getValue(LIGHTING) != Lighting.NONE;
			if (flag) extinguish(null, state, accessor, pos);

			accessor.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIGHTING, Lighting.NONE), 3);
			accessor.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(accessor));
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ON_WALL, LIGHTING, WATERLOGGED);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return canSurvive(reader, pos, state.getValue(ON_WALL), state.getValue(FACING));
	}

	public static boolean canSurvive(LevelReader reader, BlockPos pos, boolean onWall, Direction facing) {
		return canSupportCenter(reader, onWall ? pos.relative(facing) : pos.below(), Direction.UP);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		boolean ominous = state.getValue(LIGHTING) == Lighting.OMINOUS;
		if (state.getValue(LIGHTING) != Lighting.NONE) {
			this.getParticleOffsets(state, level, pos).forEach(vec3 -> addParticlesAndSound(level, pos, vec3.x, vec3.y, vec3.z, rand, ominous));
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return null;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
	}
}
