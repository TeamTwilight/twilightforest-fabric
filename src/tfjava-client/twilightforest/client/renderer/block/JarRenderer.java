package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import twilightforest.block.entity.MasonJarBlockEntity;

public class JarRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {

	protected final ItemRenderer itemRenderer;

	public JarRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (blockEntity instanceof MasonJarBlockEntity masonJar) {
			this.renderContents(masonJar, poseStack, buffer, packedLight);
		}
	}

	protected void renderContents(MasonJarBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		ItemStack stack = blockEntity.getItemHandler().getItem();
		if (stack.isEmpty()) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0.5D, 0.4375D, 0.5D);
		poseStack.mulPose(Axis.YN.rotationDegrees(RotationSegment.convertToDegrees(blockEntity.getItemRotation())));
		poseStack.scale(0.5F, 0.5F, 0.5F);
		this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, blockEntity.getLevel(), 0);
		poseStack.popPose();
	}

	public static class MasonJarRenderer extends JarRenderer<MasonJarBlockEntity> {
		public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
		}
	}
}
