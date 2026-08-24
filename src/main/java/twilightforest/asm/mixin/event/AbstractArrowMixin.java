package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asm.hooks.event.ToolEventHooks;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

	@Inject(
		method = "stepMoveAndHit(Lnet/minecraft/world/phys/BlockHitResult;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		),
		cancellable = true
	)
	private void twilightforest$projectileImpactBlock(
		BlockHitResult blockHitResult,
		CallbackInfo ci
	) {
		AbstractArrow self = (AbstractArrow) (Object) this;
		var canceled = false;

		// ProjectileImpactEvent events go here and need to set canceled...
		canceled |= ToolEventHooks.onEnderBowHit(self, blockHitResult);
		canceled |= TravellersGearEventHooks.magnetizeArrows(self, blockHitResult);
		canceled |= TravellersGearEventHooks.performPerfectDodge(self, blockHitResult);

		if (canceled) {
			ci.cancel();
		}
	}

	@Inject(
		method = "stepMoveAndHit(Lnet/minecraft/world/phys/BlockHitResult;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;hitTargetsOrDeflectSelf(Ljava/util/Collection;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		),
		cancellable = true
	)
	private void twilightforest$projectileImpactEntity(
		BlockHitResult blockHitResult,
		CallbackInfo ci,
		@Local(name = "firstEntityHit") EntityHitResult firstEntityHit
	) {
		if (firstEntityHit.getType() == HitResult.Type.MISS) {
			return;
		}

		AbstractArrow self = (AbstractArrow) (Object) this;
		var canceled = false;

		// ProjectileImpactEvent events go here and need to set canceled...
		canceled |= ToolEventHooks.onEnderBowHit(self, blockHitResult);
		canceled |= TravellersGearEventHooks.magnetizeArrows(self, blockHitResult);
		canceled |= TravellersGearEventHooks.performPerfectDodge(self, blockHitResult);

		if (canceled) {
			ci.cancel();
		}
	}
}