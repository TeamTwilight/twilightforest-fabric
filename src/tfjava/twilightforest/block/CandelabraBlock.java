package twilightforest.block;

import com.google.common.collect.Iterables;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.CandelabraBlockEntity;
import twilightforest.components.item.CandelabraData;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

import java.util.List;
import java.util.Optional;

/**
 * 1:1 port of upstream {@code twilightforest.block.CandelabraBlock}.
 *
 * <p>NeoForge → Fabric translations:
 * <ul>
 *   <li>{@code stack.is(Tags.Items.DUSTS_REDSTONE)} → {@code stack.is(Items.REDSTONE)}.
 *       NeoForge "common" Tags.Items don't exist on Fabric; the upstream tag was a single-item
 *       {@code minecraft:redstone} which we read directly.</li>
 *   <li>{@code getToolModifiedState(... ItemAbility ...)} → method retained without @Override.
 *       Upstream NeoForge fired this from FireStarter items (flint-and-steel, fire-charge) to
 *       light a candelabra in-place. Fabric has no equivalent hook on Block; the
 *       {@link LightableBlock#tryLightCandles} interaction path covers the same flow.</li>
 *   <li>{@code canConnectRedstone(... Direction ...)} → method retained without @Override.
 *       Upstream NF method; Fabric BlockBehaviour doesn't provide it. Kept 1:1 in source so a
 *       future Fabric mixin/RedstoneConnectabilityRegistry-style integration can re-attach.</li>
 *   <li>{@code TFSounds.CANDELABRA_LIGHT} → codex direct field.</li>
 *   <li>{@code TFParticleType.X} → codex direct field.</li>
 * </ul>
 */
@SuppressWarnings("deprecation")
public class CandelabraBlock extends BaseEntityBlock implements LightableBlock, SimpleWaterloggedBlock {

	public static final BooleanProperty ON_WALL = BooleanProperty.create("on_wall");
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final List<BooleanProperty> CANDLES = List.of(BooleanProperty.create("has_candle_1"), BooleanProperty.create("has_candle_2"), BooleanProperty.create("has_candle_3"));
	public static final VoxelShape CANDLES_NORTH = Shapes.or(Block.box(1, 7, 2, 15, 15, 6), Block.box(1, 1, 3.5, 15, 7, 4.5), Block.box(7.5, 1, 1, 8.5, 7, 7), Block.box(6, 2, 0, 10, 6, 1));
	public static final VoxelShape CANDLES_SOUTH = Shapes.or(Block.box(1, 7, 10, 15, 15, 14), Block.box(1, 1, 11.5, 15, 7, 12.5), Block.box(7.5, 1, 9, 8.5, 7, 15), Block.box(6, 2, 15, 10, 6, 16));
	public static final VoxelShape CANDLES_WEST = Shapes.or(Block.box(2, 7, 1, 6, 15, 15), Block.box(3.5, 1, 1, 4.5, 7, 15), Block.box(1, 1, 7.5, 7, 7, 8.5), Block.box(0, 2, 6, 1, 6, 10));
	public static final VoxelShape CANDLES_EAST = Shapes.or(Block.box(10, 7, 1, 14, 15, 15), Block.box(11.5, 1, 1, 12.5, 7, 15), Block.box(9, 1, 7.5, 15, 7, 8.5), Block.box(15, 2, 6, 16, 6, 10));
	public static final VoxelShape CANDLES_X = Shapes.or(Block.box(6, 7, 1, 10, 15, 15), Block.box(7.5, 1, 1, 8.5, 7, 15), Block.box(5, 1, 7.5, 11, 7, 8.5), Block.box(6, 0, 6, 10, 1, 10));
	public static final VoxelShape CANDLES_Z = Shapes.or(Block.box(1, 7, 6, 15, 15, 10), Block.box(1, 1, 7.5, 15, 7, 8.5), Block.box(7.5, 1, 5, 8.5, 7, 11), Block.box(6, 0, 6, 10, 1, 10));
	public static final List<Vec3> NORTH_OFFSETS = List.of(new Vec3(0.1875D, 0.9D, 0.25D), new Vec3(0.5D, 0.9D, 0.25D), new Vec3(0.8125D, 0.9D, 0.25D));
	public static final List<Vec3> SOUTH_OFFSETS = List.of(new Vec3(0.1875D, 0.9D, 0.75D), new Vec3(0.5D, 0.9D, 0.75D), new Vec3(0.8125D, 0.9D, 0.75D));
	public static final List<Vec3> WEST_OFFSETS = List.of(new Vec3(0.25D, 0.9D, 0.1875D), new Vec3(0.25D, 0.9D, 0.5D), new Vec3(0.25D, 0.9D, 0.8125D));
	public static final List<Vec3> EAST_OFFSETS = List.of(new Vec3(0.75D, 0.9D, 0.1875D), new Vec3(0.75D, 0.9D, 0.5D), new Vec3(0.75D, 0.9D, 0.8125D));
	public static final List<Vec3> X_OFFSETS = List.of(new Vec3(0.5D, 0.9D, 0.1875D), new Vec3(0.5D, 0.9D, 0.5D), new Vec3(0.5D, 0.9D, 0.8125D));
	public static final List<Vec3> Z_OFFSETS = List.of(new Vec3(0.1875D, 0.9D, 0.5D), new Vec3(0.5D, 0.9D, 0.5D), new Vec3(0.8125D, 0.9D, 0.5D));
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final MapCodec<CandelabraBlock> CODEC = simpleCodec(CandelabraBlock::new);

	public CandelabraBlock(Properties properties) {
		super(properties);
		BlockState state = this.getStateDefinition().any().setValue(LIGHTING, Lighting.NONE).setValue(FACING, Direction.NORTH).setValue(ON_WALL, false).setValue(LIGHTING, Lighting.NONE).setValue(WATERLOGGED, false);

		for (BooleanProperty booleanproperty : CANDLES) {
			state = state.setValue(booleanproperty, false);
		}

		this.registerDefaultState(state);
	}

	public static int getCandleCount(BlockState state) {
		int candleCount = 0;
		for (BooleanProperty property : CANDLES) {
			if (state.getValue(property)) candleCount++;
		}
		return candleCount;
	}

	public static boolean canSurvive(LevelReader reader, BlockPos pos, boolean onWall, Direction facing) {
		return canSupportCenter(reader, onWall ? pos.relative(facing) : pos.below(), Direction.UP);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	// Codex Fabric port note: vanilla 1.21.1 BlockBehaviour has no `getLightEmission(state, getter, pos)`
	// overridable method (NeoForge patched it on). Vanilla light emission is set via the
	// {@code BlockBehaviour.Properties.lightLevel(state-fn)} call at construction time. The
	// candelabraBlock helper passes {@link #lightForState(BlockState)} into Properties.lightLevel
	// so vanilla resolves emission per-state via the state-only signature; this method is kept
	// for 1:1 parity with upstream callers + future Fabric mixin/registry hookup.
	public static int lightForState(BlockState state) {
		int candleCount = getCandleCount(state);
		return switch (state.getValue(LIGHTING)) {
			case DIM, OMINOUS -> 2 * candleCount;
			case NORMAL -> 5 * candleCount;
			default -> 0;
		};
	}

	public int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos) {
		return lightForState(state);
	}

	@Override
	public Iterable<Vec3> getParticleOffsets(BlockState state, LevelAccessor accessor, BlockPos pos) {
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

	// Codex Fabric port note: NeoForge `getToolModifiedState(BlockState, UseOnContext, ItemAbility, boolean)`
	// fires from FireStarter ItemAbility to light a candelabra in-place. Fabric BlockBehaviour has
	// no equivalent hook; the LightableBlock.tryLightCandles flow (called from useItemOn) covers
	// the same player-facing behaviour. Method body retained 1:1 for future mixin/registry hookup.
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, Object itemAbility, boolean simulate) {
		if (this.canBeLit(state)) {
			return state.setValue(LIGHTING, Lighting.NORMAL);
		}
		return null;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (stack.is(ItemTags.CANDLES) || player.isShiftKeyDown()) {
			if (level.getBlockEntity(pos) instanceof CandelabraBlockEntity candelabra) {
				int i = this.getSlot(state.getValue(FACING), result.getLocation().subtract(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()));
				if (state.getValue(CANDLES.get(i)) && player.isShiftKeyDown()) {
					ItemStack itemstack = new ItemStack(candelabra.removeCandle(i));
					level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
					if (player.hasInfiniteMaterials()) {
						if (!player.getInventory().contains(itemstack)) {
							player.getInventory().add(itemstack);
						}
					} else {
						if (!player.getInventory().add(itemstack)) {
							player.drop(itemstack, false);
						}
					}
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					return ItemInteractionResult.sidedSuccess(level.isClientSide());
				} else if (!state.getValue(CANDLES.get(i))) {
					if (stack.is(ItemTags.CANDLES) && stack.getItem() instanceof BlockItem block) {
						if (!level.isClientSide()) {
							player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
							candelabra.setCandle(i, block.getBlock());
							level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
							stack.consume(1, player);
						}
						return ItemInteractionResult.sidedSuccess(level.isClientSide());
					}
				}
				return ItemInteractionResult.CONSUME;
			}
			// Codex Fabric: NeoForge Tags.Items.DUSTS_REDSTONE → vanilla minecraft:redstone item directly.
		} else if (stack.is(Items.REDSTONE) && state.getValue(LIGHTING) == Lighting.NORMAL) {
			level.setBlockAndUpdate(pos, state.setValue(LIGHTING, Lighting.DIM));
			stack.consume(1, player);
			level.playSound(null, pos, TFSounds.CANDELABRA_LIGHT, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
			if (level.isClientSide()) {
				this.eruptFlameParticles(TFParticleType.DIM_FLAME, level, pos, state);
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		} else if ((stack.is(ItemTagGenerator.SCEPTERS) || stack.is(TFItems.EXANIMATE_ESSENCE.get())) && state.getValue(LIGHTING) == Lighting.NORMAL) {
			level.setBlockAndUpdate(pos, state.setValue(LIGHTING, Lighting.OMINOUS));
			level.playSound(null, pos, TFSounds.CANDELABRA_OMINOUS, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
			if (level.isClientSide()) {
				this.eruptFlameParticles(TFParticleType.OMINOUS_FLAME, level, pos, state);
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}
		return this.tryLightCandles(stack, state, level, pos, player, hand);
	}

	private void eruptFlameParticles(ParticleOptions particle, Level level, BlockPos pos, BlockState state) {
		for (int i = 0; i < 3; i++) {
			if (state.getValue(CANDLES.get(i))) {
				Vec3 vec = Iterables.get(this.getParticleOffsets(state, level, pos), i);
				for (int j = 0; j < 5; j++) {
					level.addParticle(particle, pos.getX() + vec.x, pos.getY() + vec.y, pos.getZ() + vec.z, (level.getRandom().nextDouble() - 0.5D) * 0.05D, 0.015F, (level.getRandom().nextDouble() - 0.5D) * 0.05D);
				}
			}
		}
	}

	protected void updateNeighborsBasedOnRotation(Level level, BlockPos pos, BlockState state) {
		if (state.getValue(ON_WALL)) {
			Direction direction = state.getValue(FACING);
			BlockPos blockpos = pos.relative(direction);
			level.neighborChanged(blockpos, this, pos);
			level.updateNeighborsAtExceptFromFacing(blockpos, this, direction.getOpposite());
		} else {
			level.neighborChanged(pos.below(), this, pos);
			level.updateNeighborsAtExceptFromFacing(pos.below(), this, Direction.UP);
		}
	}

	protected int getSlot(Direction blockDir, Vec3 hitVec) {
		Vec3i up = new Vec3i(0, 1, 0);
		Vec3i dir = up.cross(blockDir.getNormal());
		boolean reverse = blockDir == Direction.NORTH || blockDir == Direction.EAST;

		double cx = dir.getX() * hitVec.x() + dir.getZ() * hitVec.z();

		if (cx <= 0.0D) {
			cx = cx + 1;
		}

		if (cx <= 0.375F) {
			return reverse ? 2 : 0;
		} else {
			return cx <= 0.6875F ? 1 : reverse ? 0 : 2;
		}
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
		this.lightCandlesWithProjectile(level, state, result, projectile);
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

		if (onBottomBlock) {
			if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
				return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false).setValue(WATERLOGGED, flag);

			for (Direction nextSide : placements)
				if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
					return this.defaultBlockState().setValue(FACING, nextSide).setValue(ON_WALL, true).setValue(WATERLOGGED, flag);
		} else {
			for (Direction nextSide : placements)
				if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
					return this.defaultBlockState().setValue(FACING, nextSide).setValue(ON_WALL, true).setValue(WATERLOGGED, flag);

			if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
				return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false).setValue(WATERLOGGED, flag);
		}

		return null;
	}

	@Override
	public boolean canBeLit(BlockState state) {
		return state.getValue(LIGHTING) == Lighting.NONE && !state.getValue(WATERLOGGED) && getCandleCount(state) > 0;
	}

	@Override
	public boolean placeLiquid(LevelAccessor accessor, BlockPos pos, BlockState state, FluidState fluid) {
		if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluid.is(Fluids.WATER)) {
			boolean flag = state.getValue(LIGHTING) != Lighting.NONE;
			if (flag) this.extinguish(null, state, accessor, pos);

			accessor.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIGHTING, Lighting.NONE), Block.UPDATE_ALL);
			accessor.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(accessor));
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ON_WALL, LIGHTING, WATERLOGGED);
		CANDLES.forEach(builder::add);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return canSurvive(reader, pos, state.getValue(ON_WALL), state.getValue(FACING));
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
		if (state.getValue(LIGHTING) != Lighting.NONE) {
			for (int i = 0; i < 3; i++) {
				if (state.getValue(CANDLES.get(i))) {
					Vec3 vec = Iterables.get(this.getParticleOffsets(state, level, pos), i);
					this.addParticlesAndSound(level, pos, vec.x, vec.y, vec.z, rand, state.getValue(LIGHTING));
				}
			}
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CandelabraBlockEntity(pos, state);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		Optional<ItemStack> base = drops.stream().filter(item -> item.is(this.asItem())).findFirst();
		if (base.isPresent()) {
			BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
			if (blockEntity instanceof CandelabraBlockEntity candelabra) {
				RegistryAccess access = blockEntity.getLevel().registryAccess();
				// Codex Fabric port: vanilla 1.21.1 ItemStack.getEnchantmentLevel was removed —
				// EnchantmentHelper.getItemEnchantmentLevel(Holder, ItemStack) is the canonical
				// replacement.
				if (!builder.getParameter(LootContextParams.TOOL).isEmpty() && net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(access.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), builder.getParameter(LootContextParams.TOOL)) > 0) {
					ItemStack newStack = new ItemStack(this);
					newStack.applyComponents(candelabra.collectComponents());
					drops.remove(base.get());
					drops.add(newStack);
				} else {
					candelabra.getCandles().ordered().forEach(block -> drops.add(new ItemStack(block)));
				}
			}
		}

		return drops;
	}

	// Codex Fabric port note: NeoForge `canConnectRedstone(BlockState, BlockGetter, BlockPos, Direction)`
	// gates whether a redstone wire visually connects to this block face. Fabric BlockBehaviour
	// has no equivalent hook (redstone connectability is implicit from block class). Method body
	// retained 1:1 in source; getSignal/getDirectSignal still emit the analog signal so wires
	// downstream of the candelabra still respond, just without the explicit visual-connect bit.
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
		return state.getValue(LIGHTING) == Lighting.DIM && state.getValue(ON_WALL) && state.getValue(FACING).getOpposite() == direction;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		if (state.getValue(LIGHTING) == Lighting.DIM) {
			if (state.getValue(ON_WALL)) {
				return state.getValue(FACING) == direction.getOpposite() ? getCandleCount(state) * 3 : 0;
			} else {
				return direction == Direction.UP ? getCandleCount(state) * 3 : 0;
			}
		}
		return 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		return state.getSignal(getter, pos, direction);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		int candleCount = getCandleCount(state);
		return switch (state.getValue(LIGHTING)) {
			case DIM -> 3 + candleCount;
			case OMINOUS -> 6 + candleCount;
			case NORMAL -> 9 + candleCount;
			default -> candleCount;
		};
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		this.updateNeighborsBasedOnRotation(level, pos, state);
		if (level.getBlockEntity(pos) instanceof CandelabraBlockEntity candelabra && !newState.is(state.getBlock())) {
			candelabra.updateState(0); //force an update so the blockstates are synced up with the block entity data
		}
		super.onPlace(state, level, pos, newState, moving);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (!state.is(newState.getBlock())) {
			this.updateNeighborsBasedOnRotation(level, pos, state);
		}
		super.onRemove(state, level, pos, newState, moving);
	}

	// Codex Fabric port: vanilla 1.21.1 has the 3-arg signature (LevelReader, BlockPos, BlockState);
	// upstream NeoForge added the 5-arg HitResult/Player variant. We override the vanilla 3-arg
	// signature so middle-click pick-block on a candelabra still copies the candle component, and
	// we keep a 5-arg dispatcher (no @Override) for any caller that still expects the NF shape.
	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof CandelabraBlockEntity candelabra && candelabra.getCandles() != CandelabraData.EMPTY) {
			ItemStack itemstack = new ItemStack(this);
			itemstack.applyComponents(candelabra.collectComponents());
			return itemstack;
		}
		return super.getCloneItemStack(level, pos, state);
	}

	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
		return this.getCloneItemStack(level, pos, state);
	}
}
