package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.trinkets.model.CharmOfLifeNecklaceModel;

public class CharmOfLifeNecklaceRenderer implements TrinketRenderer {
	private final CharmOfLifeNecklaceModel model;
	private final int necklaceColor;

	public CharmOfLifeNecklaceRenderer(int necklaceColor) {
		this.model = new CharmOfLifeNecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE));
		this.necklaceColor = necklaceColor;
	}

	@Override
	public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, PoseStack matrices, MultiBufferSource vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (contextModel instanceof HumanoidModel<?> model) {
			matrices.pushPose();
			model.body.translateAndRotate(matrices);
			matrices.translate(-0.0D, 0.23D, -0.135D);
			matrices.mulPose(Axis.YP.rotationDegrees(0.0F));
			matrices.scale(-0.4F, -0.4F, 0.4F);
			ItemInHandRenderer renderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
			renderer.renderItem(entity, stack, ItemDisplayContext.FIXED, false, matrices, vertexConsumers, light);
			matrices.popPose();
		}
		this.model.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.model.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
		TrinketRenderer.followBodyRotations(entity, this.model);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("charm_of_life_necklace.png")));
		this.model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, this.necklaceColor);
	}
}