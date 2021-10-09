package twilightforest.compat.rei.common;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import twilightforest.inventory.UncraftingContainer;

public class REIServer implements REIServerPlugin {
    public static final CategoryIdentifier UNCRAFTING_ID = CategoryIdentifier.of("minecraft", "plugins/crafting");

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(UNCRAFTING_ID, UncraftingContainer.class, new UncraftingTableMenuInfo<>());
    }
}
