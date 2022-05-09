package twilightforest.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HostileWolfModel;
import twilightforest.entity.monster.HostileWolf;

@Environment(EnvType.CLIENT)
public class HostileWolfRenderer extends MobRenderer<HostileWolf, HostileWolfModel<HostileWolf>> {
	private static final ResourceLocation WOLF_LOCATION = new ResourceLocation("textures/entity/wolf/wolf.png");
	private static final ResourceLocation WOLF_ANGRY_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

	public HostileWolfRenderer(EntityRendererProvider.Context p_174452_) {
		super(p_174452_, new HostileWolfModel<>(p_174452_.bakeLayer(TFModelLayers.HOSTILE_WOLF)), 0.5F);
	}

	@Override
	protected float getBob(HostileWolf p_116528_, float p_116529_) {
		return p_116528_.getTailAngle();
	}

	@Override
	public ResourceLocation getTextureLocation(HostileWolf wolf) {
		return wolf.isAggressive() ? WOLF_ANGRY_LOCATION : WOLF_LOCATION;
	}
}
