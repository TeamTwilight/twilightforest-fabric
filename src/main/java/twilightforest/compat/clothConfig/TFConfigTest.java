package twilightforest.compat.clothConfig;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import twilightforest.TwilightForestMod;
import twilightforest.compat.clothConfig.TFConfigClient;
import twilightforest.compat.clothConfig.TFConfigCommon;

@Config(name = TwilightForestMod.ID)
public class TFConfigTest extends PartitioningSerializer.GlobalData{

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public static TFConfigCommon tfConfigCommon = new TFConfigCommon();

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public static TFConfigClient tfConfigClient = new TFConfigClient();
}
