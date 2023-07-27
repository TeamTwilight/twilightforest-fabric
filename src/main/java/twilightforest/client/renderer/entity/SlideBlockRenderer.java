package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.SlideBlock;

import java.util.Random;

public class SlideBlockRenderer extends EntityRenderer<SlideBlock> {

	public SlideBlockRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		shadowRadius = 0.0f;
	}

	// [VanillaCopy] RenderFallingBlock, with spin
	@Override
	public void render(SlideBlock entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		if (entity.getBlockState() != null) {
			BlockState blockstate = entity.getBlockState();

			if (blockstate.getRenderShape() == RenderShape.MODEL) {
				Level world = entity.level();

				if (blockstate != world.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
					stack.pushPose();
					BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
					// spin
					if (blockstate.getProperties().contains(RotatedPillarBlock.AXIS)) {
						Direction.Axis axis = blockstate.getValue(RotatedPillarBlock.AXIS);
						float angle = (entity.tickCount + partialTicks) * 60F;
						stack.translate(0.0, 0.5, 0.0);
						if (axis == Direction.Axis.Y) {
							stack.mulPose(Axis.YP.rotationDegrees(angle));
						} else if (axis == Direction.Axis.X) {
							stack.mulPose(Axis.XP.rotationDegrees(angle));
						} else if (axis == Direction.Axis.Z) {
							stack.mulPose(Axis.ZP.rotationDegrees(angle));
						}
						stack.translate(-0.5, -0.5, -0.5);
					}

					BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
					var model = dispatcher.getBlockModel(blockstate);
					for (var renderType : RenderType.chunkBufferLayers())
						if (ItemBlockRenderTypes.getChunkRenderType(blockstate) == renderType)
							dispatcher.getModelRenderer().tesselateBlock(world, model, blockstate, blockpos, stack, buffer.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entity.blockPosition()), OverlayTexture.NO_OVERLAY);

					stack.popPose();
					super.render(entity, yaw, partialTicks, stack, buffer, light);
				}
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(SlideBlock entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
