package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MosquitoSwarmModel;
import twilightforest.entity.monster.MosquitoSwarm;

public class MosquitoSwarmRenderer extends TFGenericMobRenderer<MosquitoSwarm, MosquitoSwarmModel> {

	public MosquitoSwarmRenderer(EntityRendererProvider.Context manager) {
		super(manager, new MosquitoSwarmModel(manager.bakeLayer(TFModelLayers.MOSQUITO_SWARM)), 0.0F, "mosquitoswarm.png");
	}

	@Override
	protected float getFlipDegrees(MosquitoSwarm swarm) {
		return 0.0F;
	}
}
