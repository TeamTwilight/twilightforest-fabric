package twilightforest.asm.mixin.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import twilightforest.asm.hooks.event.ToolEventHooks;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends Projectile {
	public FireworkRocketEntityMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@Override
	public void onHit(HitResult result) {
		if (result.getType() == HitResult.Type.MISS) {
			super.onHit(result);
			return;
		}

		var canceled = false;

		// ProjectileImpactEvent events go here and need to set canceled...
		canceled |= ToolEventHooks.onEnderBowHit(this, result);
		canceled |= TravellersGearEventHooks.magnetizeArrows(this, result);
		canceled |= TravellersGearEventHooks.performPerfectDodge(this, result);

		if (!canceled) {
			super.onHit(result);
		}
	}
}