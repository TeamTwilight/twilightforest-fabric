package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import com.codex.twilight.client.render.CodexModelLayers;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TrollModel;
import twilightforest.entity.monster.Troll;

public class TrollRenderer extends TFBipedRenderer<Troll, TrollModel> {
	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("troll.png");

	public TrollRenderer(EntityRendererProvider.Context context) {
		super(context, new TrollModel(context.bakeLayer(CodexModelLayers.TROLL)), 0.625F, "troll.png");
	}

	@Override
	public void render(Troll entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(entity, yaw, partialTicks, stack, buffer, light);
		this.renderHeldRock(entity, partialTicks, stack, buffer, light);
	}

	private void renderHeldRock(Troll entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		BlockState rock = entity.getRockState();
		if (!entity.hasRock() || rock.isAir() || rock.getRenderShape() == RenderShape.INVISIBLE || entity.isInvisible()) {
			return;
		}

		Vec3 entityPosition = entity.getPosition(partialTicks);
		Vec3 rockPosition = entity.getHeldRockPosition();
		BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
		int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

		stack.pushPose();
		stack.translate(rockPosition.x() - entityPosition.x(), rockPosition.y() - entityPosition.y(), rockPosition.z() - entityPosition.z());
		stack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
		stack.mulPose(Axis.XP.rotationDegrees(-18.0F));
		stack.scale(-0.72F, -0.72F, 0.72F);
		stack.translate(-0.5D, -0.5D, -0.5D);
		dispatcher.renderSingleBlock(rock, stack, buffer, light, overlay);
		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(Troll entity) {
		return TEXTURE;
	}
}
