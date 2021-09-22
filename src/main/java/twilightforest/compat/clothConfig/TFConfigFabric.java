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
@Config(name = "twilightconfig")
@Config.Gui.Background(TwilightForestMod.ID + ":textures/block/maze_stone_brick.png")
public class TFConfigFabric implements ConfigData {
    //private static final String config = TwilightForestMod.ID + ".config.";

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @Comment("Settings that are not reversible without consequences.")
    dimSettings dimension = new dimSettings();

    public static class dimSettings {

        @ConfigEntry.Gui.Tooltip
        @Comment("If true, players spawning for the first time will spawn in the Twilight Forest.")
        boolean spawn_in_tf = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @Comment("If true, Twilight Forest will generate as a void except for Major Structures")
        boolean skylight_forest = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @Comment("If true, giant Twilight Oaks will also spawn in void worlds")
        boolean skylight_oaks = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @Comment("Marked dimension ID for Twilight Portals and some other Twilight mod logic as well")
        String portal_destination_id = "twilightforest:twilight_forest";

        @ConfigEntry.Gui.CollapsibleObject
        @ConfigEntry.Gui.Tooltip
        @Comment("""
            Defines custom stalactites generated in hollow hills.
            Format is "modid:block size maxLength minHeight weight", where the properties are:
            Size - the maximum length of the stalactite relative to the space between hill floor and ceiling,
            Max length - maximum length of a stalactite in blocks,
            Min height - minimum space between the hill floor and the stalactite to let it generate,
            Weight - how often it generates.
            For example: "minecraft:iron_ore 0.7 8 1 24" would add a stalactite equal to the default iron ore stalactite.""")
        hollowHillStalactites hollow_hill_stalactites = new hollowHillStalactites();

        public static class hollowHillStalactites {

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @Comment("Blocks generating as stalactites in large hills only")
            ArrayList<String> large_hill = new ArrayList<>();

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @Comment("Blocks generating as stalactites in medium and large hills")
            ArrayList<String> medium_hill = new ArrayList<>();

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @Comment("Blocks generating as stalactites in all hills")
            ArrayList<String> small_hill = new ArrayList<>();

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @Comment("If true, default stalactites and stalactites defined by other mods will not be used.")
            boolean stalactite_config_only = false;
        }

    }

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Should TF Compatibility load? Turn off if TF's Compatibility is causing crashes or if not desired.")
    boolean compat = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("The dimension you can always travel to the Twilight Forest from, as well as the dimension you will return to. Defaults to the overworld. (domain:regname).")
    String origin_dimension = "minecraft:overworld";

    @ConfigEntry.Gui.Tooltip
    @Comment("Allow portals to the Twilight Forest to be made outside of the 'origin' dimension. May be considered an exploit.")
    boolean portals_in_other_dimensions = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Allow portals only for admins (Operators). This severely reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.")
    boolean admin_portals = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.")
    boolean portals = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Determines if new portals should be pre-checked for safety. If enabled, portals will fail to form rather than redirect to a safe alternate destination." +
            "\nNote that enabling this also reduces the rate at which portal formation checks are performed.")
    boolean check_portal_destination = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Set this true if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.")
    boolean portal_lighting = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("If false, the return portal will require the activation item.")
    boolean portal_return = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Disable the uncrafting function of the uncrafting table. Provided as an option when interaction with other mods produces exploitable recipes.")
    boolean uncrafting = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("If true, Keepsake Caskets that are spawned when a player dies will not be accessible by other players. Use this if you dont want people taking from other people's death caskets. NOTE: server operators will still be able to open locked caskets.")
    boolean casket_uuid_locking = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("If true, disables the ability to make Skull Candles by right clicking a vanilla skull with a candle. Turn this on if you're having mod conflict issues for some reason.")
    boolean disable_skull_candles = false;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @Comment("We recommend downloading the Shield Parry mod for parrying, but these controls remain for without.")
    shield_interactions shield_parry = new shield_interactions();

    public static class shield_interactions {

        @ConfigEntry.Gui.Tooltip
        @Comment("Set to true to parry non-Twilight projectiles.")
        boolean parry_non_twilight = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @Comment("The amount of ticks after raising a shield that makes it OK to parry an arrow.")
        int parry_window_arrow = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @Comment("The amount of ticks after raising a shield that makes it OK to parry a fireball.")
        int parry_window_fireball = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @Comment("The amount of ticks after raising a shield that makes it OK to parry a thrown item.")
        int parry_window_throwable = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @Comment("???")
        int parry_window_beam = 40;
    }
    
    client client = new client();

    @Config(name = "twilightconfig")
    public static class client{

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


}


