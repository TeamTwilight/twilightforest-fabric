package twilightforest.compat.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CurioHeadRenderer {

	public static void render(ItemStack stack, SlotReference slotContext, EntityModel<? extends LivingEntity> contextModel, PoseStack matrixStack, MultiBufferSource buffer, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(contextModel instanceof HeadedModel headModel) {
			matrixStack.pushPose();
			headModel.getHead().translateAndRotate(matrixStack);
			matrixStack.translate(0.0D, -0.25D, 0.0D);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
			matrixStack.scale(0.625F, -0.625F, -0.625F);
			Minecraft.getInstance().getItemInHandRenderer().renderItem(slotContext.inventory().getComponent().getEntity(), stack, ItemTransforms.TransformType.HEAD, false, matrixStack, buffer, light);
			matrixStack.popPose();
		}
	}
}
