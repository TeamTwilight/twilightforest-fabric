package twilightforest.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TinyBirdModel;
import twilightforest.entity.passive.TinyBird;

@Environment(EnvType.CLIENT)
public class TinyBirdRenderer extends BirdRenderer<TinyBird, TinyBirdModel> {

	private static final ResourceLocation textureLocSparrow  = TwilightForestMod.getModelTexture("tinybirdbrown.png");
	private static final ResourceLocation textureLocFinch    = TwilightForestMod.getModelTexture("tinybirdgold.png");
	private static final ResourceLocation textureLocCardinal = TwilightForestMod.getModelTexture("tinybirdred.png");
	private static final ResourceLocation textureLocBluebird = TwilightForestMod.getModelTexture("tinybirdblue.png");

	public TinyBirdRenderer(EntityRendererProvider.Context manager, TinyBirdModel model, float shadowSize) {
		super(manager, model, shadowSize, "");
	}

	@Override
	public ResourceLocation getTextureLocation(TinyBird entity) {
		return switch (entity.getBirdType()) {
			case 1 -> textureLocBluebird;
			case 2 -> textureLocCardinal;
			case 3 -> textureLocFinch;
			default -> textureLocSparrow;
		};
	}
}
