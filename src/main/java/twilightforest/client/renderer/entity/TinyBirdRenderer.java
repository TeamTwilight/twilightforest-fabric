package twilightforest.client.renderer.entity;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import twilightforest.entity.passive.TinyBird;

public class TinyBirdRenderer<T extends TinyBird, M extends AgeableListModel<T>> extends BirdRenderer<T, M> {

	public TinyBirdRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize, "");
	}

	@Override
	public Identifier getTextureLocation(T entity) {
		return entity.getVariant().value().texture();
	}
}
