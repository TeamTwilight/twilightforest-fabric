package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.RedThreadBlockEntity;
import twilightforest.client.TFShaders;
import twilightforest.init.TFItems;

import java.util.Objects;

public class RedThreadRenderer<T extends RedThreadBlockEntity> implements BlockEntityRenderer<T> {

	private static final RenderType GLOW = RenderType
			.create(TwilightForestMod.ID + ":glow", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState
					.builder()
					.setLightmapState(new RenderStateShard.LightmapStateShard(true))
					.setCullState(new RenderStateShard.CullStateShard(true))
					.setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519))
					.setShaderState(new RenderStateShard.ShaderStateShard(() -> TFShaders.RED_THREAD))
					.setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, true))
					.createCompositeState(true));

	public RedThreadRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T thread, float ticks, PoseStack ms, MultiBufferSource source, int light, int overlay) {
		BlockRenderDispatcher blockrenderdispatcher = Minecraft.getInstance().getBlockRenderer();
		BlockState state = thread.getBlockState();
		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isHolding(TFItems.RED_THREAD.get())) {
			render(GLOW, blockrenderdispatcher, thread, ms, source, false);
		} else {
			for (var type : RenderType.chunkBufferLayers()) {
				if (ItemBlockRenderTypes.getChunkRenderType(state) == type)
					render(type, blockrenderdispatcher, thread, ms, source, true);
			}
		}
	}

	private void render(RenderType type, BlockRenderDispatcher blockrenderdispatcher, T thread, PoseStack ms, MultiBufferSource source, boolean light) {
		if (light)
			blockrenderdispatcher.getModelRenderer().tesselateBlock(

					Objects.requireNonNull(thread.getLevel()),

					blockrenderdispatcher.getBlockModel(thread.getBlockState()),

					thread.getBlockState(),

					thread.getBlockPos(),

					ms,

					source.getBuffer(type),

					false,

					thread.getLevel().random,

					thread.getBlockState().getSeed(thread.getBlockPos()),

					OverlayTexture.NO_OVERLAY
			);
		else
			blockrenderdispatcher.getModelRenderer().renderModel(

					ms.last(),

					source.getBuffer(type),

					thread.getBlockState(),

					blockrenderdispatcher.getBlockModel(thread.getBlockState()),

					1F, 1F, 1F,

					0xF000F0,

					OverlayTexture.NO_OVERLAY

			);
	}

}
