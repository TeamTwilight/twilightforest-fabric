package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.SlideBlock;

public class SlideBlockRenderer extends EntityRenderer<SlideBlock> {

    public SlideBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(SlideBlock entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        BlockState blockState = entity.getBlockState();
        if (blockState.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();
            if (blockState != level.getBlockState(entity.blockPosition()) && blockState.getRenderShape() != RenderShape.INVISIBLE) {
                stack.pushPose();
                BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());

                if (blockState.getProperties().contains(RotatedPillarBlock.AXIS)) {
                    Direction.Axis axis = blockState.getValue(RotatedPillarBlock.AXIS);
                    float angle = (entity.tickCount + partialTicks) * 60.0F;
                    stack.translate(0.0D, 0.5D, 0.0D);
                    if (axis == Direction.Axis.Y) {
                        stack.mulPose(Axis.YP.rotationDegrees(angle));
                    } else if (axis == Direction.Axis.X) {
                        stack.mulPose(Axis.XP.rotationDegrees(angle));
                    } else if (axis == Direction.Axis.Z) {
                        stack.mulPose(Axis.ZP.rotationDegrees(angle));
                    }
                    stack.translate(-0.5D, -0.5D, -0.5D);
                } else {
                    stack.translate(-0.5D, 0.0D, -0.5D);
                }

                BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                dispatcher.getModelRenderer().tesselateBlock(
                        level,
                        dispatcher.getBlockModel(blockState),
                        blockState,
                        blockPos,
                        stack,
                        buffer.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)),
                        false,
                        RandomSource.create(),
                        blockState.getSeed(entity.blockPosition()),
                        OverlayTexture.NO_OVERLAY);
                stack.popPose();
                super.render(entity, yaw, partialTicks, stack, buffer, light);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SlideBlock entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
