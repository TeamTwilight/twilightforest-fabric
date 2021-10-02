package twilightforest.compat.clothConfig.configFiles;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import shadow.cloth.clothconfig.com.moandjiezana.toml.TomlComment;
import twilightforest.TFConstants;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.BlockSpikeFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Config(name = "twilightforest-common")
@Config.Gui.Background(TFConstants.ID + ":textures/block/maze_stone_brick.png")
public class TFConfigCommon implements ConfigData {

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    @TomlComment("Should TF Compatibility load? Turn off if TF's Compatibility is causing crashes or if not desired.")
    public boolean doCompat = true;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("The dimension you can always travel to the Twilight Forest from, as well as the dimension you will return to. Defaults to the overworld. (domain:regname).")
    public String originDimension = "minecraft:overworld";

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Allow portals to the Twilight Forest to be made outside of the 'origin' dimension. May be considered an exploit.")
    public boolean allowPortalsInOtherDimensions = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Allow portals only for admins (Operators). This severely reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.")
    public boolean adminOnlyPortals = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.")
    public boolean disablePortalCreation = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Determines if new portals should be pre-checked for safety. If enabled, portals will fail to form rather than redirect to a safe alternate destination." +
            "\nNote that enabling this also reduces the rate at which portal formation checks are performed.")
    public boolean checkPortalDestination = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Set this true if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.")
    public boolean portalLightning = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("If false, the return portal will require the activation item.")
    public boolean shouldReturnPortalBeUsable = true;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Disable the uncrafting function of the uncrafting table. Provided as an option when interaction with other mods produces exploitable recipes.")
    public boolean disableUncrafting = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("If true, Keepsake Caskets that are spawned when a player dies will not be accessible by other players. Use this if you dont want people taking from other people's death caskets. NOTE: server operators will still be able to open locked caskets.")
    public boolean uuid_locking = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    @TomlComment("If true, disables the ability to make Skull Candles by right clicking a vanilla skull with a candle. Turn this on if you're having mod conflict issues for some reason.")
    public boolean skull_candles = false;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @TomlComment("Settings that are not reversible without consequences.")
    public dimSettings dimension = new dimSettings();

    public static class dimSettings {

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @TomlComment("#If true, players spawning for the first time will spawn in the Twilight Forest.")
        public boolean newPlayersSpawnInTF = false;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @TomlComment("#If true, Twilight Forest will generate as a void except for Major Structures")
        public boolean skylightForest = false;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @TomlComment("#If true, giant Twilight Oaks will also spawn in void worlds")
        public boolean skylightOaks = true;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @TomlComment("Marked dimension ID for Twilight Portals and some other Twilight mod logic as well")
        public String portalDestinationID = "twilightforest:twilight_forest";

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
        public hollowHillStalactites hollowHillStalactites = new hollowHillStalactites();

        public static class hollowHillStalactites {

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("Blocks generating as stalactites in large hills only")
            public ArrayList<String> largeHill = new ArrayList<>();

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("Blocks generating as stalactites in medium and large hills")
            public ArrayList<String> mediumHill = new ArrayList<>();

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("Blocks generating as stalactites in all hills")
            public ArrayList<String> smallHill = new ArrayList<>();

            @ConfigEntry.Category("common")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.Gui.RequiresRestart
            @TomlComment("If true, default stalactites and stalactites defined by other mods will not be used.")
            public boolean useConfigOnly = false;

            public void load() {
                registerHill(smallHill, 1);
                registerHill(mediumHill, 2);
                registerHill(largeHill, 3);
            }

            private void registerHill(List<? extends String> definitions, int tier) {
                for (String definition : definitions) {
                    if (!parseStalactite(definition, tier)) {
                        TwilightForestMod.LOGGER.warn("Invalid hollow hill stalactite definition: {}", definition);
                    }
                }
            }

            private boolean parseStalactite(String definition, int tier) {
                String[] split = definition.split(" ");
                if (split.length != 5) return false;

                Optional<Block> block = parseBlock(split[0]);
                if (!block.isPresent()) return false;

                try {
                    BlockSpikeFeature.registerStalactite(tier, block.get().defaultBlockState(),
                            Float.parseFloat(split[1]),
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            Integer.parseInt(split[4])
                    );
                } catch (NumberFormatException e) {
                    return false;
                }
                return true;
            }

            private static Optional<Block> parseBlock(String string) {
                ResourceLocation id = ResourceLocation.tryParse(string);
                if (id == null || !Registry.BLOCK.containsKey(id)) {
                    return Optional.empty();
                } else {
                    return Optional.of(Registry.BLOCK.get(id));
                }
            }

        }

    }

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    @TomlComment("We recommend downloading the Shield Parry mod for parrying, but these controls remain for without.")
    public shield_interactions shield_parry = new shield_interactions();

    public static class shield_interactions {

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @TomlComment("Set to true to parry non-Twilight projectiles.")
        public boolean parryNonTwilightAttacks = false;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("The amount of ticks after raising a shield that makes it OK to parry an arrow.")
        public int shieldParryTicksArrow = 40;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("The amount of ticks after raising a shield that makes it OK to parry a fireball.")
        public int shieldParryTicksFireball = 40;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("The amount of ticks after raising a shield that makes it OK to parry a thrown item.")
        public int shieldParryTicksThrowable = 40;

        @ConfigEntry.Category("common")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
        @TomlComment("???")
        public int shieldParryTicksBeam = 40;
    }
}

