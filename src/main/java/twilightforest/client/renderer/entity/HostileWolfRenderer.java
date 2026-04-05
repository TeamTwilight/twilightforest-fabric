package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HostileWolfModel;
import twilightforest.entity.monster.HostileWolf;

public class HostileWolfRenderer extends MobRenderer<HostileWolf, HostileWolfModel<HostileWolf>> {

	public HostileWolfRenderer(EntityRendererProvider.Context context) {
		super(context, new HostileWolfModel<>(context.bakeLayer(TFModelLayers.HOSTILE_WOLF)), 0.5F);
	}

	@Override
	protected float getBob(HostileWolf entity, float partialTicks) {
		return entity.getTailAngle();
	}

	@Override
	public Identifier getTextureLocation(HostileWolf entity) {
		return entity.getTexture();
	}
}
