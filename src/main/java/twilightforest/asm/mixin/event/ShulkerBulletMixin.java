package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.event.ToolEventHooks;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {

	@WrapOperation(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/ShulkerBullet;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		)
	)
	private ProjectileDeflection twilightforest$projectileImpact(
		ShulkerBullet instance,
		HitResult hitResult,
		Operation<ProjectileDeflection> original
	) {
		var canceled = false;

		// ProjectileImpactEvent events go here and need to set canceled...
		canceled |= ToolEventHooks.onEnderBowHit(instance, hitResult);
		canceled |= TravellersGearEventHooks.magnetizeArrows(instance, hitResult);
		canceled |= TravellersGearEventHooks.performPerfectDodge(instance, hitResult);

		if (canceled) {
			return ProjectileDeflection.NONE;
		}

		return original.call(instance, hitResult);
	}
}