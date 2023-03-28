package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public abstract class CharmOfKeepingRenderer {

	public static void render(ItemStack stack, SlotReference slotContext, EntityModel<? extends LivingEntity> contextModel, PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (contextModel instanceof HumanoidModel<?> model) {
			ms.pushPose();
			model.rightLeg.translateAndRotate(ms);
			ms.translate(-0.0D, 0.15D, -0.15D);
			ms.mulPose(Axis.YP.rotationDegrees(0.0F));
			ms.scale(0.3F, -0.3F, -0.3F);
			ItemInHandRenderer renderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
			renderer.renderItem(entity, stack, ItemDisplayContext.FIXED, false, ms, buffer, light);
			ms.popPose();
		}
	}
}
