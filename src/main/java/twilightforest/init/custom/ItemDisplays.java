package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import twilightforest.TFCommon;
import twilightforest.init.TFRegistries;
import twilightforest.client.overlay.display.ClockDisplay;
import twilightforest.client.overlay.display.CompassDisplay;
import twilightforest.client.overlay.display.MapDisplay;
import twilightforest.client.overlay.display.MoonDialDisplay;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemDisplays {

	public static final Holder<ItemDisplayType> MAP = register("map", () -> new ItemDisplayType(stack -> stack.getItem() instanceof MapItem, () -> new MapDisplay(), Optional.of(TFCommon.prefix("textures/item/map_display.png"))));
	public static final Holder<ItemDisplayType> COMPASS = register("compass", () -> new ItemDisplayType(stack -> stack.is(Items.COMPASS), () -> new CompassDisplay(), Optional.of(TFCommon.prefix("textures/item/compass_display.png"))));
	public static final Holder<ItemDisplayType> CLOCK = register("clock", () -> new ItemDisplayType(stack -> stack.is(Items.CLOCK), () -> new ClockDisplay(), Optional.of(TFCommon.prefix("textures/item/clock_display.png"))));
	public static final Holder<ItemDisplayType> MOON_DIAL = register("moon_dial", () -> new ItemDisplayType(stack -> stack.is(TFItems.MOON_DIAL), () -> new MoonDialDisplay(), Optional.of(TFCommon.prefix("textures/item/moon_dial_display.png"))));

	private static Holder<ItemDisplayType> register(String name, Supplier<ItemDisplayType> type) {
		return Registry.registerForHolder(
			TFRegistries.ITEM_DISPLAY_TYPE,
			TFCommon.prefix(name),
			type.get()
		);
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing item display types...");
	}
}