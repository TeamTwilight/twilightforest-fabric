package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.block.HarvestableBlock;
import io.github.fabricators_of_create.porting_lib.item.ContinueUsingItem;
import io.github.fabricators_of_create.porting_lib.item.ReequipAnimationItem;
import io.github.fabricators_of_create.porting_lib.item.UsingTickItem;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.util.WorldUtil;

import java.util.concurrent.atomic.AtomicBoolean;

public class CrumbleHornItem extends Item implements ReequipAnimationItem, ContinueUsingItem, UsingTickItem {

	private static final int CHANCE_HARVEST = 20;
	private static final int CHANCE_CRUMBLE = 5;

	public CrumbleHornItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		player.playSound(TFSounds.QUEST_RAM_AMBIENT.get(), 1.0F, 0.8F);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
		if (count > 10 && count % 5 == 0 && !living.level().isClientSide()) {
			int crumbled = doCrumble(living.level(), living);

			if (crumbled > 0) {
				stack.hurtAndBreak(crumbled, living, (user) -> user.broadcastBreakEvent(living.getUsedItemHand()));
			}

			living.level().playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.QUEST_RAM_AMBIENT.get(), living.getSoundSource(), 1.0F, 0.8F);
		}
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
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

	private int doCrumble(Level world, LivingEntity living) {

		final double range = 3.0D;
		final double radius = 2.0D;

		Vec3 srcVec = new Vec3(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
		Vec3 lookVec = living.getLookAngle().scale(range);
		Vec3 destVec = srcVec.add(lookVec);

		AABB crumbleBox = new AABB(destVec.x() - radius, destVec.y() - radius, destVec.z() - radius, destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);

		return crumbleBlocksInAABB(world, living, crumbleBox);
	}

	private int crumbleBlocksInAABB(Level world, LivingEntity living, AABB box) {
		int crumbled = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			if (crumbleBlock(world, living, pos)) {
				crumbled++;
				if (living instanceof Player player && player instanceof ServerPlayer) {
					player.awardStat(TFStats.BLOCKS_CRUMBLED.get());
				}
			}
		}
		return crumbled;
	}

	private boolean crumbleBlock(Level world, LivingEntity living, BlockPos pos) {

		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		AtomicBoolean flag = new AtomicBoolean(false);

		if (state.isAir()) return false;

		if (living instanceof Player) {
			if (!PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, (Player) living, pos, state, null))
				return false;
		}

		if (world instanceof ServerLevel level) {
			level.getRecipeManager().getAllRecipesFor(TFRecipes.CRUMBLE_RECIPE.get()).forEach(recipe -> {
				if (flag.get()) return;
				if (recipe.result().is(Blocks.AIR)) {
					if (recipe.input().is(block) && world.getRandom().nextInt(CHANCE_HARVEST) == 0 && !flag.get()) {
						if (living instanceof Player) {
							if ((block instanceof HarvestableBlock harvestableBlock && harvestableBlock.canHarvestBlock(state, world, pos, (Player) living)) || ((Player) living).hasCorrectToolForDrops(state)) {
								world.removeBlock(pos, false);
								block.playerDestroy(world, (Player) living, pos, state, world.getBlockEntity(pos), ItemStack.EMPTY);
								world.levelEvent(2001, pos, Block.getId(state));
								postTrigger(living);
								flag.set(true);
							}
						} else if (world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
							world.destroyBlock(pos, true);
							postTrigger(living);
							flag.set(true);
						}
					}
				} else {
					if (recipe.input().is(block) && world.getRandom().nextInt(CHANCE_CRUMBLE) == 0 && !flag.get()) {
						world.setBlock(pos, recipe.result().getBlock().withPropertiesOf(state), 3);
						world.levelEvent(2001, pos, Block.getId(state));
						postTrigger(living);
						flag.set(true);
					}
				}
			});
		}

		return flag.get();
	}

	private void postTrigger(LivingEntity living) {
		if (living instanceof ServerPlayer) {
			Player player = (Player) living;
			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}
}