package twilightforest.asm.mixin.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFMobEffects;

import java.util.Optional;

/**
 * Recreates NeoForge's LivingIncomingDamageEvent handling for the frosty
 * effect: freeze damage is reduced and fire removes a frosted level.
 */
@Mixin(LivingEntity.class)
public class LivingHurtMixin {

	@ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private float twilightforest$reduceFrostedDamage(float amount, ServerLevel level, DamageSource source) {
		LivingEntity living = (LivingEntity) (Object) this;
		Optional<MobEffectInstance> frosty = Optional.ofNullable(living.getEffect(TFMobEffects.FROSTY));
		if (frosty.isPresent() && source.is(DamageTypes.FREEZE)) {
			return amount + (float) (frosty.get().getAmplifier() / 2);
		}
		return amount;
	}

	@Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"))
	private void twilightforest$removeFrostedOnFire(ServerLevel level, DamageSource source, float amount, CallbackInfo ci) {
		LivingEntity living = (LivingEntity) (Object) this;
		MobEffectInstance frosty = living.getEffect(TFMobEffects.FROSTY);
		if (frosty != null && !source.is(DamageTypes.FREEZE) && source.is(DamageTypeTags.IS_FIRE)) {
			living.removeEffect(TFMobEffects.FROSTY);
			frosty.amplifier -= 1;
			if (frosty.amplifier >= 0) living.addEffect(frosty);
		}
	}
}
