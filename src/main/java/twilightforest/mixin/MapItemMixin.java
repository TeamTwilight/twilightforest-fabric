package twilightforest.mixin;

import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.item.MagicMapItem;
import twilightforest.item.MazeMapItem;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.item.mapdata.TFMazeMapData;

/**
 * Mixin to redirect MapItem.getSavedData to TF custom map data.
 * In 1.21.1, getSavedData takes (MapId, Level) instead of (ItemStack, Level).
 * We intercept the MapId version to check for TF custom map data.
 */
@Mixin(MapItem.class)
public class MapItemMixin {

	@Inject(
		method = "getSavedData(Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void twilightforest$getSavedData(
		@Nullable MapId mapId,
		Level level,
		CallbackInfoReturnable<MapItemSavedData> cir
	) {
		if (mapId == null) return;

		MapItemSavedData data = getTFMapData(level, mapId.id());
		if (data != null) {
			cir.setReturnValue(data);
		}
	}

	@Unique
	@Nullable
	private static MapItemSavedData getTFMapData(Level level, int id) {
		TFMagicMapData magicData = TFMagicMapData.getMagicMapData(level, MagicMapItem.getMapName(id));
		if (magicData != null) {
			return magicData;
		}

		return TFMazeMapData.getMazeMapData(level, MazeMapItem.getMapName(id));
	}
}