package twilightforest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

/**
 * Cancels armor rendering for entities wearing Emperor's Cloth.
 * In 1.21.1, renderArmorPiece no longer takes float parameters.
 */
@Environment(EnvType.CLIENT)
@Mixin(HumanoidArmorLayer.class)
public class ArmorRenderLayerMixin {

	@Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
	private void twilightforest$cancelArmorRendering(PoseStack poseStack, MultiBufferSource buffer, LivingEntity livingEntity, EquipmentSlot slot, int i, HumanoidModel<?> model, CallbackInfo ci) {
		ItemStack stack = livingEntity.getItemBySlot(slot);
		if (!ArmorHooks.cancelArmorRendering(true, stack)) {
			ci.cancel();
		}
	}
}