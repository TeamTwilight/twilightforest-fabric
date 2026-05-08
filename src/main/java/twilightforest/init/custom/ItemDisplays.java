package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.Optional;

public final class ItemDisplays {
    public static final ItemDisplayType MAP = register("map", new ItemDisplayType(
            stack -> stack.getItem() instanceof MapItem,
            Optional.of(TwilightForestMod.prefix("textures/item/map_display.png"))));
    public static final ItemDisplayType COMPASS = register("compass", new ItemDisplayType(
            stack -> stack.is(Items.COMPASS),
            Optional.of(TwilightForestMod.prefix("textures/item/compass_display.png"))));
    public static final ItemDisplayType CLOCK = register("clock", new ItemDisplayType(
            stack -> stack.is(Items.CLOCK),
            Optional.of(TwilightForestMod.prefix("textures/item/clock_display.png"))));
    public static final ItemDisplayType MOON_DIAL = register("moon_dial", new ItemDisplayType(
            stack -> stack.is(TFItems.MOON_DIAL.get()),
            Optional.of(TwilightForestMod.prefix("textures/item/moon_dial_display.png"))));

    private ItemDisplays() {
    }

    public static void bootstrap() {
    }

    private static ItemDisplayType register(String path, ItemDisplayType type) {
        return Registry.register(TFRegistries.ITEM_DISPLAY_TYPE, TwilightForestMod.prefix(path), type);
    }
}
