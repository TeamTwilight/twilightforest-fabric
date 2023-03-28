package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import twilightforest.entity.boss.SnowQueenIceShield;

@Environment(EnvType.CLIENT)
public class SnowQueenIceShieldLayer<T extends SnowQueenIceShield> extends EntityRenderer<T> {
	public SnowQueenIceShieldLayer(EntityRendererProvider.Context renderManager) {
		super(renderManager);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		BlockState blockstate = Blocks.PACKED_ICE.defaultBlockState();
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level world = entityIn.level;
			if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				matrixStackIn.pushPose();
				BlockPos blockpos = BlockPos.containing(entityIn.getX(), entityIn.getBoundingBox().maxY, entityIn.getZ());
				matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
				BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
				var model = dispatcher.getBlockModel(blockstate);
				for (var renderType : RenderType.chunkBufferLayers())
					if (ItemBlockRenderTypes.getChunkRenderType(blockstate) == renderType)
						dispatcher.getModelRenderer().tesselateBlock(world, model, blockstate, blockpos, matrixStackIn, bufferIn.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entityIn.blockPosition()), OverlayTexture.NO_OVERLAY);
				matrixStackIn.popPose();
				super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
