package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConstants;

public class MistWolfRenderer extends WolfRenderer {

	private static final ResourceLocation textureLoc = TFConstants.getModelTexture("mistwolf.png");

	public MistWolfRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 1.0F;
	}

	@Override
	protected void scale(Wolf entity, PoseStack stack, float partialTicks) {
		float wolfScale = 1.9F;
		stack.scale(wolfScale, wolfScale, wolfScale);

		RenderSystem.enableBlend();
		//GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_ALPHA, GL11.GL_DST_ALPHA);

		float misty = entity.getBrightness() * 3F + 0.25F;
		misty = Math.min(1F, misty);

		float smoky = entity.getBrightness() * 2F + 0.6F;

		RenderSystem.setShaderColor(misty, misty, misty, smoky);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */

    @Override
	public ResourceLocation getTextureLocation(Wolf entity) {
		return textureLoc;
	}
}
