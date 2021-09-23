package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.entity.SlimeBeetleEntity;

public class SlimeBeetleRenderer extends MobRenderer<SlimeBeetleEntity, SlimeBeetleModel> {

	private static final ResourceLocation textureLoc = TFConstants.getModelTexture("slimebeetle.png");

	public SlimeBeetleRenderer(EntityRendererProvider.Context manager, SlimeBeetleModel model, float shadowSize) {
		super(manager, model, shadowSize);
		addLayer(new LayerInner(this, manager));
	}

	@Override
	public void render(SlimeBeetleEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if(this.model.riding) matrixStackIn.translate(0, -0.5F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(SlimeBeetleEntity entity) {
		return textureLoc;
	}

	static class LayerInner extends RenderLayer<SlimeBeetleEntity, SlimeBeetleModel> {
		private final SlimeBeetleModel innerModel;

		public LayerInner(RenderLayerParent<SlimeBeetleEntity, SlimeBeetleModel> renderer, EntityRendererProvider.Context manager) {
			super(renderer);
			innerModel =  new SlimeBeetleModel(manager.bakeLayer(TFModelLayers.SLIME_BEETLE), true);
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, SlimeBeetleEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isInvisible()) {
				innerModel.copyPropertiesTo(getParentModel());
				innerModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
				innerModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				VertexConsumer buffer = buffers.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
				innerModel.renderToBuffer(ms, buffer, light, LivingEntityRenderer.getOverlayCoords(entity, 0), 1, 1, 1, 1);
			}
		}
	}
}
