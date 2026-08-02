package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.data.ModelData;
import twilightforest.entity.boss.SnowQueenIceShield;

public class SnowQueenIceShieldRenderer<T extends SnowQueenIceShield> extends EntityRenderer<T> {
	public SnowQueenIceShieldRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		BlockState blockstate = Blocks.PACKED_ICE.defaultBlockState();
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level level = entity.level();
			if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				stack.pushPose();
				BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
				stack.translate(-0.5D, 0.0D, -0.5D);
				BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
				var model = dispatcher.getBlockModel(blockstate);
				dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, stack, buffer.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockstate.getSeed(entity.blockPosition()), OverlayTexture.NO_OVERLAY);
				stack.popPose();
				super.render(entity, entityYaw, partialTicks, stack, buffer, light);
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
