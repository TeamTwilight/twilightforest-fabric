package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import twilightforest.TFConstants;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.tileentity.MoonwormTileEntity;

import javax.annotation.Nullable;

public class MoonwormTileEntityRenderer implements BlockEntityRenderer<MoonwormTileEntity> {

	private static final ResourceLocation textureLoc = TFConstants.getModelTexture("moonworm.png");
	private final MoonwormModel moonwormModel;

	public MoonwormTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.moonwormModel = new MoonwormModel(renderer.bakeLayer(TFModelLayers.MOONWORM));
	}

	@Override
	public void render(@Nullable MoonwormTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentRotation;
		if (te == null) partialTicks = Minecraft.getInstance().getFrameTime();

		ms.pushPose();
		Direction facing = te != null ? te.getBlockState().getValue(DirectionalBlock.FACING) : Direction.NORTH;

		float rotX = 90.0F;
		float rotZ = 0.0F;
		if (facing == Direction.SOUTH) {
			rotZ = 0F;
		} else if (facing == Direction.NORTH) {
			rotZ = 180F;
		} else if (facing == Direction.EAST) {
			rotZ = -90F;
		} else if (facing == Direction.WEST) {
			rotZ = 90F;
		} else if (facing == Direction.UP) {
			rotX = 0F;
		} else if (facing == Direction.DOWN) {
			rotX = 180F;
		}
		ms.translate(0.5, 0.5, 0.5);
		ms.mulPose(Vector3f.XP.rotationDegrees(rotX));
		ms.mulPose(Vector3f.ZP.rotationDegrees(rotZ));
		ms.mulPose(Vector3f.YP.rotationDegrees(yaw));
		ms.scale(1f, -1f, -1f);

		VertexConsumer builder = buffer.getBuffer(this.moonwormModel.renderType(textureLoc));
		this.moonwormModel.setRotationAngles(te, partialTicks);
		this.moonwormModel.renderToBuffer(ms, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		ms.popPose();
	}
}
