package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.components.item.SkullCandles;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFDataComponents;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractSkullCandleBlock extends BaseEntityBlock implements LightableBlock {
	public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;
	private final SkullBlock.Type type;

	public AbstractSkullCandleBlock(SkullBlock.Type type, Properties properties) {
		super(properties);
		this.type = type;
		this.registerDefaultState(this.getStateDefinition().any().setValue(LIGHTING, Lighting.NONE).setValue(CANDLES, 1));
	}

	public SkullBlock.Type getType() {
		return this.type;
	}

	public static int lightForState(BlockState state) {
		return switch (state.getValue(LIGHTING)) {
			case NORMAL -> 3 * state.getValue(CANDLES);
			case OMINOUS -> 2 * state.getValue(CANDLES);
			case DIM -> state.getValue(CANDLES);
			default -> 0;
		};
	}

	public int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos) {
		return lightForState(state);
	}

	public BlockState getToolModifiedState(BlockState state, UseOnContext context, Object itemAbility, boolean simulate) {
		return this.canBeLit(state) ? state.setValue(LIGHTING, Lighting.NORMAL) : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SkullCandleBlockEntity(pos, state, 0);
	}

	public static Block candleColorToCandle(CandleColors color) {
		if (color != CandleColors.PLAIN) {
			return Objects.requireNonNull(BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_candle")));
		}
		return Blocks.CANDLE;
	}

	public static CandleColors candleToCandleColor(Item candle) {
		if (candle != Blocks.CANDLE.asItem()) {
			return CandleColors.valueOf(BuiltInRegistries.ITEM.getKey(candle).getPath().replace("_candle", "").replace("\"", "").toUpperCase(Locale.ROOT));
		}
		return CandleColors.PLAIN;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof SkullCandleBlockEntity sc) {
			SkullCandles skullCandles = stack.getOrDefault(TFDataComponents.SKULL_CANDLES, SkullCandles.DEFAULT);
			sc.setCandleColor(skullCandles.color());
			if (this.type == SkullBlock.Types.PLAYER && stack.has(DataComponents.PROFILE)) {
				sc.setOwner(stack.get(DataComponents.PROFILE));
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		Optional<ItemStack> skullStack = drops.stream().filter(item -> item.is(ItemTags.SKULLS) && !item.is(this.asItem())).findFirst();
		if (skullStack.isPresent()) {
			BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
			if (blockEntity instanceof SkullCandleBlockEntity sc) {
				ItemStack tool = builder.getParameter(LootContextParams.TOOL);
				if (!tool.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(builder.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), tool) > 0) {
					ItemStack newStack = new ItemStack(this);
					newStack.set(TFDataComponents.SKULL_CANDLES, new SkullCandles(sc.getCandleColor(), state.getValue(CANDLES)));
					if (this.type == SkullBlock.Types.PLAYER && sc.getOwnerProfile() != null) {
						newStack.set(DataComponents.PROFILE, sc.getOwnerProfile());
					}
					drops.remove(skullStack.get());
					drops.add(newStack);
				} else {
					drops.add(new ItemStack(candleColorToCandle(CandleColors.colorFromInt(sc.getCandleColor())), state.getValue(CANDLES)));
				}
			}
		}
		return drops;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		ItemStack newStack = new ItemStack(this);
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			newStack.set(TFDataComponents.SKULL_CANDLES, new SkullCandles(sc.getCandleColor(), state.getValue(CANDLES)));
			if (this.type == SkullBlock.Types.PLAYER && sc.getOwnerProfile() != null) {
				newStack.set(DataComponents.PROFILE, sc.getOwnerProfile());
			}
		}
		return newStack;
	}

	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
		return this.getCloneItemStack(level, pos, state);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc
			&& stack.is(ItemTags.CANDLES)
			&& stack.is(candleColorToCandle(CandleColors.colorFromInt(sc.getCandleColor())).asItem())
			&& !player.isShiftKeyDown()) {
			int candles = state.getValue(CANDLES);
			if (candles < 4) {
				level.setBlockAndUpdate(pos, state.setValue(CANDLES, candles + 1));
				level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);
				level.getLightEngine().checkBlock(pos);
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		return this.tryLightCandles(stack, state, level, pos, player, hand);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc && player.isSecondaryUseActive() && state.getValue(CANDLES) > 0) {
			int newCandleAmount = state.getValue(CANDLES) - 1;
			if (newCandleAmount > 0) {
				level.setBlockAndUpdate(pos, state.setValue(CANDLES, newCandleAmount));
			} else {
				boolean wall = state.getBlock() instanceof WallSkullCandleBlock;
				Block newBlock = getNoCandleSkull(wall);
				if (newBlock != null) {
					ResolvableProfile profile = sc.getOwnerProfile();
					BlockState newState = wall
						? newBlock.defaultBlockState().setValue(WallSkullBlock.FACING, state.getValue(WallSkullCandleBlock.FACING))
						: newBlock.defaultBlockState().setValue(SkullBlock.ROTATION, state.getValue(SkullCandleBlock.ROTATION));
					level.setBlockAndUpdate(pos, newState);
					level.setBlockEntity(new net.minecraft.world.level.block.entity.SkullBlockEntity(pos, newState));
					if (level.getBlockEntity(pos) instanceof net.minecraft.world.level.block.entity.SkullBlockEntity skull) {
						skull.setOwner(profile);
					}
				}
			}
			level.playSound(null, pos, SoundEvents.CANDLE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.getLightEngine().checkBlock(pos);
			ItemStack candle = new ItemStack(candleColorToCandle(CandleColors.colorFromInt(sc.getCandleColor())));
			if (player.hasInfiniteMaterials()) {
				if (!player.getInventory().contains(candle)) {
					player.getInventory().add(candle);
				}
			} else if (!player.getInventory().add(candle)) {
				player.drop(candle, false);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Nullable
	private Block getNoCandleSkull(boolean wall) {
		return switch ((SkullBlock.Types) this.getType()) {
			case SKELETON -> wall ? Blocks.SKELETON_WALL_SKULL : Blocks.SKELETON_SKULL;
			case WITHER_SKELETON -> wall ? Blocks.WITHER_SKELETON_WALL_SKULL : Blocks.WITHER_SKELETON_SKULL;
			case PLAYER -> wall ? Blocks.PLAYER_WALL_HEAD : Blocks.PLAYER_HEAD;
			case ZOMBIE -> wall ? Blocks.ZOMBIE_WALL_HEAD : Blocks.ZOMBIE_HEAD;
			case CREEPER -> wall ? Blocks.CREEPER_WALL_HEAD : Blocks.CREEPER_HEAD;
			case PIGLIN -> wall ? Blocks.PIGLIN_WALL_HEAD : Blocks.PIGLIN_HEAD;
			default -> null;
		};
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
		this.lightCandlesWithProjectile(level, state, result, projectile);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (state.getValue(LIGHTING) != Lighting.NONE) {
			this.getParticleOffsets(state, level, pos).forEach(offset -> {
				Vec3 trueOffset = offset.add(pos.getX(), pos.getY(), pos.getZ());
				this.addParticlesAndSound(level, trueOffset.x(), trueOffset.y(), trueOffset.z(), rand, state.getValue(LIGHTING));
			});
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIGHTING, CANDLES);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.SKULL_CANDLE, SkullCandleBlockEntity::tick);
	}

	public enum CandleColors implements StringRepresentable {
		PLAIN(0),
		WHITE(1), LIGHT_GRAY(2), GRAY(3), BLACK(4),
		RED(5), ORANGE(6), YELLOW(7), GREEN(8),
		LIME(9), BLUE(10), CYAN(11), LIGHT_BLUE(12),
		PURPLE(13), MAGENTA(14), PINK(15), BROWN(16);

		private final int value;
		private static final Map<Integer, CandleColors> map = new HashMap<>();

		CandleColors(int value) {
			this.value = value;
		}

		static {
			for (CandleColors color : CandleColors.values()) {
				map.put(color.value, color);
			}
		}

		public static CandleColors colorFromInt(int value) {
			return map.get(value);
		}

		public int getValue() {
			return this.value;
		}

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
