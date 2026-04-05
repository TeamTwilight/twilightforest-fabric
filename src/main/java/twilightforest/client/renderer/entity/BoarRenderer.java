package twilightforest.client.renderer.entity;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.Boar;

public class BoarRenderer<T extends Boar, M extends PigModel<T>> extends MobRenderer<T, M> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("wildboar.png");

	public BoarRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model, 0.7F);
	}

	@Override
	public Identifier getTextureLocation(T entity) {
		return TEXTURE;
	}
}
