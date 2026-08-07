package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.item.mapdata.TFMagicMapData;

import java.util.ArrayList;
import java.util.List;

@Mixin(MapRenderer.MapInstance.class)
public abstract class MapInstanceMixin {

	@WrapOperation(
		method = "draw",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;getDecorations()Ljava/lang/Iterable;"
		)
	)
	private Iterable<MapDecoration> twilightforest$reorderDecorations(
		MapItemSavedData mapData,
		Operation<Iterable<MapDecoration>> original
	) {
		Iterable<MapDecoration> decorations = original.call(mapData);
		if (!(mapData instanceof TFMagicMapData)) return decorations;

		List<MapDecoration> reordered = new ArrayList<>();
		List<MapDecoration> playerIcons = new ArrayList<>();
		for (MapDecoration decoration : decorations) {
			if (decoration.type().is(MapDecorationTypes.PLAYER)) {
				playerIcons.add(decoration);
			} else {
				reordered.add(decoration);
			}
		}
		reordered.addAll(playerIcons);

		return reordered;
	}
}