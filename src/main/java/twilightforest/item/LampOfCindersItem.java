package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class LampOfCindersItem extends CodexItem {

    private static final int FIRING_TIME = 12;

    public LampOfCindersItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
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
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (this.burnBlock(level, pos)) {
            if (player != null) {
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, context.getItemInHand());
                }
                player.playSound(TFSounds.LAMP_BURN, 0.5F, 1.5F);
            }
            for (int i = 0; i < 10; i++) {
                double dx = pos.getX() + 0.5D + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.75D;
                double dy = pos.getY() + 0.5D + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.75D;
                double dz = pos.getZ() + 0.5D + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.75D;
                if (level instanceof ServerLevel sl) {
                    sl.sendParticles(ParticleTypes.SMOKE, dx, dy, dz, 1, 0.0, 0.0, 0.0, 0.0);
                    sl.sendParticles(ParticleTypes.FLAME, dx, dy, dz, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
        int useTime = this.getUseDuration(stack, living) - useRemaining;
        if (useTime <= FIRING_TIME) return;
        if (stack.getDamageValue() + 1 >= stack.getMaxDamage()) return;

        BlockPos pos = BlockPos.containing(living.getEyePosition().add(living.getLookAngle().scale(2.0D)));
        int range = 4;

        if (level instanceof ServerLevel sl) {
            sl.playSound(null, living.getX(), living.getY(), living.getZ(),
                    TFSounds.LAMP_BURN, living.getSoundSource(), 1.5F, 0.8F);
            for (int dx = -range; dx <= range; dx++) {
                for (int dy = -range; dy <= range; dy++) {
                    for (int dz = -range; dz <= range; dz++) {
                        this.burnBlock(level, pos.offset(dx, dy, dz));
                    }
                }
            }
            for (int i = 0; i < 18; i++) {
                BlockPos rPos = pos.offset(
                        level.getRandom().nextInt(range) - level.getRandom().nextInt(range),
                        level.getRandom().nextInt(2),
                        level.getRandom().nextInt(range) - level.getRandom().nextInt(range));
                sl.sendParticles(ParticleTypes.FLAME,
                        rPos.getX() + 0.5, rPos.getY() + 0.5, rPos.getZ() + 0.5,
                        4, 0.3, 0.3, 0.3, 0.01);
            }
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.below(2)).inflate(4.0D))) {
                if (!(target instanceof Player)) target.igniteForSeconds(5);
            }
            if (living instanceof ServerPlayer sp && !sp.getAbilities().instabuild) {
                stack.hurtAndBreak(1, sl, sp, removed -> {});
            }
        }
    }

    private boolean burnBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(TFBlocks.BROWN_THORNS.get()) || state.is(TFBlocks.GREEN_THORNS.get())) {
            level.setBlock(pos, TFBlocks.BURNT_THORNS.get().withPropertiesOf(state), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }
}
