package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.client.model.entity.QuestRamModel;
import twilightforest.entity.passive.QuestRamEntity;

public class QuestRamRenderer extends MobRenderer<QuestRamEntity, QuestRamModel> {

	private static final ResourceLocation textureLoc = TFConstants.getModelTexture("questram.png");
	private static final ResourceLocation textureLocLines = TFConstants.getModelTexture("questram_lines.png");

	public QuestRamRenderer(EntityRendererProvider.Context manager, QuestRamModel model) {
		super(manager, model, 1.0F);
		addLayer(new LayerGlowingLines(this));
	}

	@Override
	public ResourceLocation getTextureLocation(QuestRamEntity entity) {
		return textureLoc;
	}

	class LayerGlowingLines extends RenderLayer<QuestRamEntity, QuestRamModel> {

		public LayerGlowingLines(RenderLayerParent<QuestRamEntity, QuestRamModel> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int i, QuestRamEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(textureLocLines));
			stack.scale(1.025f, 1.025f, 1.025f);
			QuestRamRenderer.this.getModel().renderToBuffer(stack, builder, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
