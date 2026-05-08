package twilightforest.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Q22 ported behaviour for charm_of_life_1 / charm_of_life_2.
 *
 * <p>The actual death-revive logic is wired in {@code CodexTwilight#onInitialize}
 * via {@code ServerLivingEntityEvents.ALLOW_DEATH}: when a player is about to die
 * with a CharmOfLife in inventory or curio slot, the charm is consumed and the
 * player is revived. Tier 1 (charm_of_life_1) revives at 6 hearts; tier 2
 * (charm_of_life_2) revives at full health and grants regeneration.</p>
 *
 * <p>This subclass only marks an item as a charm and exposes its tier so the
 * event handler can disambiguate.</p>
 */
public class CharmOfLifeItem extends CodexItem {

    private final int tier;

    public CharmOfLifeItem(Properties properties, Item fallback, int tier) {
        super(properties, fallback, -1);
        this.tier = tier;
    }

    public int charmTier() {
        return this.tier;
    }

    /**
     * Apply the revive effect to the player. Called from the global death-event
     * handler. Returns true if the charm successfully prevented death.
     */
    public static boolean tryRevive(Player player, DamageSource source, ItemStack charmStack) {
        if (!(charmStack.getItem() instanceof CharmOfLifeItem charm)) return false;
        if (player.level().isClientSide()) return false;

        // Refill health based on tier.
        if (charm.tier == 2) {
            player.setHealth(player.getMaxHealth());
            player.removeAllEffects();
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 6000, 0));
        } else {
            player.setHealth(Math.max(player.getHealth(), 12.0F));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
        }
        // Visual + audio feedback.
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1.0, player.getZ(), 30, 0.5, 0.5, 0.5, 0.0);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        // Consume one charm.
        charmStack.shrink(1);
        return true;
    }

    /** Find a CharmOfLife in the player's inventory; null if none. */
    public static ItemStack findCharm(LivingEntity entity) {
        if (!(entity instanceof Player player)) return ItemStack.EMPTY;
        // Prefer higher-tier charm first.
        ItemStack best = ItemStack.EMPTY;
        int bestTier = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof CharmOfLifeItem charm && charm.tier > bestTier) {
                best = stack;
                bestTier = charm.tier;
            }
        }
        return best;
    }
}
