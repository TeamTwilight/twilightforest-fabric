package twilightforest.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.fabricators_of_create.porting_lib.config.client.gui.ConfigurationScreen;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.TwilightForestMod;

public class ModMenuCompat implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> new ConfigurationScreen(FabricLoader.getInstance().getModContainer(TwilightForestMod.ID).orElseThrow(), screen);
	}
}