package twilightforest.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TripleBowItem extends CodexBowItem {

    public TripleBowItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (!(living instanceof Player player)) return;
        ItemStack arrowStack = player.getProjectile(stack);

        int useTicks = this.getUseDuration(stack, player) - timeLeft;
        if (useTicks < 0) return;

        if (arrowStack.isEmpty() && !player.getAbilities().instabuild) return;
        if (arrowStack.isEmpty()) arrowStack = new ItemStack(net.minecraft.world.item.Items.ARROW);

        float power = getPowerForTime(useTicks);
        if (power < 0.1D) return;

        List<ItemStack> drawn = draw(stack, arrowStack, player);
        if (level instanceof ServerLevel sl && !drawn.isEmpty()) {
            this.shoot(sl, player, player.getUsedItemHand(), stack, drawn, power * 2.5F, 1.0F, power == 1.0F, null);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F,
                1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    protected void shoot(ServerLevel level, LivingEntity living, InteractionHand hand, ItemStack stack, List<ItemStack> arrows, float speed, float accuracy, boolean crit, @Nullable LivingEntity target) {
        // Three arrows in a horizontal fan with a small vertical spread.
        float fanStep = arrows.size() == 1 ? 0.0F : 20.0F / (float) (arrows.size() - 1);
        float baseFan = (float) ((arrows.size() - 1) % 2) * fanStep / 2.0F;
        float sign = 1.0F;

        for (int i = 0; i < arrows.size(); i++) {
            ItemStack ammo = arrows.get(i);
            if (ammo.isEmpty()) continue;
            float fanOffset = baseFan + sign * (float) ((i + 1) / 2) * fanStep;
            sign = -sign;
            stack.hurtAndBreak(this.getDurabilityUse(ammo), living,
                    LivingEntity.getSlotForHand(hand));

            for (int j = -1; j < 2; j++) {
                ItemStack copy = ammo.copy();
                if (j != 0) copy.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
                Projectile projectile = this.createProjectile(level, living, stack, copy, crit);
                this.shootProjectile(living, projectile, i, speed, accuracy, fanOffset, target);
                projectile.setDeltaMovement(projectile.getDeltaMovement().add(0.0D, 0.15D * j, 0.0D));
                level.addFreshEntity(projectile);
            }
        }
    }
}
