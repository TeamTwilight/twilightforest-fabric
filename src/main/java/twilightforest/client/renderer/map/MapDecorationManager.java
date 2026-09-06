package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Holder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

import java.util.IdentityHashMap;
import java.util.Map;

public final class MapDecorationManager {
	public static final RenderStateDataKey<Holder<MapDecorationType>> DECORATION_TYPE = RenderStateDataKey.create(() -> "map_decoration_type");

	private static final Map<MapDecorationType, TFMapDecorationRenderer> RENDERERS = new IdentityHashMap<>();

	public static void addDecoration(MapDecorationType type, TFMapDecorationRenderer renderer) {
		RENDERERS.put(type, renderer);
	}

	public static boolean render(
		MapRenderState.MapDecorationRenderState decorationRenderState,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		MapRenderState mapRenderState,
		TextureAtlas decorationSprites,
		boolean inItemFrame,
		int packedLight,
		int index
	) {
		TFMapDecorationRenderer decorationRenderer = RENDERERS.get(decorationRenderState.getData(DECORATION_TYPE).value());
		if (decorationRenderer != null) {
			return decorationRenderer.render(decorationRenderState, poseStack, submitNodeCollector, mapRenderState, decorationSprites, inItemFrame, packedLight, index);
		}
		return false;
	}
}