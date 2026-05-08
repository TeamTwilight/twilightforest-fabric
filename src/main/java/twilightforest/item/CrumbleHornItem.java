package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDataMaps;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.util.WorldUtil;

public class CrumbleHornItem extends CodexItem {

    public CrumbleHornItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        player.playSound(TFSounds.QUEST_RAM_AMBIENT, 1.0F, 0.8F);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        if (count > 10 && count % 5 == 0 && level instanceof ServerLevel serverLevel) {
            this.doCrumble(serverLevel, living, stack);
            serverLevel.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.QUEST_RAM_AMBIENT, living.getSoundSource(), 1.0F, 0.8F);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
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
                stack.hurtAndBreak(1, living, LivingEntity.getSlotForHand(living.getUsedItemHand()));
                if (stack.getDamageValue() >= stack.getMaxDamage()) {
                    break;
                }
            }
        }
    }

    private boolean crumbleBlock(ServerLevel serverLevel, LivingEntity living, BlockPos pos) {
        BlockState state = serverLevel.getBlockState(pos);
        if (state.isAir() || TFDataMaps.getCrumbleHorn(state) == null) {
            return false;
        }

        BlockState replacement = TFDataMaps.getCrumbleHornResult(state, serverLevel.getRandom());
        if (replacement == null) {
            return false;
        }

        if (replacement.isAir()) {
            if (living instanceof Player player) {
                if (!serverLevel.mayInteract(player, pos) || !player.hasCorrectToolForDrops(state)) {
                    return false;
                }
                Block block = state.getBlock();
                serverLevel.removeBlock(pos, false);
                block.playerDestroy(serverLevel, player, pos, state, serverLevel.getBlockEntity(pos), ItemStack.EMPTY);
                serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(Stats.ITEM_USED.get(this));
                }
                return true;
            }

            if (serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                serverLevel.destroyBlock(pos, true);
                return true;
            }
            return false;
        }

        serverLevel.setBlock(pos, replacement, Block.UPDATE_ALL);
        serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        if (living instanceof ServerPlayer player) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return true;
    }
}
