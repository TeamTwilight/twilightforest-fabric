package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
	private CharmOfLifeNecklaceModel model;
	private final int necklaceColor;

	public CharmOfLifeNecklaceRenderer(int necklaceColor) {
		this.necklaceColor = necklaceColor;
	}

	@Override
	public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, PoseStack matrices, MultiBufferSource vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		CharmOfLifeNecklaceModel model = getModel();
		if (contextModel instanceof HumanoidModel<?> humanoid) {
			matrices.pushPose();
			humanoid.body.translateAndRotate(matrices);
			matrices.translate(0.0D, 0.23D, -0.135D);
			matrices.scale(-0.4F, -0.4F, 0.4F);

			ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;

			renderer.renderItem(entity, stack, ItemDisplayContext.FIXED, false, matrices, vertexConsumers, light);
			matrices.popPose();
		}
		model.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		model.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
		TrinketRenderer.followBodyRotations(entity, model);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("charm_of_life_necklace.png")));
		model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, necklaceColor);
	}

	private CharmOfLifeNecklaceModel getModel() {
		if (this.model == null) {
			this.model = new CharmOfLifeNecklaceModel(
				Minecraft.getInstance()
					.getEntityModels()
					.bakeLayer(TFModelLayers.CHARM_OF_LIFE)
			);
		}
		return this.model;
	}
}