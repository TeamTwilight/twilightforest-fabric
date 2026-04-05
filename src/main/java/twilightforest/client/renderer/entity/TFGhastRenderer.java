package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TFGhastModel;
import twilightforest.entity.monster.CarminiteGhastguard;

public class TFGhastRenderer<T extends CarminiteGhastguard, M extends TFGhastModel<T>> extends MobRenderer<T, M> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("towerghast.png");
	private static final Identifier LOOKING_TEXTURE = TwilightForestMod.getModelTexture("towerghast_openeyes.png");
	private static final Identifier ATTACKING_TEXTURE = TwilightForestMod.getModelTexture("towerghast_fire.png");

	public TFGhastRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize);
	}

	@Override
	public Identifier getTextureLocation(T entity) {
		if (entity.isCharging() || entity.isDeadOrDying()) {
			return ATTACKING_TEXTURE;
		}

		return switch (entity.getAttackStatus()) {
			case 1 -> LOOKING_TEXTURE;
			case 2 -> ATTACKING_TEXTURE;
			default -> TEXTURE;
		};
	}
}
