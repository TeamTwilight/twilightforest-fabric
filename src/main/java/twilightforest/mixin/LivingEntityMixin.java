package twilightforest.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.events.EntityEvents;
import twilightforest.events.ToolEvents;
import twilightforest.init.custom.TravellersModifiersManager;

/**
 * Q34 mixin for {@link LivingEntity}: implements two travellers-gear movement
 * modifiers.
 *
 * <ul>
 *   <li><b>Step-Up</b> (boots) — overrides {@code maxUpStep()} to return 1.0625
 *       so the wearer auto-steps onto blocks up to 1 full block tall (vs the
 *       vanilla 0.6).</li>
 *   <li><b>Cushioned Fall</b> (vest) — reduces fall damage by 25% via {@code
 *       calculateFallDamage} (in addition to the Q32 Resistance I).</li>
 * </ul>
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "maxUpStep", at = @At("HEAD"), cancellable = true)
    private void codex_twilight$travellersStepUp(CallbackInfoReturnable<Float> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player)) return;
        if (TravellersModifiersManager.isStepUpActive(self)) {
            cir.setReturnValue(1.0625F);
        }
    }

    @Inject(method = "calculateFallDamage", at = @At("RETURN"), cancellable = true)
    private void codex_twilight$travellersCushion(float distance, float multiplier, CallbackInfoReturnable<Integer> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player)) return;
        if (TravellersModifiersManager.isSlimySolesActive(self)) {
            cir.setReturnValue(0);
            return;
        }
        if (TravellersModifiersManager.isCushionActive(self)) {
            int original = cir.getReturnValueI();
            cir.setReturnValue(Math.max(0, (int) Math.floor(original * 0.75F)));
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void codex_twilight$travellersPerfectDodge(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player)) return;
        if (self.level().isClientSide()) return;
        if (source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
        if (!TravellersModifiersManager.isPerfectDodgeActive(self)) return;
        if (self.getRandom().nextFloat() < 0.12F) {
            self.level().playSound(null, self.getX(), self.getY(), self.getZ(),
                    net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_NODAMAGE,
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.6F, 1.6F);
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float codex_twilight$toolDamageBonuses(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;
        return EntityEvents.modifyIncomingDamage(self, source, ToolEvents.modifyIncomingDamage(self, source, amount));
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void codex_twilight$pocketWatchPreventsFatigue(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (ToolEvents.shouldBlockEffect(self, effectInstance)) {
            cir.setReturnValue(false);
        }
    }
}
