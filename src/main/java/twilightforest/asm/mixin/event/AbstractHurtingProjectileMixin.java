package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.event.ToolEventHooks;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

@Mixin(AbstractHurtingProjectile.class)
public class AbstractHurtingProjectileMixin {

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
		Operation<HitResult.Type> original,
		@Share(value = "impacted", namespace = "twilightforest") LocalBooleanRef impacted
	) {
		HitResult.Type type = original.call(hitResult);

		if (type == HitResult.Type.MISS) {
			impacted.set(false);
			return type;
		}

		AbstractHurtingProjectile self = (AbstractHurtingProjectile) (Object) this;
		var canceled = false;

		// ProjectileImpactEvent events go here and need to set canceled...
		canceled |= ToolEventHooks.onEnderBowHit(self, hitResult);
		canceled |= TravellersGearEventHooks.magnetizeArrows(self, hitResult);
		canceled |= TravellersGearEventHooks.performPerfectDodge(self, hitResult);


		impacted.set(!canceled);
		return type;
	}

	@WrapOperation(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		)
	)
	private ProjectileDeflection twilightforest$allowProjectileImpact(
		AbstractHurtingProjectile projectile,
		HitResult hitResult,
		Operation<ProjectileDeflection> original,
		@Share(value = "impacted", namespace = "carminite") LocalBooleanRef impacted
	) {
		if (!impacted.get()) {
			return ProjectileDeflection.NONE;
		}

		return original.call(projectile, hitResult);
	}
}