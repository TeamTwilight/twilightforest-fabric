package twilightforest.client.renderer.entity.newmodels;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.newmodels.NewHydraModel;
import twilightforest.entity.boss.Hydra;

public class NewHydraRenderer extends MobRenderer<Hydra, NewHydraModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public NewHydraRenderer(EntityRendererProvider.Context manager, NewHydraModel modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	protected float getFlipDegrees(Hydra entity) {
		return 0F;
	}

	@Override
	public ResourceLocation getTextureLocation(Hydra entity) {
		return textureLoc;
	}
}
