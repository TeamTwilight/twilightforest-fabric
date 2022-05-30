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
import twilightforest.client.model.item.CharmOfLife2NecklaceModel;

public class CharmOfLife2NecklaceRenderer implements TrinketRenderer {

	private CharmOfLife2NecklaceModel model;

	public CharmOfLife2NecklaceRenderer() {
//		this.model = new CharmOfLife2NecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE_2));
	}

	@Override
	public void render(ItemStack item, SlotReference slotContext, EntityModel<? extends LivingEntity> contextModel, PoseStack stack, MultiBufferSource buffer, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (model == null)
			this.model = new CharmOfLife2NecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE_2));
		TrinketRenderer.followBodyRotations(slotContext.inventory().getComponent().getEntity(), model);
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("curios/charm_of_life_2.png")));
		model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
