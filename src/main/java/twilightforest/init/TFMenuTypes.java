package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import twilightforest.TwilightForestMod;
import twilightforest.client.UncraftingScreen;
import twilightforest.inventory.UncraftingMenu;

public class TFMenuTypes {

	public static final LazyRegistrar<MenuType<?>> CONTAINERS = LazyRegistrar.create(Registries.MENU, TwilightForestMod.ID);

	public static final RegistryObject<MenuType<UncraftingMenu>> UNCRAFTING = CONTAINERS.register("uncrafting",
			() -> new MenuType<>(UncraftingMenu::fromNetwork));

	@Environment(EnvType.CLIENT)
	public static void renderScreens() {
		MenuScreens.register(UNCRAFTING.get(), UncraftingScreen::new);
	}
}
