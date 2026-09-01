package twilightforest.item;

import carminite.interfaces.markers.IContinuousUseItem;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.util.WorldUtil;

import java.util.Map;

public class CrumbleHornItem extends Item implements IContinuousUseItem {
	public static final Map<Block, Pair<Block, Float>> CRUMBLE_HORN = Map.<Block, Pair<Block, Float>>ofEntries(
		Map.entry(Blocks.STONE_BRICKS, Pair.of(Blocks.CRACKED_STONE_BRICKS, 0.2F)),
		Map.entry(Blocks.INFESTED_STONE_BRICKS, Pair.of(Blocks.INFESTED_CRACKED_STONE_BRICKS, 0.2F)),
		Map.entry(Blocks.POLISHED_BLACKSTONE_BRICKS, Pair.of(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.2F)),
		Map.entry(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Pair.of(Blocks.BLACKSTONE, 0.2F)),
		Map.entry(Blocks.NETHER_BRICKS, Pair.of(Blocks.CRACKED_NETHER_BRICKS, 0.2F)),
		Map.entry(Blocks.DEEPSLATE_BRICKS, Pair.of(Blocks.CRACKED_DEEPSLATE_BRICKS, 0.2F)),
		Map.entry(Blocks.DEEPSLATE_TILES, Pair.of(Blocks.CRACKED_DEEPSLATE_TILES, 0.2F)),
		Map.entry(TFBlocks.MAZESTONE_BRICK, Pair.of(TFBlocks.CRACKED_MAZESTONE, 0.2F)),
		Map.entry(TFBlocks.UNDERBRICK, Pair.of(TFBlocks.CRACKED_UNDERBRICK, 0.2F)),
		Map.entry(TFBlocks.DEADROCK, Pair.of(TFBlocks.CRACKED_DEADROCK, 0.2F)),
		Map.entry(TFBlocks.CRACKED_DEADROCK, Pair.of(TFBlocks.WEATHERED_DEADROCK, 0.2F)),
		Map.entry(TFBlocks.TOWERWOOD, Pair.of(TFBlocks.CRACKED_TOWERWOOD, 0.2F)),
		Map.entry(TFBlocks.CASTLE_BRICK, Pair.of(TFBlocks.CRACKED_CASTLE_BRICK, 0.2F)),
		Map.entry(TFBlocks.CRACKED_CASTLE_BRICK, Pair.of(TFBlocks.WORN_CASTLE_BRICK, 0.2F)),
		Map.entry(TFBlocks.NAGASTONE_PILLAR, Pair.of(TFBlocks.CRACKED_NAGASTONE_PILLAR, 0.2F)),
		Map.entry(TFBlocks.ETCHED_NAGASTONE, Pair.of(TFBlocks.CRACKED_ETCHED_NAGASTONE, 0.2F)),
		Map.entry(TFBlocks.CASTLE_BRICK_STAIRS, Pair.of(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS, 0.2F)),
		Map.entry(TFBlocks.NAGASTONE_STAIRS_LEFT, Pair.of(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, 0.2F)),
		Map.entry(TFBlocks.NAGASTONE_STAIRS_RIGHT, Pair.of(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, 0.2F)),
		Map.entry(Blocks.STONE, Pair.of(Blocks.COBBLESTONE, 0.2F)),
		Map.entry(Blocks.COBBLESTONE, Pair.of(Blocks.GRAVEL, 0.2F)),
		Map.entry(Blocks.SANDSTONE, Pair.of(Blocks.SAND, 0.2F)),
		Map.entry(Blocks.RED_SANDSTONE, Pair.of(Blocks.RED_SAND, 0.2F)),
		Map.entry(Blocks.GRASS_BLOCK, Pair.of(Blocks.DIRT, 0.2F)),
		Map.entry(Blocks.PODZOL, Pair.of(Blocks.DIRT, 0.2F)),
		Map.entry(Blocks.MYCELIUM, Pair.of(Blocks.DIRT, 0.2F)),
		Map.entry(Blocks.COARSE_DIRT, Pair.of(Blocks.DIRT, 0.2F)),
		Map.entry(Blocks.ROOTED_DIRT, Pair.of(Blocks.DIRT, 0.2F)),
		Map.entry(Blocks.OXIDIZED_COPPER, Pair.of(Blocks.WEATHERED_COPPER, 0.2F)),
		Map.entry(Blocks.WEATHERED_COPPER, Pair.of(Blocks.EXPOSED_COPPER, 0.2F)),
		Map.entry(Blocks.EXPOSED_COPPER, Pair.of(Blocks.COPPER_BLOCK, 0.2F)),
		Map.entry(Blocks.OXIDIZED_CUT_COPPER, Pair.of(Blocks.WEATHERED_CUT_COPPER, 0.2F)),
		Map.entry(Blocks.WEATHERED_CUT_COPPER, Pair.of(Blocks.EXPOSED_CUT_COPPER, 0.2F)),
		Map.entry(Blocks.EXPOSED_CUT_COPPER, Pair.of(Blocks.CUT_COPPER, 0.2F)),
		Map.entry(Blocks.OXIDIZED_CUT_COPPER_STAIRS, Pair.of(Blocks.WEATHERED_CUT_COPPER_STAIRS, 0.2F)),
		Map.entry(Blocks.WEATHERED_CUT_COPPER_STAIRS, Pair.of(Blocks.EXPOSED_CUT_COPPER_STAIRS, 0.2F)),
		Map.entry(Blocks.EXPOSED_CUT_COPPER_STAIRS, Pair.of(Blocks.CUT_COPPER_STAIRS, 0.2F)),
		Map.entry(Blocks.OXIDIZED_CUT_COPPER_SLAB, Pair.of(Blocks.WEATHERED_CUT_COPPER_SLAB, 0.2F)),
		Map.entry(Blocks.WEATHERED_CUT_COPPER_SLAB, Pair.of(Blocks.EXPOSED_CUT_COPPER_SLAB, 0.2F)),
		Map.entry(Blocks.EXPOSED_CUT_COPPER_SLAB, Pair.of(Blocks.CUT_COPPER_SLAB, 0.2F)),
		Map.entry(Blocks.OXIDIZED_CHISELED_COPPER, Pair.of(Blocks.WEATHERED_CHISELED_COPPER, 0.2F)),
		Map.entry(Blocks.WEATHERED_CHISELED_COPPER, Pair.of(Blocks.EXPOSED_CHISELED_COPPER, 0.2F)),
		Map.entry(Blocks.EXPOSED_CHISELED_COPPER, Pair.of(Blocks.CHISELED_COPPER, 0.2F)),
		Map.entry(Blocks.OXIDIZED_COPPER_GRATE, Pair.of(Blocks.WEATHERED_COPPER_GRATE, 0.2F)),
		Map.entry(Blocks.WEATHERED_COPPER_GRATE, Pair.of(Blocks.EXPOSED_COPPER_GRATE, 0.2F)),
		Map.entry(Blocks.EXPOSED_COPPER_GRATE, Pair.of(Blocks.COPPER_GRATE, 0.2F)),
		Map.entry(Blocks.OXIDIZED_COPPER_BULB, Pair.of(Blocks.WEATHERED_COPPER_BULB, 0.2F)),
		Map.entry(Blocks.WEATHERED_COPPER_BULB, Pair.of(Blocks.EXPOSED_COPPER_BULB, 0.2F)),
		Map.entry(Blocks.EXPOSED_COPPER_BULB, Pair.of(Blocks.COPPER_BULB, 0.2F)),
		Map.entry(Blocks.OXIDIZED_COPPER_TRAPDOOR, Pair.of(Blocks.WEATHERED_COPPER_TRAPDOOR, 0.2F)),
		Map.entry(Blocks.WEATHERED_COPPER_TRAPDOOR, Pair.of(Blocks.EXPOSED_COPPER_TRAPDOOR, 0.2F)),
		Map.entry(Blocks.EXPOSED_COPPER_TRAPDOOR, Pair.of(Blocks.COPPER_TRAPDOOR, 0.2F)),
		Map.entry(Blocks.OXIDIZED_COPPER_DOOR, Pair.of(Blocks.WEATHERED_COPPER_DOOR, 0.2F)),
		Map.entry(Blocks.WEATHERED_COPPER_DOOR, Pair.of(Blocks.EXPOSED_COPPER_DOOR, 0.2F)),
		Map.entry(Blocks.EXPOSED_COPPER_DOOR, Pair.of(Blocks.COPPER_DOOR, 0.2F)),
		Map.entry(Blocks.GRAVEL, Pair.of(Blocks.AIR, 0.05F)),
		Map.entry(Blocks.DIRT, Pair.of(Blocks.AIR, 0.05F)),
		Map.entry(Blocks.SAND, Pair.of(Blocks.AIR, 0.05F)),
		Map.entry(Blocks.RED_SAND, Pair.of(Blocks.AIR, 0.05F)),
		Map.entry(Blocks.CLAY, Pair.of(Blocks.AIR, 0.05F)),
		Map.entry(Blocks.ANDESITE, Pair.of(Blocks.AIR, 0.05F)),
		Map.entry(Blocks.DIORITE, Pair.of(Blocks.AIR, 0.05F)),
		Map.entry(Blocks.GRANITE, Pair.of(Blocks.AIR, 0.05F))
	);


	public CrumbleHornItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		player.playSound(TFSounds.QUEST_RAM_AMBIENT.value(), 1.0F, 0.8F);
		return InteractionResult.CONSUME;
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
		if (count > 10 && count % 5 == 0 && level instanceof ServerLevel serverLevel) {
			this.doCrumble(serverLevel, living, stack);
			serverLevel.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.QUEST_RAM_AMBIENT.value(), living.getSoundSource(), 1.0F, 0.8F);
		}
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.TOOT_HORN;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return oldStack.getItem() == newStack.getItem();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	private void doCrumble(ServerLevel serverLevel, LivingEntity living, ItemStack stack) {
		final double centerDistance = 3.0D;
		final int radius = 2;

		Vec3 eyePosition = living.getEyePosition();
		Vec3 lookVec = living.getLookAngle().scale(centerDistance);
		BlockPos center = BlockPos.containing(eyePosition.add(lookVec));

		AABB crumbleBox = AABB.encapsulatingFullBlocks(center.offset(-radius, -radius, -radius), center.offset(radius - 1, radius - 1, radius - 1));

		this.crumbleBlocksInAABB(serverLevel, living, crumbleBox, stack);
	}

	private void crumbleBlocksInAABB(ServerLevel serverLevel, LivingEntity living, AABB box, ItemStack stack) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			if (this.crumbleBlock(serverLevel, living, pos)) {
				if (living instanceof ServerPlayer player) {
					player.awardStat(TFStats.BLOCKS_CRUMBLED);
				}
				stack.hurtAndBreak(1, living,living.getUsedItemHand());
				if (stack.getDamageValue() >= stack.getMaxDamage()) break;
			}
		}
	}

	private boolean crumbleBlock(ServerLevel serverLevel, LivingEntity living, BlockPos pos) {
		BlockState state = serverLevel.getBlockState(pos);
		Block block = state.getBlock();
		Pair<Block, Float> crumbleMap = CRUMBLE_HORN.get(block);

		if (state.isAir() || crumbleMap == null) return false;

		if (living instanceof Player player) {
			if (!PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(serverLevel, player, pos, state, serverLevel.getBlockEntity(pos))) {
				return false;
			}
		}

		if (crumbleMap.getFirst() == Blocks.AIR) {
			if (serverLevel.getRandom().nextFloat() < crumbleMap.getSecond()) {
				if (living instanceof Player player) {
					if (block.carminite$canHarvestBlock(state, serverLevel, pos, player)) {
						serverLevel.removeBlock(pos, false);
						block.playerDestroy(serverLevel, player, pos, state, serverLevel.getBlockEntity(pos), ItemStack.EMPTY);
						serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
						if (player instanceof ServerPlayer) {
							player.awardStat(Stats.ITEM_USED.get(this));
						}
						return true;
					}
				} else if (serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)) {
					serverLevel.destroyBlock(pos, true);
					return true;
				}
			}
		} else {
			if (serverLevel.getRandom().nextFloat() < crumbleMap.getSecond()) {
				serverLevel.setBlock(pos, crumbleMap.getFirst().withPropertiesOf(state), Block.UPDATE_ALL);
				serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				if (living instanceof ServerPlayer player) {
					player.awardStat(Stats.ITEM_USED.get(this));
				}
				return true;
			}
		}
		return false;
	}
}