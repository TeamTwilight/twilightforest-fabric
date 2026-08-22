package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.hooks.EventHooks;

@Mixin(ShulkerBullet.class)
public abstract class ShulkerBulletMixin extends Entity {
	public ShulkerBulletMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

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
		if (hitResult != null && this.isAlive() && hitResult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(instance, hitResult)) {
			return original.call(instance, hitResult);
		}
		return ProjectileDeflection.NONE;
	}
}