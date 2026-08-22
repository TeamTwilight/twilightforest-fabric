package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.hooks.EventHooks;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
	public FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@WrapOperation(
		method = "checkCollision()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/FishingHook;hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"
		)
	)
	private ProjectileDeflection twilightforest$projectileImpact(
		FishingHook instance, HitResult hitResult, Operation<ProjectileDeflection> original
	) {
		if (hitResult.getType() == HitResult.Type.MISS || !EventHooks.onProjectileImpact(instance, hitResult)) {
			this.onHit(hitResult);
		}
		return ProjectileDeflection.NONE;
	}
}