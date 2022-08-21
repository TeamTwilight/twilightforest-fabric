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
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.item.CuriosCharmItem;
import twilightforest.compat.trinkets.model.CharmOfKeepingModel;

public class CharmOfKeepingRenderer implements TrinketRenderer {

	private CharmOfKeepingModel model;

	public CharmOfKeepingRenderer() {
//		this.model = new CharmOfKeepingModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_KEEPING));
	}

	@Override
	public void render(ItemStack stack, SlotReference slotContext, EntityModel<? extends LivingEntity> contextModel, PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (model == null)
			this.model = new CharmOfKeepingModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_KEEPING));
		CuriosCharmItem charm = (CuriosCharmItem) stack.getItem();
		TrinketRenderer.followBodyRotations(slotContext.inventory().getComponent().getEntity(), this.model);
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("curios/" + Registry.ITEM.getKey(charm).getPath() + ".png")));
		model.renderToBuffer(ms, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
