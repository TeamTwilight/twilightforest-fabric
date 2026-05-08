package twilightforest.item;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

/**
 * Q22/Q25 ported behaviour:
 * <ul>
 *   <li>Right-click a block face → places {@code twilightforest:moonworm} on
 *       the adjacent air position (1 durability cost).</li>
 *   <li>Hold + release with no block under crosshair (ranged mode, Q25) → shoots
 *       a {@link MoonwormShot} projectile that sticks to the first surface it
 *       hits (2 durability cost).</li>
 * </ul>
 */
public class MoonwormQueenItem extends CodexItem {

    public static final int FIRING_TIME = 12;

    public MoonwormQueenItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int useRemaining) {
        int useTime = this.getUseDuration(stack, user) - useRemaining;
        if (level.isClientSide() || useTime <= FIRING_TIME) return;
        if (stack.getDamageValue() + 1 >= stack.getMaxDamage()) return;

        ServerLevel serverLevel = (ServerLevel) level;
        MoonwormShot shot = new MoonwormShot(level, user);
        serverLevel.addFreshEntity(shot);
        level.playSound(null, user.getX(), user.getY(), user.getZ(),
                TFSounds.MOONWORM_SHOOT, user.getSoundSource(), 1.0F, 1.0F);
        if (user instanceof Player p && !p.getAbilities().instabuild && user instanceof ServerPlayer sp) {
            stack.hurtAndBreak(2, serverLevel, sp, removed -> {});
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clicked = context.getClickedPos();
        BlockPos place = level.getBlockState(clicked).canBeReplaced() ? clicked : clicked.relative(context.getClickedFace());
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResult.FAIL;
        }
        if (!level.getBlockState(place).canBeReplaced()) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            level.setBlock(place, TFBlocks.MOONWORM.get().defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, place, TFSounds.MOONWORM_PLACED, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
                stack.hurtAndBreak(1, serverLevel, sp, removed -> {});
            }
        }
        return InteractionResult.SUCCESS;
    }
}
