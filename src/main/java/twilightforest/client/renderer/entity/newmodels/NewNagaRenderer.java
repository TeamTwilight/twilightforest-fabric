package twilightforest.client.renderer.entity.newmodels;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.newmodels.NewNagaModel;
import twilightforest.entity.boss.Naga;

//added charging texture for new renderer
public class NewNagaRenderer<M extends NewNagaModel<Naga>> extends MobRenderer<Naga, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");
	private static final ResourceLocation textureLocDazed = TwilightForestMod.getModelTexture("nagahead_dazed.png");
	private static final ResourceLocation textureLocCharging = TwilightForestMod.getModelTexture("nagahead_charge.png");

	public NewNagaRenderer(EntityRendererProvider.Context manager, M modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public boolean shouldRender(Naga pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		if (pLivingEntity.deathTime > 32) return false;
		return super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
	}

	@Override
	protected float getFlipDegrees(Naga naga) { //Prevent the body from keeling over
		return naga.isDeadOrDying() ? 0.0F : super.getFlipDegrees(naga);
	}

	@Override
	public ResourceLocation getTextureLocation(Naga entity) {
		if (entity.isDazed()) {
			return textureLocDazed;
		} else if (entity.isCharging() || entity.isDeadOrDying()) {
			return textureLocCharging;
		} else {
			return textureLoc;
		}
	}
}
