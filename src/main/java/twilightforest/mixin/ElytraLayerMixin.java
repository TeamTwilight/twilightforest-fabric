package twilightforest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.ArmorHooks;

/**
 * Cancels elytra rendering when the chestplate has Emperor's Cloth.
 * In 1.21.1, ElytraLayer.shouldRender was removed - intercept render() instead.
 */
@Mixin(ElytraLayer.class)
public class ElytraLayerMixin {

	@Inject(
		method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$cancelElytraRendering(
		PoseStack poseStack,
		MultiBufferSource buffer,
		int packedLight,
		LivingEntity livingEntity,
		float limbSwing,
		float limbSwingAmount,
		float partialTicks,
		float ageInTicks,
		float netHeadYaw,
		float headPitch, CallbackInfo ci
	) {
		ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		if (!ArmorHooks.cancelArmorRendering(true, stack)) {
			ci.cancel();
		}
	}
}