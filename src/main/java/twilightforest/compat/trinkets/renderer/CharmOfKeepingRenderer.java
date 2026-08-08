package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CharmOfKeepingRenderer implements TrinketRenderer {

	@Override
	public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, PoseStack matrices, MultiBufferSource vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (contextModel instanceof HumanoidModel<?> model) {
			matrices.pushPose();
			model.rightLeg.translateAndRotate(matrices);
			matrices.translate(-0.0D, 0.15D, -0.15D);
			matrices.mulPose(Axis.YP.rotationDegrees(0.0F));
			matrices.scale(0.3F, -0.3F, -0.3F);
			ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;
			renderer.renderItem(entity, stack, ItemDisplayContext.FIXED, false, matrices, vertexConsumers, light);
			matrices.popPose();
		}
	}
}