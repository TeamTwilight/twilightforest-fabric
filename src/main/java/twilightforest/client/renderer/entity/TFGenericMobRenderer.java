package twilightforest.client.renderer.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import twilightforest.TwilightForestMod;

public class TFGenericMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {

	private final Identifier texture;

	public TFGenericMobRenderer(EntityRendererProvider.Context context, M model, float shadowSize, String textureName) {
		super(context, model, shadowSize);

		if (textureName.startsWith("textures")) {
			this.texture = Identifier.withDefaultNamespace(textureName);
		} else {
			this.texture = TwilightForestMod.getModelTexture(textureName);
		}
	}

	@Override
	public Identifier getTextureLocation(T entity) {
		return this.texture;
	}
}
