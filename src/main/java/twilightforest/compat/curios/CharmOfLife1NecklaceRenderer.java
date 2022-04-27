package twilightforest.compat.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.item.CharmOfLife1NecklaceModel;

public class CharmOfLife1NecklaceRenderer implements TrinketRenderer {

	private final CharmOfLife1NecklaceModel model;

	public CharmOfLife1NecklaceRenderer() {
		this.model = new CharmOfLife1NecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE_1));
	}

	@Override
	public void render(ItemStack item, SlotReference slotContext, EntityModel<? extends LivingEntity> contextModel, PoseStack stack, MultiBufferSource buffer, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		model.setupAnim(slotContext.inventory().getComponent().getEntity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.prepareMobModel(slotContext.inventory().getComponent().getEntity(), limbSwing, limbSwingAmount, partialTicks);
		TrinketRenderer.followBodyRotations(slotContext.inventory().getComponent().getEntity(), model);
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("curios/charm_of_life_1.png")));
		model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
