package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.event.ToolEventHooks;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

@Mixin(LlamaSpit.class)
public class LlamaSpitMixin {

	@WrapOperation(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/LlamaSpit;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		)
	)
	private ProjectileDeflection twilightforest$projectileImpact(
		LlamaSpit instance,
		HitResult hitResult,
		Operation<ProjectileDeflection> original
	) {
		if (hitResult.getType() == HitResult.Type.MISS) {
			return ProjectileDeflection.NONE;
		}

		var canceled = false;

		// ProjectileImpactEvent events go here and need to set canceled...
		canceled |= ToolEventHooks.onEnderBowHit(instance, hitResult);
		canceled |= TravellersGearEventHooks.magnetizeArrows(instance, hitResult);
		canceled |= TravellersGearEventHooks.performPerfectDodge(instance, hitResult);

		if (!canceled) {
			return original.call(instance, hitResult);
		}

		return ProjectileDeflection.NONE;
	}
}