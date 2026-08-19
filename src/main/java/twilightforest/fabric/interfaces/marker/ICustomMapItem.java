package twilightforest.fabric.interfaces.marker;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;

public interface ICustomMapItem {
	default @Nullable MapItemSavedData getCustomMapData(ItemStack stack, Level level) {
		MapId id = stack.get(DataComponents.MAP_ID);
		return MapItem.getSavedData(id, level);
	}
}