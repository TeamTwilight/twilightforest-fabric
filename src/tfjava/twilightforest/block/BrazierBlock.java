package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.BrazierBlockEntity;
import twilightforest.enums.BrazierLight;
import twilightforest.init.TFBlockEntities;

public class BrazierBlock extends BaseEntityBlock {
	public static final MapCodec<BrazierBlock> CODEC = simpleCodec(BrazierBlock::new);
	private static final VoxelShape UPPER_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	private static final VoxelShape LOWER_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final EnumProperty<BrazierLight> LIGHT = EnumProperty.create("light", BrazierLight.class);
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public BrazierBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LIGHT, BrazierLight.OFF).setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? new BrazierBlockEntity(pos, state) : null;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.BRAZIER, BrazierBlockEntity::tick);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIGHT, HALF);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		DoubleBlockHalf half = state.getValue(HALF);
		if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP)) {
			return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, facing, facingState, level, currentPos, facingPos);
		}
		return facingState.getBlock() instanceof BrazierBlock && facingState.getValue(HALF) != half
			? facingState.setValue(HALF, half).setValue(LIGHT, state.getValue(LIGHT))
			: Blocks.AIR.defaultBlockState();
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && (player.isCreative() || !player.hasCorrectToolForDrops(state)) && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockPos below = pos.below();
			BlockState blockstate = level.getBlockState(below);
			if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
				BlockState replacement = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				level.setBlock(below, replacement, 35);
				level.levelEvent(player, 2001, below, Block.getId(blockstate));
			}
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context)
			? this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER)
			: null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos below = pos.below();
		BlockState blockstate = level.getBlockState(below);
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(level, below, Direction.UP) : blockstate.is(this);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!state.is(this) || state.getValue(HALF) != DoubleBlockHalf.UPPER) {
			return super.useItemOn(stack, state, level, pos, player, hand, result);
		}

		if (state.getValue(LIGHT) != BrazierLight.FULL && (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE))) {
			BrazierLight light = state.cycle(LIGHT).getValue(LIGHT);
			this.setBothHalves(level, pos, state, light);
			if (!level.isClientSide()) {
				if (stack.is(Items.FLINT_AND_STEEL)) {
					stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
				} else {
					stack.consume(1, player);
				}
				level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS);
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}

		if (state.getValue(LIGHT).isLit() && this.containsWater(stack)) {
			if (!level.isClientSide()) {
				if (!this.tryDrainWaterContainer(player, hand)) {
					return ItemInteractionResult.FAIL;
				}
				this.setBothHalves(level, pos, state, BrazierLight.OFF);
				level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS);
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}

		return super.useItemOn(stack, state, level, pos, player, hand, result);
	}

	private boolean containsWater(ItemStack stack) {
		Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
		if (storage == null) {
			return false;
		}
		try (Transaction transaction = Transaction.openOuter()) {
			return storage.extract(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction) == FluidConstants.BUCKET;
		}
	}

	private void setBothHalves(Level level, BlockPos upperPos, BlockState upperState, BrazierLight light) {
		level.setBlock(upperPos, upperState.setValue(HALF, DoubleBlockHalf.UPPER).setValue(LIGHT, light), 11);
		BlockPos lowerPos = upperPos.below();
		BlockState lowerState = level.getBlockState(lowerPos);
		if (lowerState.is(this) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER) {
			level.setBlock(lowerPos, lowerState.setValue(LIGHT, light), 11);
		}
	}

	private boolean tryDrainWaterContainer(Player player, InteractionHand hand) {
		ContainerItemContext context = ContainerItemContext.forPlayerInteraction(player, hand);
		Storage<FluidVariant> storage = context.find(FluidStorage.ITEM);
		if (storage == null) {
			return false;
		}

		try (Transaction transaction = Transaction.openOuter()) {
			long extracted = storage.extract(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction);
			if (extracted == FluidConstants.BUCKET) {
				transaction.commit();
				return true;
			}
		}
		return false;
	}
}
