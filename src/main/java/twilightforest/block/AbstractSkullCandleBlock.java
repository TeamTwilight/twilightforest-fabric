package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.tileentity.SkullCandleTileEntity;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.function.ToIntFunction;

//The nastiest mash-up of AbstractSkullBLock and AbstractCandleBlock you will ever see. Oh yeah, some things in here are mine too. I dont copy everything.
public abstract class AbstractSkullCandleBlock extends BaseEntityBlock {

	private final SkullBlock.Type type;

	public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final ToIntFunction<BlockState> LIGHT_EMISSION = (state) -> state.getValue(LIT) ? 3 * state.getValue(CANDLES) : 0;
	public static final EnumProperty<CandleColors> COLOR = EnumProperty.create("color", CandleColors.class);

	public AbstractSkullCandleBlock(SkullBlock.Type type, Properties properties) {
		super(properties);
		this.type = type;
		registerDefaultState(getStateDefinition().any().setValue(LIT, false).setValue(CANDLES, 1).setValue(COLOR, CandleColors.PLAIN));
	}

	public SkullBlock.Type getType() {
		return type;
	}

	protected abstract Iterable<Vec3> getParticleOffsets(BlockState var1);

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SkullCandleTileEntity(blockPos, blockState);
	}

	//input one of the enum names to convert it into a candle block
	public static Block candleColorToCandle(String candleName) {
		if(!candleName.equals(CandleColors.PLAIN.getSerializedName())) {
			return Objects.requireNonNull(Registry.BLOCK.get(new ResourceLocation(candleName + "_candle")));
		}
		return Blocks.CANDLE;
	}

	//inverse of above
	public static CandleColors candleToCandleColor(Item candle) {
		if(!(candle == Blocks.CANDLE.asItem())) {
			return CandleColors.valueOf(Registry.ITEM.getKey(candle).getPath().replace("_candle", "").replace("\"", "").toUpperCase(Locale.ROOT));
		}
		return CandleColors.PLAIN;
	}

	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
		if (!world.isClientSide && !player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			//if we have silk touch, assign the candle values to the item and drop it
			if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
				ItemStack newStack = new ItemStack(this);
				ItemEntity itementity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newStack);
				CompoundTag tag = new CompoundTag();
				tag.putString("color", state.getValue(COLOR).getSerializedName());
				tag.putInt("candles", state.getValue(CANDLES));
				newStack.addTagElement("BlockStateTag", tag);
				itementity.setDefaultPickUpDelay();
				world.addFreshEntity(itementity);
				//otherwise lets drop the skull and candles
			} else {
				//skull is handled via loot table
				ItemStack newStack = new ItemStack(candleColorToCandle(state.getValue(COLOR).getSerializedName()));
				for(int i = 0; i < state.getValue(CANDLES); i++) {
					ItemEntity itementity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newStack);
					world.addFreshEntity(itementity);
				}
			}
		}
		super.playerDestroy(world, player, pos, state, entity, stack);
	}

	//TODO: Port
	//@Override
	public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		ItemStack newStack = new ItemStack(this);
		CompoundTag tag = new CompoundTag();
		tag.putString("color", state.getValue(COLOR).getSerializedName());
		tag.putInt("candles", state.getValue(CANDLES));
		newStack.addTagElement("BlockStateTag", tag);
		return newStack;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (player.getAbilities().mayBuild && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {

			extinguish(player, state, level, pos);
			return InteractionResult.sidedSuccess(level.isClientSide);

		} else if (player.getItemInHand(hand).is(ItemTags.CANDLES)
				&& player.getItemInHand(hand).is(candleColorToCandle(state.getValue(COLOR).getSerializedName()).asItem())
				&& state.getValue(CANDLES) < 4 && !player.isShiftKeyDown()) {

			level.setBlockAndUpdate(pos, state.setValue(CANDLES, state.getValue(CANDLES) + 1));
			if (!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);
			return InteractionResult.sidedSuccess(level.isClientSide);

		} else if (!state.getValue(LIT)){
			if(player.getItemInHand(hand).is(Items.FLINT_AND_STEEL)) {
				setLit(level, state, pos, true);
				level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				if(!player.getAbilities().instabuild) player.getItemInHand(hand).hurtAndBreak(1, player, (res) -> {
					res.broadcastBreakEvent(hand);
				});
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else if (player.getItemInHand(hand).is(Items.FIRE_CHARGE)) {
				setLit(level, state, pos, true);
				level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				if(!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
		if (!level.isClientSide && projectile.isOnFire() && this.canBeLit(state)) {
			setLit(level, state, result.getBlockPos(), true);
		}
	}

	protected boolean canBeLit(BlockState state) {
		return !state.getValue(LIT);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if (state.getValue(LIT)) {
			this.getParticleOffsets(state).forEach((offset) ->
					addParticlesAndSound(level, offset.add(pos.getX(), pos.getY(), pos.getZ()), rand));
		}
	}

	protected static void addParticlesAndSound(Level level, Vec3 vec, Random rand) {
		float var3 = rand.nextFloat();
		if (var3 < 0.3F) {
			level.addParticle(ParticleTypes.SMOKE, vec.x, vec.y, vec.z, 0.0D, 0.0D, 0.0D);
			if (var3 < 0.17F) {
				level.playLocalSound(vec.x + 0.5D, vec.y + 0.5D, vec.z + 0.5D, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
			}
		}

		level.addParticle(ParticleTypes.SMALL_FLAME, vec.x, vec.y, vec.z, 0.0D, 0.0D, 0.0D);
	}

	public static void extinguish(@Nullable Player player, BlockState state, LevelAccessor accessor, BlockPos pos) {
		setLit(accessor, state, pos, false);
		if (state.getBlock() instanceof AbstractCandleBlock) {
			((AbstractSkullCandleBlock)state.getBlock()).getParticleOffsets(state).forEach((p_151926_) ->
					accessor.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + p_151926_.x(), (double)pos.getY() + p_151926_.y(), (double)pos.getZ() + p_151926_.z(), 0.0D, 0.1D, 0.0D));
		}

		accessor.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
		accessor.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
	}

	private static void setLit(LevelAccessor accessor, BlockState state, BlockPos pos, boolean lit) {
		accessor.setBlock(pos, state.setValue(LIT, lit), 11);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CANDLES, LIT, COLOR);
	}

	public enum CandleColors implements StringRepresentable {
		WHITE, LIGHT_GRAY, GRAY, BLACK,
		RED, ORANGE, YELLOW, GREEN,
		LIME, BLUE, CYAN, LIGHT_BLUE,
		PURPLE, MAGENTA, PINK, BROWN,
		PLAIN;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
