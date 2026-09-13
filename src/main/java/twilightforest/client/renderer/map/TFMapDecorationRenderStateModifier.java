package twilightforest.client.renderer.map;

import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@FunctionalInterface
public interface TFMapDecorationRenderStateModifier {
	void accept(
		MapItemSavedData mapItemSavedData,
		MapRenderState mapRenderState,
		MapRenderState.MapDecorationRenderState mapDecorationRenderState
	);
}