package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.HostileWolf;

@Environment(EnvType.CLIENT)
public class WinterWolfRenderer extends HostileWolfRenderer {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("winterwolf.png");

	public WinterWolfRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 1.0F;
	}

	@Override
	protected void scale(HostileWolf entity, PoseStack stack, float partialTicks) {
		float wolfScale = 1.9F;
		stack.scale(wolfScale, wolfScale, wolfScale);

		//The rest of this stuff won't belong here
//        GlStateManager.enableBlend();
//        GlStateManager.disableAlpha();
//        //GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
//        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        //GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_ALPHA, GL11.GL_DST_ALPHA);
//
//        float misty = entity.getBrightness(0F) * 3F + 0.25F;
//        misty = Math.min(1F, misty);
//
//        float smoky = entity.getBrightness(0F) * 2F + 0.6F;
//
//        GlStateManager.color(misty, misty, misty, smoky);
	}

	@Override
	public ResourceLocation getTextureLocation(HostileWolf entity) {
		return textureLoc;
	}
}
