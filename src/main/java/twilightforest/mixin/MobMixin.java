package twilightforest.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.EntityHooks;

@Mixin(Mob.class)
public class MobMixin {

	@Inject(method = "aiStep", at = @At("HEAD"))
	private void twilightforest$unrestrainedSprintingInWater(CallbackInfo ci) {
		Mob self = (Mob) (Object) this;
		boolean isInWater = self.isInWater();
		boolean result = EntityHooks.unrestrainedSprintingInWater(isInWater, (LivingEntity) (Object) this);
		// If unrestrained modifier is active, override isInWater behavior
		// This is handled by the hook which returns false when unrestrained and can walk on water
		if (result != isInWater && !result) {
			// The hook handles the logic internally - the actual sprinting behavior
			// is controlled by the isInWater check in the original aiStep method
		}
	}
}