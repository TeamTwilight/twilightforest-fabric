package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import twilightforest.TFMain;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HelmetCrabModel;
import twilightforest.client.state.entity.HelmetCrabRenderState;
import twilightforest.entity.monster.HelmetCrab;

public class HelmetCrabRenderer extends MobRenderer<HelmetCrab, HelmetCrabRenderState, HelmetCrabModel> {
	public static final Identifier TEXTURE = TFMain.getModelTexture("helmetcrab.png");
	public static final Identifier BLUE_TEXTURE = TFMain.getModelTexture("helmetcrabblue.png");

	public HelmetCrabRenderer(EntityRendererProvider.Context context) {
		super(context, new HelmetCrabModel(context.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F);
	}

	@Override
	public HelmetCrabRenderState createRenderState() {
		return new HelmetCrabRenderState();
	}

	@Override
	public void extractRenderState(HelmetCrab entity, HelmetCrabRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.partialTick = partialTicks;
		state.helmetRot = state.getHelmetRotation(entity, partialTicks);
		state.id = entity.getId();
		state.blue = entity.isBlue();
	}

	@Override
	public void submit(HelmetCrabRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		// Model.renderToBuffer is final in 26.1, so render the helmet separately
		// without the red damage overlay (as the old override did)
		ModelPart helmet = this.model.getHelmet();
		helmet.visible = false;
		super.submit(state, stack, collector, camera);
		helmet.visible = true;
		RenderType renderType = this.model.renderType(this.getTextureLocation(state));
		collector.submitCustomGeometry(stack, renderType, (pose, consumer) -> {
			stack.pushPose();
			stack.last().set(pose);
			helmet.render(stack, consumer, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
			stack.popPose();
		});
	}

	@Override
	protected float getFlipDegrees() {
		return 0.0F;
	}

	@Override
	public Identifier getTextureLocation(HelmetCrabRenderState state) {
		return state.blue ? BLUE_TEXTURE : TEXTURE;
	}
}
