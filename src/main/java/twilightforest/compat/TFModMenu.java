package twilightforest.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import twilightforest.compat.clothConfig.TFConfigClient;
import twilightforest.compat.clothConfig.TFConfigCommon;

public class TFModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            //return TFConfigMenuFabric.getConfigBuilderWithDemo().setParentScreen(screen).build();

            return AutoConfig.getConfigScreen(TFConfigClient.class, screen).get();
        };
    }
}
