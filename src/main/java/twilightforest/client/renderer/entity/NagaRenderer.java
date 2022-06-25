package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.entity.boss.Naga;

//added charging texture for new renderer
public class NagaRenderer<M extends NagaModel<Naga>> extends MobRenderer<Naga, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");
	private static final ResourceLocation textureLocDazed = TwilightForestMod.getModelTexture("nagahead_dazed.png");
	private static final ResourceLocation textureLocCharging = TwilightForestMod.getModelTexture("nagahead_charge.png");

	public NagaRenderer(EntityRendererProvider.Context manager, M modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public ResourceLocation getTextureLocation(Naga entity) {
		if (entity.isDazed()) {
			return textureLocDazed;
		} else if (entity.isCharging()) {
			return textureLocCharging;
		} else {
			return textureLoc;
		}
	}
}
