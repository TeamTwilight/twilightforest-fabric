package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.hooks.EventHooks;

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
		if (hitResult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(instance, hitResult)) {
			return original.call(instance, hitResult);
		}
		return ProjectileDeflection.NONE;
	}
}