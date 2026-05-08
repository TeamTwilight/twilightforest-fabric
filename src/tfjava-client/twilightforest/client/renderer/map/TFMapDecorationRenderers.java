package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public final class TFMapDecorationRenderers {
	private static final MagicMapPlayerIconRenderer MAGIC_PLAYER = new MagicMapPlayerIconRenderer();
	private static final ConqueredMapIconRenderer CONQUERED = new ConqueredMapIconRenderer();

	private TFMapDecorationRenderers() {
	}

	public static boolean renderMagicPlayerIcon(MapDecoration decoration, PoseStack stack, MultiBufferSource bufferSource, MapItemSavedData mapData, MapDecorationTextureManager decorationTextures, int light) {
		return MAGIC_PLAYER.render(decoration, stack, bufferSource, mapData, decorationTextures, light);
	}

	public static void renderConqueredOverlay(MapDecoration decoration, PoseStack stack, MultiBufferSource bufferSource, MapItemSavedData mapData, MapDecorationTextureManager decorationTextures, int light) {
		CONQUERED.render(decoration, stack, bufferSource, mapData, decorationTextures, light);
	}
}
