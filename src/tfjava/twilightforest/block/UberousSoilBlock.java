package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 1:1 port of upstream {@code twilightforest.block.UberousSoilBlock} — soil that
 * accelerates BonemealableBlock plants directly above it (turning to dirt/farmland/
 * mycelium/grass on growth) and self-spreads via bonemeal.
 *
 * <p>Codex Fabric port note: the upstream class uses NeoForge-only symbols
 * ({@code FakePlayerFactory.getMinecraft}, {@code BoneMealItem.applyBonemeal} with a
 * fake-player parameter, {@code TriState canSustainPlant}). Codex doesn't have a
 * fake-player factory, so we replace the 15× bonemeal application with a direct
 * {@link BonemealableBlock#performBonemeal} loop — this preserves the gameplay effect
 * (15 cycles of growth) without needing a Player object. The upstream crop-sustain hook
 * is restored on Fabric by {@code twilightforest.mixin.BushBlockMixin}, which lets
 * {@code #minecraft:crops} survive on Uberous Soil long enough for this block to
 * convert itself to wet farmland and trigger growth.</p>
 */
public class UberousSoilBlock extends Block implements BonemealableBlock {

	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public UberousSoilBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
		return state.isSolid() && !(state.getBlock() instanceof BonemealableBlock && !state.is(this)) ? Blocks.DIRT.defaultBlockState() : super.getStateForPlacement(ctx);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (!newState.is(state.getBlock()) && !level.getBlockState(pos.above()).isAir()) this.neighborChanged(state, level, pos, this, pos.above(), moving);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (fromPos.getY() == pos.getY() + 1) {
			BlockState above = level.getBlockState(fromPos);
			if (!(above.getBlock() instanceof BonemealableBlock bonemealableBlock && !above.is(this))) {
				if (above.isSolid()) FarmBlock.turnToDirt(null, state, level, pos);
				return;
			}

			BlockState newState = Blocks.DIRT.defaultBlockState();

			if (above.is(BlockTags.CROPS))
				newState = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7);
			else if (bonemealableBlock instanceof MushroomBlock)
				newState = Blocks.MYCELIUM.defaultBlockState();
			else if (bonemealableBlock instanceof BushBlock)
				newState = Blocks.GRASS_BLOCK.defaultBlockState();
			else if (bonemealableBlock instanceof MossBlock mossBlock)
				newState = mossBlock.defaultBlockState();

			if (level instanceof ServerLevel serverLevel) {
				if (bonemealableBlock instanceof MushgloomBlock mushgloomBlock) {
					// Mushgloom is intentionally only growable by uberous soil — keep this
					// special-case path matching upstream.
					level.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, level, pos));
					mushgloomBlock.growMushroom(serverLevel, fromPos, above, serverLevel.random);
					level.levelEvent(2005, fromPos, 0); // Bonemeal particles client-side hint
					return;
				}
				level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, fromPos, 15);
			}

			// The block must be set to a new one before we attempt to bonemeal the plant,
			// otherwise we can end up with an infinite block update loop (e.g. mushroom
			// growth fails into a block update if there isn't enough room).
			level.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, level, pos));

			if (level instanceof ServerLevel serverLevel) {
				applyAutoBonemeal(serverLevel, fromPos);
			}

			level.levelEvent(2005, fromPos, 0);
		} else if (fromPos.getY() + 1 == pos.getY()) {
			BlockState below = level.getBlockState(fromPos);
			if (!(below.getBlock() instanceof BonemealableBlock)) return;

			level.setBlockAndUpdate(pos, pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), level, pos));

			if (level instanceof ServerLevel serverLevel) {
				applyAutoBonemeal(serverLevel, fromPos);
				level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, fromPos, 15);
			}

			level.levelEvent(2005, fromPos, 0);
		}
	}

	/**
	 * Codex Fabric stand-in for upstream's
	 * {@code for (int i=0; i<15; i++) BoneMealItem.applyBonemeal(stack, level, pos, fakePlayer)}.
	 * We can't get a FakePlayer on Fabric without a 3rd-party mod, so call the underlying
	 * {@link BonemealableBlock#performBonemeal} 15× directly — same gameplay effect.
	 */
	private static void applyAutoBonemeal(ServerLevel level, BlockPos pos) {
		for (int i = 0; i < 15; i++) {
			BlockState state = level.getBlockState(pos);
			if (state.getBlock() instanceof BonemealableBlock bonemealable
				&& bonemealable.isValidBonemealTarget(level, pos, state)
				&& bonemealable.isBonemealSuccess(level, level.random, pos, state)) {
				bonemealable.performBonemeal(level, level.random, pos, state);
			} else {
				break;
			}
		}
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	// Check each side of the block, as well as above and below each of those positions
	// for valid spots.
	public boolean isValidBonemealTarget(LevelReader getter, BlockPos pos, BlockState state) {
		for (Direction dir : Direction.values()) {
			if (dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = getter.getBlockState(pos.relative(dir));
				if (
					!getter.getBlockState(pos.relative(dir).above()).isSolid() &&
						(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
						!blockAt.is(twilightforest.init.TFBlocks.UBEROUS_SOIL.get())) {
					return true;

				} else if (
					!getter.getBlockState(pos.relative(dir).above().above()).isSolid() &&
						(getter.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || getter.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
						!getter.getBlockState(pos.relative(dir).above()).is(twilightforest.init.TFBlocks.UBEROUS_SOIL.get())) {
					return true;

				} else if (
					!getter.getBlockState(pos.relative(dir)).isSolid() &&
						(getter.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || getter.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
						!getter.getBlockState(pos.relative(dir).below()).is(twilightforest.init.TFBlocks.UBEROUS_SOIL.get())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	// Check each side of the block, as well as above and below each of those positions
	// to check for a place to put a block. The above and below checks allow the patch to
	// jump to a new y-level — easier spreading.
	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);
		for (Direction dir : directions) {
			if (dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = level.getBlockState(pos.relative(dir));
				if (
					!level.getBlockState(pos.relative(dir).above()).isSolid() &&
						(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
						!blockAt.is(twilightforest.init.TFBlocks.UBEROUS_SOIL.get())) {

					this.spreadTo(level, pos.relative(dir));
					break;
				} else if (
					!level.getBlockState(pos.relative(dir).above().above()).isSolid() &&
						(level.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || level.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
						!level.getBlockState(pos.relative(dir).above()).is(twilightforest.init.TFBlocks.UBEROUS_SOIL.get())) {

					this.spreadTo(level, pos.relative(dir).above());
					break;
				} else if (
					!level.getBlockState(pos.relative(dir)).isSolid() &&
						(level.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || level.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
						!level.getBlockState(pos.relative(dir).below()).is(twilightforest.init.TFBlocks.UBEROUS_SOIL.get())) {

					this.spreadTo(level, pos.relative(dir).below());
					break;
				}
			}
		}
	}

	public void spreadTo(ServerLevel level, BlockPos pos) {
		level.setBlockAndUpdate(pos, this.defaultBlockState());
		if (!level.getBlockState(pos.above()).isAir()) this.neighborChanged(this.defaultBlockState(), level, pos, this, pos.above(), false);
	}
}
