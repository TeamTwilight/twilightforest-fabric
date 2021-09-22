package twilightforest.compat.clothConfig;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import shadow.cloth.clothconfig.com.moandjiezana.toml.TomlComment;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Config(name = "twilightconfig")
@Config.Gui.Background(TwilightForestMod.ID + ":textures/block/maze_stone_brick.png")
//@Config.Gui.CategoryBackground(category = "common", background = TwilightForestMod.ID + ":textures/block/maze_stone_brick.png")
public class TFConfigCommon implements ConfigData {
    //private static final String config = TwilightForestMod.ID + ".config.";

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Settings that are not reversible without consequences.")
    dimSettings dimension = new dimSettings();

    public static class dimSettings {

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @TomlComment("#If true, players spawning for the first time will spawn in the Twilight Forest.")
        @ConfigEntry.Gui.PrefixText
        boolean spawn_in_tf = false;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @TomlComment("#If true, Twilight Forest will generate as a void except for Major Structures")
        boolean skylight_forest = false;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @TomlComment("#If true, giant Twilight Oaks will also spawn in void worlds")
        boolean skylight_oaks = false;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @TomlComment("Marked dimension ID for Twilight Portals and some other Twilight mod logic as well")
        String portal_destination_id = "twilightforest:twilight_forest";

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.CollapsibleObject
        @ConfigEntry.Gui.Tooltip
        @TomlComment("""
            Defines custom stalactites generated in hollow hills.
            Format is "modid:block size maxLength minHeight weight", where the properties are:
            Size - the maximum length of the stalactite relative to the space between hill floor and ceiling,
            Max length - maximum length of a stalactite in blocks,
            Min height - minimum space between the hill floor and the stalactite to let it generate,
            Weight - how often it generates.
            For example: "minecraft:iron_ore 0.7 8 1 24" would add a stalactite equal to the default iron ore stalactite.""")
        hollowHillStalactites hollow_hill_stalactites = new hollowHillStalactites();

        public static class hollowHillStalactites {

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("Blocks generating as stalactites in large hills only")
            ArrayList<String> large_hill = new ArrayList<>();

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("Blocks generating as stalactites in medium and large hills")
            ArrayList<String> medium_hill = new ArrayList<>();

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("Blocks generating as stalactites in all hills")
            ArrayList<String> small_hill = new ArrayList<>();

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("If true, default stalactites and stalactites defined by other mods will not be used.")
            boolean stalactite_config_only = false;
        }

    }

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    @TomlComment("Should TF Compatibility load? Turn off if TF's Compatibility is causing crashes or if not desired.")
    boolean compat = true;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("The dimension you can always travel to the Twilight Forest from, as well as the dimension you will return to. Defaults to the overworld. (domain:regname).")
    String origin_dimension = "minecraft:overworld";

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Allow portals to the Twilight Forest to be made outside of the 'origin' dimension. May be considered an exploit.")
    boolean portals_in_other_dimensions = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Allow portals only for admins (Operators). This severely reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.")
    boolean admin_portals = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.")
    boolean portals = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Determines if new portals should be pre-checked for safety. If enabled, portals will fail to form rather than redirect to a safe alternate destination." +
            "\nNote that enabling this also reduces the rate at which portal formation checks are performed.")
    boolean check_portal_destination = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Set this true if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.")
    boolean portal_lighting = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("If false, the return portal will require the activation item.")
    boolean portal_return = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Disable the uncrafting function of the uncrafting table. Provided as an option when interaction with other mods produces exploitable recipes.")
    boolean uncrafting = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("If true, Keepsake Caskets that are spawned when a player dies will not be accessible by other players. Use this if you dont want people taking from other people's death caskets. NOTE: server operators will still be able to open locked caskets.")
    boolean casket_uuid_locking = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("If true, disables the ability to make Skull Candles by right clicking a vanilla skull with a candle. Turn this on if you're having mod conflict issues for some reason.")
    boolean disable_skull_candles = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @TomlComment("We recommend downloading the Shield Parry mod for parrying, but these controls remain for without.")
    shield_interactions shield_parry = new shield_interactions();

    public static class shield_interactions {

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @TomlComment("Set to true to parry non-Twilight projectiles.")
        boolean parry_non_twilight = false;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("The amount of ticks after raising a shield that makes it OK to parry an arrow.")
        int parry_window_arrow = 40;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("The amount of ticks after raising a shield that makes it OK to parry a fireball.")
        int parry_window_fireball = 40;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("The amount of ticks after raising a shield that makes it OK to parry a thrown item.")
        int parry_window_throwable = 40;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("???")
        int parry_window_beam = 40;
    }
}


