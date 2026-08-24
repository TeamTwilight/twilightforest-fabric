package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.event.ToolEventHooks;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

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

		ThrowableProjectile self = (ThrowableProjectile) (Object) this;
		var canceled = false;

		// ProjectileImpactEvent events go here and need to set canceled...
		canceled |= ToolEventHooks.onEnderBowHit(self, hitResult);
		canceled |= TravellersGearEventHooks.magnetizeArrows(self, hitResult);
		canceled |= TravellersGearEventHooks.performPerfectDodge(self, hitResult);

		return canceled ? HitResult.Type.MISS : type;
	}
}