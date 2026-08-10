package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import twilightforest.TFMain;
import twilightforest.TFRegistries;
import twilightforest.client.overlay.display.ClockDisplay;
import twilightforest.client.overlay.display.CompassDisplay;
import twilightforest.client.overlay.display.MapDisplay;
import twilightforest.client.overlay.display.MoonDialDisplay;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.Optional;

public class ItemDisplays {

	public static final ItemDisplayType MAP = register("map", new ItemDisplayType(stack -> stack.getItem() instanceof MapItem, () -> new MapDisplay(), Optional.of(TFMain.prefix("textures/item/map_display.png"))));
	public static final ItemDisplayType COMPASS = register("compass", new ItemDisplayType(stack -> stack.is(Items.COMPASS), () -> new CompassDisplay(), Optional.of(TFMain.prefix("textures/item/compass_display.png"))));
	public static final ItemDisplayType CLOCK = register("clock", new ItemDisplayType(stack -> stack.is(Items.CLOCK), () -> new ClockDisplay(), Optional.of(TFMain.prefix("textures/item/clock_display.png"))));
	public static final ItemDisplayType MOON_DIAL = register("moon_dial", new ItemDisplayType(stack -> stack.is(TFItems.MOON_DIAL), () -> new MoonDialDisplay(), Optional.of(TFMain.prefix("textures/item/moon_dial_display.png"))));

	private static ItemDisplayType register(String name, ItemDisplayType type) {
		return Registry.register(
			TFRegistries.ITEM_DISPLAY_TYPE,
			TFMain.prefix(name),
			type
		);
	}
}