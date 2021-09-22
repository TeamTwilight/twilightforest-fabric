package twilightforest.compat.clothConfig;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Config(name = "twilightconfigClient")
@Config.Gui.Background(TwilightForestMod.ID + ":textures/block/maze_stone_brick.png")
public class TFConfigClient implements ConfigData {

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @Comment("Make cicadas silent for those having sound library problems, or otherwise finding them annoying.")
    boolean silent_cicadas = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @Comment("Controls whether various effects from the mod are rendered while in first-person view. Turn this off if you find them distracting.")
    boolean first_person_effects = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @Comment("Rotate trophy heads on item model. Has no performance impact at all. For those who don't like fun.")
    boolean animate_trophyitem = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @Comment("Disable the nag screen when Optifine is installed.")
    boolean optifine = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    @Comment("Disable the Here Be Dragons experimental warning screen.")
    boolean dragons = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @Comment("Client only: Controls for the Loading screen")
    loading_Screen loading_screen = new loading_Screen();

    public static class loading_Screen {

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Tooltip
        @Comment("Wobble the Loading icon. Has no performance impact at all. For those who don't like fun.")
        boolean loading_icon_enable = true;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @Comment("How many ticks between each loading screen change. Set to 0 to not cycle at all.")
        int loading_screen_swap_frequency = 0;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Excluded
        //Orignal default value was 5F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        //@ConfigEntry.BoundedFloat(min = 0, max = Float.MAX_VALUE)
        @Comment("Frequency of wobble and bounce.")
        float loading_icon_wobble_bounce_frequency = 5f;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Excluded
        //Orignal default value was 3F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        //@ConfigEntry.BoundedFloat(min = 0, max = Float.MAX_VALUE)
        @Comment("Scale of whole bouncy loading icon.")
        float loading_icon_scale = 3f;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Excluded
        //Orignal default value was 5.25F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        //@ConfigEntry.BoundedFloat(min = 0, max = Float.MAX_VALUE)
        @Comment("How much the loading icon bounces.")
        float loading_icon_bounciness = 5.25f;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Excluded
        //Orignal default value was 11.25F and a max value of 360F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        //@ConfigEntry.BoundedFloat(min = 0, max = 360F)
        @Comment("How far the loading icon wobbles.")
        float loading_icon_tilting = 11.25f;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Excluded
        //Orignal default value was 22.5F and a max value of 360F so fine a way to get Double values
        @ConfigEntry.Gui.Tooltip
        //@ConfigEntry.BoundedFloat(min = 0, max = 360f)
        @Comment("Pushback value to re-center the wobble of loading icon.")
        float loading_icon_tilt_pushback = 22.5F;

        @ConfigEntry.Category("client")
        @ConfigEntry.Gui.Tooltip
        @Comment("List of items to be used for the wobbling Loading Icon. (domain:item).")
        List<String> loading_icon_stacks = Arrays.asList(
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


