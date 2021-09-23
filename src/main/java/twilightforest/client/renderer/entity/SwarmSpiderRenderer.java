package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.entity.SwarmSpiderEntity;

public class SwarmSpiderRenderer extends SpiderRenderer<SwarmSpiderEntity> {

	private static final ResourceLocation textureLoc = TFConstants.getModelTexture("swarmspider.png");

	public SwarmSpiderRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.25F;
	}

	@Override
	public ResourceLocation getTextureLocation(SwarmSpiderEntity entity) {
		return textureLoc;
	}

	@Override
	public void render(SwarmSpiderEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if(this.model.riding) matrixStackIn.translate(0, 0.15F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected void scale(SwarmSpiderEntity entity, PoseStack stack, float partialTicks) {
		float scale = 0.5F;
		stack.scale(scale, scale, scale);
	}
}
