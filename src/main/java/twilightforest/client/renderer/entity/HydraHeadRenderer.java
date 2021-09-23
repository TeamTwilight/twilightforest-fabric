package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import twilightforest.TFConstants;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraHeadModel;
import twilightforest.entity.boss.HydraEntity;
import twilightforest.entity.boss.HydraHeadEntity;
import twilightforest.entity.boss.HydraHeadContainer;

import javax.annotation.Nullable;

public class HydraHeadRenderer extends TFPartRenderer<HydraHeadEntity, HydraHeadModel> {

	private static final ResourceLocation textureLoc = TFConstants.getModelTexture("hydra4.png");


	public HydraHeadRenderer(EntityRendererProvider.Context manager) {
		super(manager, new HydraHeadModel(manager.bakeLayer(TFModelLayers.HYDRA_HEAD)));
	}

	@Override
	public void render(HydraHeadEntity entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		// get the HydraHeadContainer that we're taking about
		HydraHeadContainer headCon = getHeadObject(entity);

		if (headCon != null) {
			// see whether we want to render these
			if (headCon.shouldRenderHead()) {
				stack.mulPose(Vector3f.YP.rotationDegrees(-180));
				super.render(entity, yaw, partialTicks, stack, buffer, light);
			}

		} else {
			super.render(entity, yaw, partialTicks, stack, buffer, light);
		}
	}

	@Nullable
	public static HydraHeadContainer getHeadObject(HydraHeadEntity entity) {
		HydraEntity hydra = entity.getParent();

		if (hydra != null) {
			for (int i = 0; i < hydra.numHeads; i++) {
				if (hydra.hc[i].headEntity == entity) {
					return hydra.hc[i];
				}
			}
		}
		return null;
	}

	@Override
	public ResourceLocation getTextureLocation(HydraHeadEntity entity) {
		return textureLoc;
	}
}
