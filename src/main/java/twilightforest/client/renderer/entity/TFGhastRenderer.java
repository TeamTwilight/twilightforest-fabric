package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.client.model.entity.TFGhastModel;
import twilightforest.entity.monster.CarminiteGhastguard;

public class TFGhastRenderer<T extends CarminiteGhastguard, M extends TFGhastModel<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation textureLocClosed = TFConstants.getModelTexture("towerghast.png");
	private static final ResourceLocation textureLocOpen = TFConstants.getModelTexture("towerghast_openeyes.png");
	private static final ResourceLocation textureLocAttack = TFConstants.getModelTexture("towerghast_fire.png");

	public TFGhastRenderer(EntityRendererProvider.Context manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		switch (entity.isCharging() ? 2 : entity.getAttackStatus()) {
			default:
			case 0:
				return textureLocClosed;
			case 1:
				return textureLocOpen;
			case 2:
				return textureLocAttack;
		}
	}
}
