package twilightforest.asm.mixin;

import carminite.interfaces.extensions.IEntityExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Recreates NeoForge's ProjectileUtil patch: projectiles fired by a rider of
 * an entity whose canRiderInteract() returns true (e.g. the twilight mounts)
 * do not hit that rider.
 */
@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {

	@Redirect(
		method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getRootVehicle()Lnet/minecraft/world/entity/Entity;", ordinal = 0)
	)
	private static Entity twilightforest$skipRiderTarget(Entity entity) {
		return ((IEntityExtension) entity).canRiderInteract() ? null : entity.getRootVehicle();
	}
}
