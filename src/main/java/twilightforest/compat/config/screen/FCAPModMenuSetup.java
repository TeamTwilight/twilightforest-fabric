package twilightforest.compat.config.screen;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import twilightforest.TFCommon;

public final class FCAPModMenuSetup {
	public static ConfigScreenFactory<?> factory() {
		return screen -> new ConfigurationScreen(TFCommon.ID, screen);
	}
}
