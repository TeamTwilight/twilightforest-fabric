package twilightforest.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import twilightforest.TwilightForestMod;
import twilightforest.compat.rei.displays.REIUncraftingDisplay;
import twilightforest.inventory.UncraftingMenu;

public class TwilightForestREIServerPlugin implements REIServerPlugin {

    public static final CategoryIdentifier<REIUncraftingDisplay> UNCRAFTING = CategoryIdentifier.of(TwilightForestMod.ID, "uncrafting");

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(UNCRAFTING, UncraftingMenu.class, SimpleMenuInfoProvider.of(UncraftingTableMenuInfo::new));
        registry.register(BuiltinPlugin.CRAFTING, UncraftingMenu.class, SimpleMenuInfoProvider.of(UncraftingTableMenuInfo::new));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(UNCRAFTING, REIUncraftingDisplay.Serializer.INSTANCE);
    }
}
