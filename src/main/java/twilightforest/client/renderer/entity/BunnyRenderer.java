package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.client.model.entity.BunnyModel;
import twilightforest.entity.passive.DwarfRabbit;

public class BunnyRenderer extends MobRenderer<DwarfRabbit, BunnyModel> {

	private final ResourceLocation textureLocDutch = TFConstants.getModelTexture("bunnydutch.png");
	private final ResourceLocation textureLocWhite = TFConstants.getModelTexture("bunnywhite.png");
	private final ResourceLocation textureLocBrown = TFConstants.getModelTexture("bunnybrown.png");

	public BunnyRenderer(EntityRendererProvider.Context manager, BunnyModel model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public ResourceLocation getTextureLocation(DwarfRabbit entity) {
		switch (entity.getBunnyType()) {
			default:
			case 0:
			case 1:
				return textureLocDutch;
			case 2:
				return textureLocWhite;
			case 3:
				return textureLocBrown;
		}
	}
}
