package twilightforest.client.renderer.entity.legacy;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.legacy.TinyBirdLegacyModel;
import twilightforest.client.renderer.entity.BirdRenderer;
import twilightforest.entity.passive.TinyBird;

public class LegacyTinyBirdRenderer extends BirdRenderer<TinyBird, TinyBirdLegacyModel> {

	public LegacyTinyBirdRenderer(EntityRendererProvider.Context manager, TinyBirdLegacyModel model, float shadowSize) {
		super(manager, model, shadowSize, "");
	}

	@Override
	public ResourceLocation getTextureLocation(TinyBird entity) {
		return entity.getBirdType().texture();
	}
}
