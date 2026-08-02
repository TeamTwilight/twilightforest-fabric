package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.ArmorHooks;
import twilightforest.asmhooks.EntityHooks;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
	private void twilightforest$modifyArmorVisibility(Entity lookingEntity, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(ArmorHooks.modifyArmorVisibility(cir.getReturnValue(), (LivingEntity) (Object) this));
	}

	@Inject(method = "canStandOnFluid", at = @At("RETURN"), cancellable = true)
	private void twilightforest$processWaterWalking(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(EntityHooks.processWaterWalking(cir.getReturnValue(), (LivingEntity) (Object) this, fluidState));
	}
}