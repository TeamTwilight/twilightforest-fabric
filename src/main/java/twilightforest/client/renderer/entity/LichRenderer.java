package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.client.model.entity.LichModel;
import twilightforest.entity.boss.LichEntity;

public class LichRenderer extends HumanoidMobRenderer<LichEntity, LichModel> {

	private static final ResourceLocation LICH_TEXTURE = TFConstants.getModelTexture("twilightlich64.png");

	public LichRenderer(EntityRendererProvider.Context manager, LichModel modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
		addLayer(new ShieldLayer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(LichEntity entity) {
		return LICH_TEXTURE;
	}
}
