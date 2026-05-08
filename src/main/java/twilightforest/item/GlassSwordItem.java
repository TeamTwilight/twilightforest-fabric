package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;

import java.util.function.Consumer;

public class GlassSwordItem extends CodexSwordItem {

    private static final BlockParticleOption GLASS_PARTICLE =
            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_STAINED_GLASS.defaultBlockState());

    public GlassSwordItem(Tier tier, Properties properties, Item fallback, int cmd) {
        super(tier, properties, fallback, cmd);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.level() instanceof ServerLevel sl) {
            for (int i = 0; i < 20; i++) {
                double dx = target.getX() + target.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
                double dy = target.getY() + target.getRandom().nextFloat() * target.getBbHeight();
                double dz = target.getZ() + target.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
                sl.sendParticles(GLASS_PARTICLE, dx, dy, dz, 1, 0, 0, 0, 0);
            }
        }
        this.hurtAndBreak(stack, attacker, user -> {
            user.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                    TFSounds.GLASS_SWORD_BREAK, attacker.getSoundSource(), 1.0F, 0.5F);
            user.onEquippedItemBroken(this, EquipmentSlot.MAINHAND);
        });
        return true;
    }

    private <T extends LivingEntity> void hurtAndBreak(ItemStack stack, T entity, Consumer<T> onBroken) {
        if (!entity.level().isClientSide() && (!(entity instanceof Player player) || !player.getAbilities().instabuild)) {
            if (this.hurt(stack, entity instanceof ServerPlayer sp ? sp : null)) {
                onBroken.accept(entity);
                stack.shrink(1);
                if (entity instanceof Player player) {
                    player.awardStat(Stats.ITEM_BROKEN.get(this));
                }
            }
        }
    }

    private boolean hurt(ItemStack stack, @Nullable ServerPlayer player) {
        if (stack.get(TFDataComponents.INFINITE_GLASS_SWORD) != null) {
            return false;
        }

        if (player != null) {
            if (EnchantmentHelper.processDurabilityChange(player.serverLevel(), stack, 1) <= 0) {
                return false;
            }

            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, stack, 0);
            TFAdvancements.BROKE_GLASS_SWORD.get().trigger(player);
        }

        return true;
    }
}
