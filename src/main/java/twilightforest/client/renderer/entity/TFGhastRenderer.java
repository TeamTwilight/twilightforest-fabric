package twilightforest.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TFGhastModel;
import twilightforest.entity.monster.CarminiteGhastguard;

@Environment(EnvType.CLIENT)
public class TFGhastRenderer<T extends CarminiteGhastguard, M extends TFGhastModel<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation textureLocClosed = TwilightForestMod.getModelTexture("towerghast.png");
	private static final ResourceLocation textureLocOpen = TwilightForestMod.getModelTexture("towerghast_openeyes.png");
	private static final ResourceLocation textureLocAttack = TwilightForestMod.getModelTexture("towerghast_fire.png");

	public TFGhastRenderer(EntityRendererProvider.Context manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return switch (entity.isCharging() ? 2 : entity.getAttackStatus()) {
			case 1 -> textureLocOpen;
			case 2 -> textureLocAttack;
			default -> textureLocClosed;
		};
	}
}
