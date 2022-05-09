package twilightforest.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.BunnyModel;
import twilightforest.entity.passive.DwarfRabbit;

@Environment(EnvType.CLIENT)
public class BunnyRenderer extends MobRenderer<DwarfRabbit, BunnyModel> {

	private final ResourceLocation textureLocDutch = TwilightForestMod.getModelTexture("bunnydutch.png");
	private final ResourceLocation textureLocWhite = TwilightForestMod.getModelTexture("bunnywhite.png");
	private final ResourceLocation textureLocBrown = TwilightForestMod.getModelTexture("bunnybrown.png");

	public BunnyRenderer(EntityRendererProvider.Context manager, BunnyModel model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public ResourceLocation getTextureLocation(DwarfRabbit entity) {
		return switch (entity.getBunnyType()) {
			case 2 -> textureLocWhite;
			case 3 -> textureLocBrown;
			default -> textureLocDutch;
		};
	}
}
