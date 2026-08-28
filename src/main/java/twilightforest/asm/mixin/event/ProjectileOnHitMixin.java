package twilightforest.asm.mixin.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.config.TFConfig;
import twilightforest.entity.projectile.ITFProjectile;

/**
 * Recreates NeoForge's ProjectileImpactEvent: parrying a projectile with a
 * shield deflects it back.
 */
@Mixin(Projectile.class)
public class ProjectileOnHitMixin {

	@Inject(method = "onHit(Lnet/minecraft/world/phys/HitResult;)V", at = @At("HEAD"), cancellable = true)
	private void twilightforest$parryProjectile(HitResult hitResult, CallbackInfo ci) {
		Projectile projectile = (Projectile) (Object) this;

		if (!projectile.level().isClientSide() && !isParryModLoaded() && (TFConfig.parryNonTwilightAttacks || projectile instanceof ITFProjectile)) {
			if (hitResult instanceof EntityHitResult result) {
				Entity entity = result.getEntity();

				if (entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isBlocking() && entityBlocking.getUseItem().getUseDuration(entityBlocking) - entityBlocking.getUseItemRemainingTicks() <= TFConfig.shieldParryTicks) {
						projectile.deflect(ProjectileDeflection.AIM_DEFLECT, entityBlocking, net.minecraft.world.entity.EntityReference.of(entityBlocking), true);
						ci.cancel();
					}
				}
			}
		}
	}

	private static boolean isParryModLoaded() {
		return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("parry");
	}
}
