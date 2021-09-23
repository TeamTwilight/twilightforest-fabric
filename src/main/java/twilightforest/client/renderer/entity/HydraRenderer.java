package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.client.model.entity.HydraModel;
import twilightforest.entity.boss.HydraEntity;

public class HydraRenderer extends MobRenderer<HydraEntity, HydraModel> {

	private static final ResourceLocation textureLoc = TFConstants.getModelTexture("hydra4.png");

	public HydraRenderer(EntityRendererProvider.Context manager, HydraModel modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	protected float getFlipDegrees(HydraEntity entity) {
		return 0F;
	}

	@Override
	public ResourceLocation getTextureLocation(HydraEntity entity) {
		return textureLoc;
	}
}
