package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.hooks.EventHooks;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

	@WrapOperation(
		method = "stepMoveAndHit(Lnet/minecraft/world/phys/BlockHitResult;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		)
	)
	private ProjectileDeflection twilightforest$projectileImpactBlock(
		AbstractArrow instance,
		HitResult hitResult,
		Operation<ProjectileDeflection> original
	) {
		if (!EventHooks.onProjectileImpact(instance, hitResult)) {
			return original.call(instance, hitResult);
		}
		return ProjectileDeflection.NONE;
	}

	@Inject(
		method = "stepMoveAndHit(Lnet/minecraft/world/phys/BlockHitResult;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;hitTargetsOrDeflectSelf(Ljava/util/Collection;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		),
		cancellable = true
	)
	private void twilightforest$projectileImpactEntities(
		BlockHitResult blockHitResult,
		CallbackInfo ci,
		@Local(name = "firstEntityHit") EntityHitResult firstEntityHit
	) {
		if (EventHooks.onProjectileImpact((AbstractArrow) (Object) this, firstEntityHit)) {
			ci.cancel();
		}
	}
}