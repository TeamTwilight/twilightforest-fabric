package twilightforest.compat.clothConfig.configFiles;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shadow.cloth.clothconfig.com.moandjiezana.toml.TomlComment;
import twilightforest.compat.clothConfig.configEntry.ExtendedConfigEntry;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Config(name = "twilightforest-client")
@Environment(EnvType.CLIENT)
//@Config.Gui.Background(TwilightForestMod.ID + ":textures/block/maze_stone_brick.png")
public class TFConfigClient implements ConfigData {

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Make cicadas silent for those having sound library problems, or otherwise finding them annoying.")
    public boolean silentCicadas = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Controls whether various effects from the mod are rendered while in first-person view. Turn this off if you find them distracting.")
    public boolean firstPersonEffects = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Rotate trophy heads on item model. Has no performance impact at all. For those who don't like fun.")
    public boolean rotateTrophyHeadsGui = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Disable the nag screen when Optifine is installed.")
    public boolean disableOptifineNagScreen = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Disable the Here Be Dragons experimental warning screen.")
    public boolean disableHereBeDragons = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Client only: Controls for the Loading screen")
    public LoadingScreen loading_screen = new LoadingScreen();

    public static class LoadingScreen {

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Tooltip
        @TomlComment("Wobble the Loading icon. Has no performance impact at all. For those who don't like fun.")
        public boolean enable = true;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("How many ticks between each loading screen change. Set to 0 to not cycle at all.")
        public int cycleLoadingScreenFrequency = 0;

        @ConfigEntry.Category("client")
        //@ConfigEntry.Gui.Excluded
        //Orignal default value was 5F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        @ExtendedConfigEntry.BoundedFloat(min = 0, max = Float.MAX_VALUE)
        @TomlComment("Frequency of wobble and bounce.")
        public float frequency = 5f;

        @ConfigEntry.Category("client")
        //@ConfigEntry.Gui.Excluded
        //Orignal default value was 3F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        @ExtendedConfigEntry.BoundedFloat(min = 0, max = Float.MAX_VALUE)
        @TomlComment("Scale of whole bouncy loading icon.")
        public float scale = 3f;

        @ConfigEntry.Category("client")
        //@ConfigEntry.Gui.Excluded
        //Orignal default value was 5.25F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        @ExtendedConfigEntry.BoundedFloat(min = 0, max = Float.MAX_VALUE)
        @TomlComment("How much the loading icon bounces.")
        public float scaleDeviation = 5.25f;

        @ConfigEntry.Category("client")
        //@ConfigEntry.Gui.Excluded
        //Orignal default value was 11.25F and a max value of 360F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        @ExtendedConfigEntry.BoundedFloat(min = 0, max = 360F)
        @TomlComment("How far the loading icon wobbles.")
        public float tiltRange = 11.25f;

        @ConfigEntry.Category("client")
        //@ConfigEntry.Gui.Excluded
        //Orignal default value was 22.5F and a max value of 360F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        @ExtendedConfigEntry.BoundedFloat(min = 0, max = 360f)
        @TomlComment("Pushback value to re-center the wobble of loading icon.")
        public float tiltConstant = 22.5F;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Tooltip
        @TomlComment("List of items to be used for the wobbling Loading Icon. (domain:item).")
        public List<String> loadingIconStacks = Arrays.asList(
                "twilightforest:experiment_115",
                "twilightforest:magic_map",
                "twilightforest:charm_of_life_2",
                "twilightforest:charm_of_keeping_3",
                "twilightforest:phantom_helmet",
                "twilightforest:lamp_of_cinders",
                "twilightforest:carminite",
                "twilightforest:block_and_chain",
                "twilightforest:yeti_helmet",
                "twilightforest:hydra_chop",
                "twilightforest:magic_beans",
                "twilightforest:ironwood_raw",
                "twilightforest:naga_scale",
                "twilightforest:experiment_115{think:1}",
                "twilightforest:twilight_portal_miniature_structure",
                "twilightforest:lich_tower_miniature_structure",
                "twilightforest:knightmetal_block",
                "twilightforest:ghast_trap",
                "twilightforest:time_sapling",
                "twilightforest:transformation_sapling",
                "twilightforest:mining_sapling",
                "twilightforest:sorting_sapling",
                "twilightforest:rainboak_sapling",
                "twilightforest:borer_essence"
        );
    }
}


