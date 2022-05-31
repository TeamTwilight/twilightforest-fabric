package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.util.EntityDestroyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nullable;

public class ForceFieldBlock extends Block implements SimpleWaterloggedBlock, EntityDestroyBlock {
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
	public static final BooleanProperty DOWN = PipeBlock.DOWN;
	public static final BooleanProperty UP = PipeBlock.UP;
	public static final BooleanProperty NORTH = PipeBlock.NORTH;
	public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
	public static final BooleanProperty WEST = PipeBlock.WEST;
	public static final BooleanProperty EAST = PipeBlock.EAST;

	protected static final VoxelShape BASE_SHAPE = Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);

	protected static final VoxelShape WEST_SHAPE = Block.box(0.0D, 7.0D, 7.0D, 7.0D, 9.0D, 9.0D);
	protected static final VoxelShape EAST_SHAPE = Block.box(9.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D);
	protected static final VoxelShape UP_SHAPE = Block.box(7.0D, 9.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 7.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.box(7.0D, 7.0D, 9.0D, 9.0D, 9.0D, 16.0D);

	protected static final VoxelShape DOWN_WEST_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 7.0D, 7.0D, 9.0D);
	protected static final VoxelShape DOWN_EAST_SHAPE = Block.box(9.0D, 0.0D, 7.0D, 16.0D, 7.0D, 9.0D);
	protected static final VoxelShape DOWN_NORTH_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 7.0D, 7.0D);
	protected static final VoxelShape DOWN_SOUTH_SHAPE = Block.box(7.0D, 0.0D, 9.0D, 9.0D, 7.0D, 16.0D);

	protected static final VoxelShape UP_WEST_SHAPE = Block.box(0.0D, 9.0D, 7.0D, 7.0D, 16.0D, 9.0D);
	protected static final VoxelShape UP_EAST_SHAPE = Block.box(9.0D, 9.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	protected static final VoxelShape UP_NORTH_SHAPE = Block.box(7.0D, 9.0D, 0.0D, 9.0D, 16.0D, 7.0D);
	protected static final VoxelShape UP_SOUTH_SHAPE = Block.box(7.0D, 9.0D, 9.0D, 9.0D, 16.0D, 16.0D);

	protected static final VoxelShape NORTH_WEST_SHAPE = Block.box(0.0D, 7.0D, 0.0D, 7.0D, 9.0D, 7.0D);
	protected static final VoxelShape NORTH_EAST_SHAPE = Block.box(9.0D, 7.0D, 0.0D, 16.0D, 9.0D, 7.0D);
	protected static final VoxelShape SOUTH_WEST_SHAPE = Block.box(0.0D, 7.0D, 9.0D, 7.0D, 9.0D, 16.0D);
	protected static final VoxelShape SOUTH_EAST_SHAPE = Block.box(9.0D, 7.0D, 9.0D, 16.0D, 9.0D, 16.0D);

	ForceFieldBlock(BlockBehaviour.Properties props) {
		super(props);
		this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false).setValue(AXIS, Direction.Axis.Y)
				.setValue(DOWN, false).setValue(UP, false)
				.setValue(NORTH, false).setValue(SOUTH, false)
				.setValue(WEST, false).setValue(EAST, false));
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return this.getVoxelShape(state);
	}

	private VoxelShape getVoxelShape(BlockState state) {
		VoxelShape shape = BASE_SHAPE;

		Direction.Axis axis = state.getValue(AXIS);

		boolean down = state.getValue(DOWN);
		boolean up = state.getValue(UP);
		boolean north = state.getValue(NORTH);
		boolean south = state.getValue(SOUTH);
		boolean west = state.getValue(WEST);
		boolean east = state.getValue(EAST);

		boolean x = west && east;
		boolean y = down && up;
		boolean z = north && south;

		if (down) {
			shape = Shapes.or(shape, DOWN_SHAPE);
			if (north && (!x || axis != Direction.Axis.X)) shape = Shapes.or(shape, DOWN_NORTH_SHAPE);
			if (south && (!x || axis != Direction.Axis.X)) shape = Shapes.or(shape, DOWN_SOUTH_SHAPE);
			if (west && (!z || axis != Direction.Axis.Z)) shape = Shapes.or(shape, DOWN_WEST_SHAPE);
			if (east && (!z || axis != Direction.Axis.Z)) shape = Shapes.or(shape, DOWN_EAST_SHAPE);
		}
		if (up) {
			shape = Shapes.or(shape, UP_SHAPE);
			if (north && (!x || axis != Direction.Axis.X)) shape = Shapes.or(shape, UP_NORTH_SHAPE);
			if (south && (!x || axis != Direction.Axis.X)) shape = Shapes.or(shape, UP_SOUTH_SHAPE);
			if (west && (!z || axis != Direction.Axis.Z)) shape = Shapes.or(shape, UP_WEST_SHAPE);
			if (east && (!z || axis != Direction.Axis.Z)) shape = Shapes.or(shape, UP_EAST_SHAPE);
		}
		if (north) {
			shape = Shapes.or(shape, NORTH_SHAPE);
			if (west && (!y || axis != Direction.Axis.Y)) shape = Shapes.or(shape, NORTH_WEST_SHAPE);
			if (east && (!y || axis != Direction.Axis.Y)) shape = Shapes.or(shape, NORTH_EAST_SHAPE);
		}
		if (south) {
			shape = Shapes.or(shape, SOUTH_SHAPE);
			if (west && (!y || axis != Direction.Axis.Y)) shape = Shapes.or(shape, SOUTH_WEST_SHAPE);
			if (east && (!y || axis != Direction.Axis.Y)) shape = Shapes.or(shape, SOUTH_EAST_SHAPE);
		}
		if (west) shape = Shapes.or(shape, WEST_SHAPE);
		if (east) shape = Shapes.or(shape, EAST_SHAPE);

		return shape;
	}

	@Environment(EnvType.CLIENT)
	public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
		if (adjacentState.is(this)) {
			BooleanProperty opposite = PipeBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite());
			if (adjacentState.getValue(opposite)) {
				BooleanProperty property = PipeBlock.PROPERTY_BY_DIRECTION.get(direction);

				if (countSides(state.setValue(opposite, false).setValue(property, false)) > countSides(adjacentState.setValue(opposite, false).setValue(property, false))
						&& state.getValue(AXIS) == direction.getAxis()) return false;


				VoxelShape a = this.getVoxelShape(state).getFaceShape(direction);
				VoxelShape b = this.getVoxelShape(adjacentState).getFaceShape(direction.getOpposite());

				return a.toAabbs().stream().anyMatch(aabb -> b.toAabbs().stream().anyMatch(aabb::equals));//Am I dumb or is this the only way of doing this without checking for every possible scenario?
			}
		}
		return false;
	}

	private static int countSides(BlockState state) {
		int count = 0;
		for (Direction direction : Direction.values()) if (state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction))) count++;
		return count;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return true;
	}

	public boolean canConnectTo(BlockGetter blockGetter, BlockPos pos, Direction direction) {
		BlockState state = blockGetter.getBlockState(pos.relative(direction));
		boolean solidSide = state.isFaceSturdy(blockGetter, pos.relative(direction), direction.getOpposite());
		Block block = state.getBlock();

		int betterConnections = 0;
		if (!state.is(this)) {
			for (Direction face : Direction.values()) {
				if (blockGetter.getBlockState(pos.relative(face)).is(this)) betterConnections++;
			}
			if (betterConnections > 3) return false; //Might need to alter this a bit, without this the forcefields in the final castles like to attach to every block, still does a bit in the main room :/
		}

		return !isExceptionForConnection(state) && solidSide || block instanceof ForceFieldBlock || block instanceof IronBarsBlock || state.is(BlockTags.WALLS);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		Direction clicked = context.getClickedFace();

		return this.defaultBlockState()
				.setValue(AXIS, context.getClickedFace().getAxis())
				.setValue(DOWN, clicked != Direction.DOWN ? this.canConnectTo(level, pos, Direction.DOWN) : !context.isSecondaryUseActive())
				.setValue(UP, clicked != Direction.UP ? this.canConnectTo(level, pos, Direction.UP) : !context.isSecondaryUseActive())
				.setValue(NORTH, clicked != Direction.NORTH ? this.canConnectTo(level, pos, Direction.NORTH) : !context.isSecondaryUseActive())
				.setValue(EAST, clicked != Direction.EAST ? this.canConnectTo(level, pos, Direction.EAST) : !context.isSecondaryUseActive())
				.setValue(SOUTH, clicked != Direction.SOUTH ? this.canConnectTo(level, pos, Direction.SOUTH) : !context.isSecondaryUseActive())
				.setValue(WEST, clicked != Direction.WEST ? this.canConnectTo(level, pos, Direction.WEST) : !context.isSecondaryUseActive());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor levelAccessor, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
		return state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), this.canConnectTo(levelAccessor, pos, direction));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED, AXIS,  NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
}
