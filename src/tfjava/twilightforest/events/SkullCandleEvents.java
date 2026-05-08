package twilightforest.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStats;

public final class SkullCandleEvents {
	private SkullCandleEvents() {
	}

	public static void bootstrap() {
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (TFConfig.disableSkullCandles || player.isShiftKeyDown()) {
				return InteractionResult.PASS;
			}
			ItemStack stack = player.getItemInHand(hand);
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = level.getBlockState(pos);
			if (!stack.is(ItemTags.CANDLES) || !"minecraft".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace())) {
				return InteractionResult.PASS;
			}
			if (!(state.getBlock() instanceof AbstractSkullBlock skull) || !"minecraft".equals(BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace())) {
				return InteractionResult.PASS;
			}
			Block newBlock = blockFor((SkullBlock.Types) skull.getType(), state.getBlock() instanceof WallSkullBlock);
			if (newBlock == null) {
				return InteractionResult.PASS;
			}
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			makeSkullCandle(level, pos, state, stack, newBlock);
			stack.consume(1, player);
			player.swing(hand);
			if (player instanceof ServerPlayer serverPlayer) {
				serverPlayer.awardStat(TFStats.SKULL_CANDLES_MADE);
			}
			return InteractionResult.CONSUME;
		});
	}

	private static Block blockFor(SkullBlock.Types type, boolean wall) {
		return switch (type) {
			case SKELETON -> wall ? TFBlocks.SKELETON_WALL_SKULL_CANDLE.get() : TFBlocks.SKELETON_SKULL_CANDLE.get();
			case WITHER_SKELETON -> wall ? TFBlocks.WITHER_SKELETON_WALL_SKULL_CANDLE.get() : TFBlocks.WITHER_SKELETON_SKULL_CANDLE.get();
			case PLAYER -> wall ? TFBlocks.PLAYER_WALL_SKULL_CANDLE.get() : TFBlocks.PLAYER_SKULL_CANDLE.get();
			case ZOMBIE -> wall ? TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get() : TFBlocks.ZOMBIE_SKULL_CANDLE.get();
			case CREEPER -> wall ? TFBlocks.CREEPER_WALL_SKULL_CANDLE.get() : TFBlocks.CREEPER_SKULL_CANDLE.get();
			case PIGLIN -> wall ? TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get() : TFBlocks.PIGLIN_SKULL_CANDLE.get();
			default -> null;
		};
	}

	private static void makeSkullCandle(Level level, BlockPos pos, BlockState oldState, ItemStack candleStack, Block newBlock) {
		ResolvableProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull) {
			profile = skull.getOwnerProfile();
		}
		level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		BlockState newState = newBlock.withPropertiesOf(oldState)
			.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE);
		if (newState.hasProperty(AbstractSkullCandleBlock.CANDLES)) {
			newState = newState.setValue(AbstractSkullCandleBlock.CANDLES, 1);
		}
		level.setBlockAndUpdate(pos, newState);
		SkullCandleBlockEntity be = new SkullCandleBlockEntity(pos, newState, AbstractSkullCandleBlock.candleToCandleColor(candleStack.getItem()).getValue());
		level.setBlockEntity(be);
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			sc.setOwner(profile);
		}
	}
}
