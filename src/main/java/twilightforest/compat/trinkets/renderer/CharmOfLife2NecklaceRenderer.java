package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.trinkets.model.CharmOfLife2NecklaceModel;

public abstract class CharmOfLife2NecklaceRenderer {

	private static CharmOfLife2NecklaceModel model;

	public static void render(ItemStack item, SlotReference slotContext, EntityModel<? extends LivingEntity> contextModel, PoseStack stack, MultiBufferSource buffer, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (model == null)
			model = new CharmOfLife2NecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE_2));
		TrinketRenderer.followBodyRotations(slotContext.inventory().getComponent().getEntity(), model);
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("curios/charm_of_life_2.png")));
		model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
