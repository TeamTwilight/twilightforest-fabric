package twilightforest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.ArmorHooks;

/**
 * Hides cape rendering when the chestplate has Emperor's Cloth.
 * Simplified for 1.21.1 to avoid WrapOperation issues.
 */
@Mixin(CapeLayer.class)
public class CapeLayerMixin {

	@Inject(
		method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$fixCapeRendering(
		PoseStack poseStack,
		MultiBufferSource buffer,
		int packedLight,
		AbstractClientPlayer livingEntity,
		float limbSwing,
		float limbSwingAmount,
		float partialTicks,
		float ageInTicks,
		float netHeadYaw,
		float headPitch,
		CallbackInfo ci
	) {
		ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		if (!ArmorHooks.fixCapeRendering(true, stack)) {
			ci.cancel();
		}
	}
}