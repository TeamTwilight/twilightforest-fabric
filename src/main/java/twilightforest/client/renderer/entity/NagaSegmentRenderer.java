package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.entity.boss.NagaSegment;

public class NagaSegmentRenderer<T extends NagaSegment> extends TFPartRenderer<T, NagaModel<T>> {
	private static final ResourceLocation part_TextureLoc = TwilightForestMod.getModelTexture("nagasegment.png");

	public NagaSegmentRenderer(EntityRendererProvider.Context m) {
		super(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NEW_NAGA_BODY)));
	}

	@Override
	public void render(T segment, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		if (!segment.isInvisible()) {
			stack.pushPose();

			float yawDiff = segment.getYRot() - segment.yRotO;
			if (yawDiff > 180) {
				yawDiff -= 360;
			} else if (yawDiff < -180) {
				yawDiff += 360;
			}
			float yaw2 = segment.yRotO + yawDiff * partialTicks;

			stack.mulPose(Axis.YP.rotationDegrees(yaw2));
			stack.mulPose(Axis.XP.rotationDegrees(segment.getXRot()));

			stack.scale(2.0F, 2.0F, 2.0F);
			stack.translate(0.0D, -1.25F, 0.0D);

			int realLight = this.entityRenderDispatcher.getPackedLightCoords(segment.getParent(), partialTicks);
			super.render(segment, entityYaw, partialTicks, stack, buffer, realLight);
			stack.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return part_TextureLoc;
	}
}
