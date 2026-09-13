package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class MapDecorationManager {
	public static final RenderStateDataKey<Holder<MapDecorationType>> DECORATION_TYPE = RenderStateDataKey.create(() -> "map_decoration_type");

	private static final Map<MapDecorationType, TFMapDecorationRenderer> RENDERERS = new IdentityHashMap<>();
	private static final List<BiConsumer<MapItemSavedData, MapRenderState>> MAP = new ObjectArrayList<>();
	private static final Map<ResourceKey<MapDecorationType>, Collection<TFMapDecorationRenderStateModifier>> MAP_DECORATION = new Reference2ObjectArrayMap<>();

	public static void addRenderer(MapDecorationType type, TFMapDecorationRenderer renderer) {
		RENDERERS.put(type, renderer);
	}

	public static void addMap(BiConsumer<MapItemSavedData, MapRenderState> modifier) {
		MAP.add(modifier);
	}

	public static void addDecoration(ResourceKey<MapDecorationType> mapDecorationTypeKey, TFMapDecorationRenderStateModifier modifier) {
		MAP_DECORATION.computeIfAbsent(mapDecorationTypeKey, _ -> new ObjectArrayList<>()).add(modifier);
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

	@ApiStatus.Internal
	public static void onUpdateMapRenderState(MapItemSavedData mapItemSavedData, MapRenderState renderState) {
		renderState.clearExtraData();
		for (BiConsumer<MapItemSavedData, MapRenderState> modifier : MAP) {
			modifier.accept(mapItemSavedData, renderState);
		}
	}

	@ApiStatus.Internal
	public static MapRenderState.MapDecorationRenderState onUpdateMapDecorationRenderState(Holder<MapDecorationType> mapDecorationTypeHolder, MapItemSavedData mapItemSavedData, MapRenderState mapRenderState, MapRenderState.MapDecorationRenderState mapDecorationRenderState) {
		mapDecorationRenderState.clearExtraData();
		var modifiers = MAP_DECORATION.getOrDefault(mapDecorationTypeHolder.unwrapKey().orElseThrow(), List.of());
		for (var modifier : modifiers) {
			modifier.accept(mapItemSavedData, mapRenderState, mapDecorationRenderState);
		}
		return mapDecorationRenderState;
	}
}