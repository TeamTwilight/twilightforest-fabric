package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.block.CustomPathNodeTypeBlock;
import io.github.fabricators_of_create.porting_lib.util.PlayerDestroyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageSources;

import org.jetbrains.annotations.Nullable;

public class ThornsBlock extends ConnectableRotatedPillarBlock implements SimpleWaterloggedBlock, CustomPathNodeTypeBlock, PlayerDestroyBlock {

	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final float THORN_DAMAGE = 4.0F;

	public ThornsBlock(Properties props) {
		super(props, 10);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false).setValue(AXIS, Direction.Axis.Y)
				.setValue(DOWN, false).setValue(UP, false)
				.setValue(NORTH, false).setValue(SOUTH, false)
				.setValue(WEST, false).setValue(EAST, false));
	}

	@Override
	public boolean canConnectTo(BlockState state, boolean solidSide) {
		return (state.getBlock() instanceof ThornsBlock
						|| state.getBlock() == TFBlocks.THORN_ROSE.get()
						|| state.getBlock() == TFBlocks.THORN_LEAVES.get()
						|| state.getMaterial() == Material.PLANT
						|| state.getMaterial() == Material.DIRT);
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob entity) {
		return BlockPathTypes.DAMAGE_CACTUS;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		entity.hurt(TFDamageSources.THORNS, THORN_DAMAGE);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {

		if (state.getBlock() instanceof ThornsBlock && state.getValue(AXIS) == Direction.Axis.Y) {
			entityInside(state, level, pos, entity);
		}

		super.stepOn(level, pos, state, entity);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if (!player.getAbilities().instabuild) {
			if (!level.isClientSide()) {
				// grow more
				this.doThornBurst(level, pos, state);
			}
			return false;
		} else {
			return PlayerDestroyBlock.super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
		}
	}

	@Override
	@Deprecated
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	/**
	 * Grow thorns out of both the ends, then maybe in another direction too
	 */
	private void doThornBurst(Level level, BlockPos pos, BlockState state) {
		switch (state.getValue(AXIS)) {
			case Y -> {
				this.growThorns(level, pos, Direction.UP);
				this.growThorns(level, pos, Direction.DOWN);
			}
			case X -> {
				this.growThorns(level, pos, Direction.EAST);
				this.growThorns(level, pos, Direction.WEST);
			}
			case Z -> {
				this.growThorns(level, pos, Direction.NORTH);
				this.growThorns(level, pos, Direction.SOUTH);
			}
		}

		// also try three random directions
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
	}

	/**
	 * grow several green thorns in the specified direction
	 */
	private void growThorns(Level level, BlockPos pos, Direction dir) {
		int length = 1 + level.getRandom().nextInt(3);

		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			if (level.isEmptyBlock(dPos)) {
				level.setBlock(dPos, TFBlocks.GREEN_THORNS.get().defaultBlockState().setValue(AXIS, dir.getAxis()), 2);
			} else {
				break;
			}
		}
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
