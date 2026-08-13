package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import twilightforest.TFMain;
import twilightforest.inventory.UncraftingMenu;

public class TFMenuTypes {

	public static final MenuType<UncraftingMenu> UNCRAFTING = register("uncrafting", new MenuType<>(UncraftingMenu::fromNetwork, FeatureFlags.REGISTRY.allFlags()));

	private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType<T> type) {
		return Registry.register(
			BuiltInRegistries.MENU,
			TFMain.prefix(name),
			type
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing menu types...");
	}
}