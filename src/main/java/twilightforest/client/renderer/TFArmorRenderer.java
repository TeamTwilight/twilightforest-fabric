package twilightforest.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.model.armor.TFArmorModel;

public class TFArmorRenderer {
	public static void renderArmorPart(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ItemStack stack, Model model, ResourceLocation texture, int color) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil());
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float b = (float) (color & 255) / 255.0F;
		model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 1);
	}

	public static void setPartVisibility(TFArmorModel armorModel, EquipmentSlot slot) {
		armorModel.setAllVisible(false);
		switch (slot) {
			case HEAD:
				armorModel.head.visible = true;
				armorModel.hat.visible = true;
				break;
			case CHEST:
				armorModel.body.visible = true;
				armorModel.rightArm.visible = true;
				armorModel.leftArm.visible = true;
				break;
			case LEGS:
				armorModel.body.visible = true;
				armorModel.rightLeg.visible = true;
				armorModel.leftLeg.visible = true;
				break;
			case FEET:
				armorModel.rightLeg.visible = true;
				armorModel.leftLeg.visible = true;
		}
	}
}
