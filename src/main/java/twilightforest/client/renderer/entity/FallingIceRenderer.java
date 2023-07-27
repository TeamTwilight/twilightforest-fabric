package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.projectile.FallingIce;

public class FallingIceRenderer extends EntityRenderer<FallingIce> {
	public FallingIceRenderer(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn);
		this.shadowRadius = 0.5F;
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.FallingBlockRenderer}
	 */
	@Override
	public void render(FallingIce entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		BlockState blockstate = entity.getBlockState();
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level world = entity.level();
			if (blockstate != world.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				stack.pushPose();
				BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
				stack.translate(-0.5D, 0.0D, -0.5D);
				BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
				var model = dispatcher.getBlockModel(blockstate);
				for (var renderType : RenderType.chunkBufferLayers())
					if (ItemBlockRenderTypes.getChunkRenderType(blockstate) == renderType)
						dispatcher.getModelRenderer().tesselateBlock(world, model, blockstate, blockpos, stack, buffer.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entity.blockPosition()), OverlayTexture.NO_OVERLAY);
				stack.popPose();
				super.render(entity, entityYaw, partialTicks, stack, buffer, light);
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(FallingIce entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
