package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import twilightforest.TwilightForestMod;
import twilightforest.inventory.UncraftingMenu;

public final class TFMenuTypes {
	public static final MenuType<UncraftingMenu> UNCRAFTING = Registry.register(
			BuiltInRegistries.MENU,
			TwilightForestMod.prefix("uncrafting"),
			new MenuType<>(UncraftingMenu::fromNetwork, FeatureFlags.DEFAULT_FLAGS));

	private TFMenuTypes() {
	}

	public static void bootstrap() {
		// Class init performs the registry write.
	}
}
