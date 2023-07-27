package twilightforest.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.entity.TinyBirdModel;
import twilightforest.client.renderer.entity.BirdRenderer;
import twilightforest.entity.passive.TinyBird;

@Environment(EnvType.CLIENT)
public class TinyBirdRenderer extends BirdRenderer<TinyBird, TinyBirdModel> {

	public TinyBirdRenderer(EntityRendererProvider.Context manager, TinyBirdModel model, float shadowSize) {
		super(manager, model, shadowSize, "");
	}

	@Override
	public ResourceLocation getTextureLocation(TinyBird entity) {
		return entity.getBirdType().texture();
	}
}
