package twilightforest.compat.config.screen;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuCompat implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (FabricLoader.getInstance().isModLoaded("forgeconfigapiport")) {
			return FCAPModMenuSetup.factory();
		}
		return ModMenuApi.super.getModConfigScreenFactory();
	}
}
