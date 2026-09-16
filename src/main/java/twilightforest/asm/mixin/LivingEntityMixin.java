package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asm.hooks.event.EntityEventHooks;
import twilightforest.asm.hooks.event.ToolEventHooks;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(
		method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Math;max(FF)F"
		)
	)
	private void twilightforest$captureOriginalDamage(
		ServerLevel level,
		DamageSource source,
		float dmg,
		CallbackInfo ci,
		@Local(name = "originalDamage") float originalDamage,
		@Share(value = "capturedOriginal", namespace = "twilightforest") LocalFloatRef capturedOriginal
	) {
		capturedOriginal.set(originalDamage);
	}

	@ModifyVariable(
		method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V",
		at = @At(
			value = "LOAD",
			ordinal = 2
		),
		name = "dmg",
		argsOnly = true
	)
	private float twilightforest$modifyDamage(
		float dmg,
		@Local(argsOnly = true, name = "source") DamageSource source,
		@Share(value = "capturedOriginal", namespace = "twilightforest") LocalFloatRef capturedOriginal
	) {
		LivingEntity target = (LivingEntity) (Object) this;

		dmg = ToolEventHooks.doKnightmetalToolLogic(
			target,
			source,
			capturedOriginal.get(),
			dmg
		);

		dmg = ToolEventHooks.addExtraAxeChargingDamage(
			target,
			source,
			dmg
		);

		dmg = EntityEventHooks.reduceFrostedEffectIfOnFire(
			target,
			source,
			capturedOriginal.get(),
			dmg
		);

		return dmg;
	}
}