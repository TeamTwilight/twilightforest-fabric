package twilightforest.compat.curios.renderer;

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
	public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel,
			PoseStack poseStack, MultiBufferSource buffer, int light, LivingEntity entity,
			float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (contextModel instanceof HumanoidModel<?> model) {
			poseStack.pushPose();
			model.rightLeg.translateAndRotate(poseStack);
			poseStack.translate(-0.0D, 0.15D, -0.15D);
			poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
			poseStack.scale(0.3F, -0.3F, -0.3F);
			ItemInHandRenderer renderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
			renderer.renderItem(entity, stack, ItemDisplayContext.FIXED, false, poseStack, buffer, light);
			poseStack.popPose();
		}
	}
}
