package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.SwarmSpider;

@Environment(EnvType.CLIENT)
public class SwarmSpiderRenderer extends SpiderRenderer<SwarmSpider> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("swarmspider.png");

	public SwarmSpiderRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.25F;
	}

	@Override
	public ResourceLocation getTextureLocation(SwarmSpider entity) {
		return textureLoc;
	}

	@Override
	protected void scale(SwarmSpider entity, PoseStack stack, float partialTicks) {
		float scale = 0.5F;
		stack.scale(scale, scale, scale);
	}
}
