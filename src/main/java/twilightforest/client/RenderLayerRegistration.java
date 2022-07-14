package twilightforest.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import twilightforest.init.TFBlocks;

public class RenderLayerRegistration {
	@SuppressWarnings("removal")
	public static void init() {
		RenderType cutout = RenderType.cutout();
		RenderType translucent = RenderType.translucent();

		//FIXME these blocks absolutely refuse to use the render types defined in the json
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.CANDELABRA.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.TWISTED_STONE_PILLAR.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.FIERY_BLOCK.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.YELLOW_CASTLE_DOOR.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_CASTLE_DOOR.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_CASTLE_DOOR.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_CASTLE_DOOR.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.GREEN_THORNS.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BROWN_THORNS.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BURNT_THORNS.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.GREEN_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ORANGE_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.RED_THREAD.get(), cutout);
	}
}
