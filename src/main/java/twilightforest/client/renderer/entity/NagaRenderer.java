package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.entity.boss.Naga;

//added charging texture for new renderer
public class NagaRenderer<M extends NagaModel<Naga>> extends MobRenderer<Naga, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");
	private static final ResourceLocation textureLocDazed = TwilightForestMod.getModelTexture("nagahead_dazed.png");
	private static final ResourceLocation textureLocCharging = TwilightForestMod.getModelTexture("nagahead_charge.png");

	private int lastTick;

	public NagaRenderer(EntityRendererProvider.Context manager, M modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public void render(Naga entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
		if (!Minecraft.getInstance().isPaused() && entity.isDazed() && entity.tickCount != lastTick) { // TODO: This REALLY shouldn't be here, move it to the Entity#tick method
			for (int i = 0; i < 5; i++) {
				Vec3 pos = new Vec3(entity.getX(), entity.getY() + 2.15D, entity.getZ()).add(new Vec3(1.5D, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))));
				Minecraft.getInstance().level.addParticle(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0);
			}
			lastTick = entity.tickCount;
		}
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
