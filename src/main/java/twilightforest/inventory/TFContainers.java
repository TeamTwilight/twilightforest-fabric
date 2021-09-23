package twilightforest.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import twilightforest.TFConstants;
import twilightforest.client.UncraftingGui;

import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class TFContainers {

	//public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, TwilightForestMod.ID);

	public static final MenuType<UncraftingContainer> UNCRAFTING = Registry.register(Registry.MENU, TFConstants.ID+ ":uncrafting",
			new MenuType<>(UncraftingContainer::fromNetwork));

	@Environment(EnvType.CLIENT)
	public static void renderScreens() {
		ScreenRegistry.register(UNCRAFTING, UncraftingGui::new);
	}
}
