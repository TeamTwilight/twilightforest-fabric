package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.hooks.EventHooks;

@Mixin(ThrowableProjectile.class)
public class ThrowableProjectileMixin {

	@WrapOperation(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;",
			ordinal = 0
		)
	)
	private HitResult.Type twilightforest$projectileImpact(
		HitResult hitResult,
		Operation<HitResult.Type> original
	) {
		HitResult.Type type = original.call(hitResult);
		if (type == HitResult.Type.MISS) {
			return type;
		}
		return EventHooks.onProjectileImpact((ThrowableProjectile) (Object) this, hitResult) ? HitResult.Type.MISS : type;
	}
}