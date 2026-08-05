package twilightforest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.ArmorHooks;

@Mixin(value = HumanoidArmorLayer.class, priority = 500) // Needs to be before Porting Lib and Fabric API for some custom armor sets to work
public class HumanoidArmorLayerMixin {

	@Inject(
		method = "renderArmorPiece",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$cancelArmorRendering(
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		LivingEntity livingEntity,
		EquipmentSlot slot,
		int packedLight,
		HumanoidModel<?> model, CallbackInfo ci
	) {
		ItemStack stack = livingEntity.getItemBySlot(slot);
		if (!ArmorHooks.cancelArmorRendering(true, stack)) {
			ci.cancel();
		}
	}
}