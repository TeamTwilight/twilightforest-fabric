package twilightforest.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import twilightforest.init.TFBlocks;

public class RenderLayerRegistration {
	@SuppressWarnings("removal")
	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BEANSTALK_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_CASTLE_DOOR.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_FORCE_FIELD.get(), RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BROWN_THORNS.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BURNT_THORNS.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.CANDELABRA.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.CANOPY_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.DARK_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.FIERY_BLOCK.get(), RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.GREEN_FORCE_FIELD.get(), RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.GREEN_THORNS.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.MANGROVE_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.MINING_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ORANGE_FORCE_FIELD.get(), RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_CASTLE_DOOR.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_FORCE_FIELD.get(), RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.RAINBOW_OAK_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.RED_THREAD.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.SORTING_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.THORN_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.TIME_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.TRANSFORMATION_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.TWILIGHT_OAK_LEAVES.get(), RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.TWISTED_STONE_PILLAR.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_CASTLE_DOOR.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_FORCE_FIELD.get(), RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.YELLOW_CASTLE_DOOR.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), RenderType.cutout());
	}
}
