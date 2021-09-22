package twilightforest.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import twilightforest.compat.clothConfig.TFConfigFabric;

public class TFModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            //return TFConfigMenuFabric.getConfigBuilderWithDemo().setParentScreen(screen).build();
            return AutoConfig.getConfigScreen(TFConfigFabric.class, screen).get();
        };
    }
}
