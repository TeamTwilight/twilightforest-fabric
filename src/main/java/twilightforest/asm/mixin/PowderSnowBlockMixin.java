package twilightforest.asm.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFItems;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

	@Inject(method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
	private static void twilightforest$canWalkWithYetiBoots(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		// Item.canWalkOnPowderedSnow was removed in 26.1, so restore the Yeti
		// boots behaviour here
		if (!cir.getReturnValueZ() && entity instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.FEET).is(TFItems.YETI_BOOTS)) {
			cir.setReturnValue(true);
		}
	}
}
