package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.inventory.UncraftingMenu;

public class TFMenuTypes {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, TwilightForestMod.ID);

	public static final DeferredHolder<MenuType<?>, MenuType<UncraftingMenu>> UNCRAFTING = CONTAINERS.register("uncrafting",
		() -> new MenuType<>(UncraftingMenu::fromNetwork, FeatureFlags.REGISTRY.allFlags()));
}
