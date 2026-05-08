package twilightforest.config;

import com.mojang.authlib.GameProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fabric-side mirror of the upstream Twilight Forest config fields used by
 * runtime code. NeoForge's TOML rebake layer is replaced with a small JSON file
 * so server owners can change the same gameplay toggles without recompiling.
 */
public final class TFConfig {
    public static final String CONFIG_ID = "config.twilightforest.";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "codex-twilight.json";

    public static final List<GameProfile> GAME_PROFILES = new ArrayList<>();

    /**
     * 1:1 of upstream {@code tfGenericGameRules.enforcedProgression}. Trophy pedestal,
     * portal openings, and several other landmark interactions check this — enabled by
     * default on upstream so we keep parity here.
     */
    public static boolean enforcedProgression = true;

    public static boolean disableEntireTable = false;
    public static boolean disableUncraftingOnly = false;
    public static boolean flipUncraftingTableHotkey = false;
    public static boolean allowShapelessUncrafting = false;
    public static boolean disableIngredientSwitching = false;
    public static boolean reverseRecipeBlacklist = false;
    public static boolean flipUncraftingModIdList = false;
    public static double uncraftingXpCostMultiplier = 1.0D;
    public static double repairingXpCostMultiplier = 1.0D;
    public static final List<String> disableUncraftingRecipes = new ArrayList<>();
    public static final List<String> blacklistedUncraftingModIds = new ArrayList<>();

    public static boolean silentCicadas = false;
    public static boolean silentCicadasOnHead = false;
    public static boolean firstPersonEffects = true;
    public static boolean rotateTrophyHeadsGui = true;
    public static boolean disableOptifineNagScreen = false;
    public static boolean disableLockedBiomeToasts = false;
    public static boolean showQuestRamCrosshairIndicator = true;
    public static boolean showFortificationShieldIndicator = true;
    public static boolean showFortificationShieldIndicatorInCreative = false;
    public static int clientCloudBlockPrecipitationDistance = -1;
    public static final List<String> giantSkinUUIDs = new ArrayList<>();
    public static final List<String> auroraBiomes = new ArrayList<>(List.of("twilightforest:glacier"));
    public static boolean prettifyOreMeterGui = true;
    public static boolean spawnCharmAnimationAsTotem = false;
    public static boolean manualTravellersWingsGradualGlide = true;
    public static boolean manualTravellersWingsGradualGlideDefault = true;
    public static boolean firstPersonGloveOverlay = true;
    public static int itemDisplayScreenOffsetX = 4;
    public static int itemDisplayScreenOffsetY = 4;
    public static double itemDisplayScreenScale = 1.0D;
    public static boolean twentyFourHourFormat = use24HourTimeDefault();

    public static boolean disableSkullCandles = false;
    public static boolean casketUUIDLocking = false;
    public static boolean defaultItemEnchants = true;
    public static int commonCloudBlockPrecipitationDistance = 32;
    public static boolean disableTimeCore = false;
    public static int timeCoreRange = 16;
    public static boolean disableTransformationCore = false;
    public static int transformationCoreRange = 16;
    public static boolean disableMiningCore = false;
    public static int miningCoreRange = 16;
    public static boolean disableSortingCore = false;
    public static int sortingCoreRange = 16;

    /** Default matches upstream: no multiplayer scaling unless config enables it. */
    public static MultiplayerFightAdjuster multiplayerFightAdjuster = MultiplayerFightAdjuster.NONE;

    /**
     * Mirror of upstream {@code TFConfig.Common} so that log-core and other blocks that call
     * {@code TFConfig.COMMON_CONFIG.MAGIC_TREES.disableX} compile unchanged.
     */
    public static final CommonConfig COMMON_CONFIG = new CommonConfig();

    public static final class CommonConfig {
        public final MagicTrees MAGIC_TREES = new MagicTrees();

        public static final class MagicTrees {
            // Feature toggles — false = feature is ON (upstream default)
            public boolean disableTime = false;
            public boolean disableMining = false;
            public boolean disableSorting = false;
            public boolean disableTransformation = false;

            // Effect ranges (in blocks)
            public int timeRange = 16;
            public int miningRange = 16;
            public int sortingRange = 16;
            public int transformationRange = 16;
        }
    }

    public static boolean bossDropChests = true;
    public static boolean newPlayersSpawnInTF = false;
    public static boolean portalForNewPlayerSpawn = false;
    public static String originDimension = Level.OVERWORLD.location().toString();
    public static boolean allowPortalsInOtherDimensions = false;
    public static int portalCreationPermission = 0;
    public static boolean disablePortalCreation = false;
    public static String portalLockingAdvancement = "";
    public static boolean checkPortalPlacement = true;
    public static boolean destructivePortalLightning = true;
    public static boolean shouldReturnPortalBeUsable = true;
    public static int maxPortalSize = 64;
    public static boolean parryNonTwilightAttacks = false;
    public static int shieldParryTicks = 40;

    private TFConfig() {
    }

    public static int getClientCloudBlockPrecipitationDistance() {
        return clientCloudBlockPrecipitationDistance > 0 ? clientCloudBlockPrecipitationDistance : commonCloudBlockPrecipitationDistance;
    }

    public static ResourceLocation getPortalLockingAdvancement(Player player) {
        if (portalLockingAdvancement == null || portalLockingAdvancement.isBlank()) {
            return null;
        }
        ResourceLocation parsed = ResourceLocation.tryParse(portalLockingAdvancement);
        if (parsed == null) {
            TwilightForestMod.LOGGER.warn("Ignoring invalid portal locking advancement '{}'", portalLockingAdvancement);
        }
        return parsed;
    }

    public static void load(Path configDir) {
        Path path = configDir.resolve(CONFIG_FILE);
        try {
            Files.createDirectories(configDir);
            if (Files.notExists(path)) {
                save(path);
                return;
            }

            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                ConfigData data = GSON.fromJson(reader, ConfigData.class);
                if (data != null) {
                    data.apply();
                }
            }
            syncMagicTreeMirror();
        } catch (Exception exception) {
            TwilightForestMod.LOGGER.error("Failed to load Codex Twilight config from {}", path, exception);
            syncMagicTreeMirror();
        }
    }

    public static void save(Path path) throws IOException {
        syncMagicTreeMirror();
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            GSON.toJson(ConfigData.capture(), writer);
        }
    }

    public static void rebakeCommonOptions(TFCommonConfig config) {
        newPlayersSpawnInTF = config.DIMENSION.newPlayersSpawnInTF.get();
        portalForNewPlayerSpawn = config.DIMENSION.portalForNewPlayerSpawn.get();
        originDimension = config.PORTAL.originDimension.get();
        allowPortalsInOtherDimensions = config.PORTAL.allowPortalsInOtherDimensions.get();
        portalCreationPermission = config.PORTAL.portalCreationPermission.get();
        disablePortalCreation = config.PORTAL.disablePortalCreation.get();
        checkPortalPlacement = config.PORTAL.checkPortalPlacement.get();
        destructivePortalLightning = config.PORTAL.destructivePortalLightning.get();
        shouldReturnPortalBeUsable = config.PORTAL.shouldReturnPortalBeUsable.get();
        portalLockingAdvancement = config.PORTAL.portalAdvancementLock.get();
        maxPortalSize = Math.max(4, config.PORTAL.maxPortalSize.get());
        casketUUIDLocking = config.casketUUIDLocking.get();
        disableSkullCandles = config.disableSkullCandles.get();
        defaultItemEnchants = config.defaultItemEnchants.get();
        bossDropChests = config.bossDropChests.get();
        commonCloudBlockPrecipitationDistance = Math.max(0, config.cloudBlockPrecipitationDistance.get());
        multiplayerFightAdjuster = config.multiplayerFightAdjuster.get();
        uncraftingXpCostMultiplier = Math.max(0.0D, config.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.get());
        repairingXpCostMultiplier = Math.max(0.0D, config.UNCRAFTING_STUFFS.repairingXpCostMultiplier.get());
        allowShapelessUncrafting = config.UNCRAFTING_STUFFS.allowShapelessUncrafting.get();
        disableIngredientSwitching = config.UNCRAFTING_STUFFS.disableIngredientSwitching.get();
        disableUncraftingRecipes.clear();
        disableUncraftingRecipes.addAll(config.UNCRAFTING_STUFFS.disableUncraftingRecipes.get());
        reverseRecipeBlacklist = config.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get();
        blacklistedUncraftingModIds.clear();
        blacklistedUncraftingModIds.addAll(config.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get());
        flipUncraftingModIdList = config.UNCRAFTING_STUFFS.flipUncraftingModIdList.get();
        disableUncraftingOnly = config.UNCRAFTING_STUFFS.disableUncraftingOnly.get();
        disableEntireTable = config.UNCRAFTING_STUFFS.disableEntireTable.get();
        disableTimeCore = config.MAGIC_TREES.disableTime.get();
        timeCoreRange = Math.max(0, config.MAGIC_TREES.timeRange.get());
        disableTransformationCore = config.MAGIC_TREES.disableTransformation.get();
        transformationRange(config.MAGIC_TREES.transformationRange.get());
        disableMiningCore = config.MAGIC_TREES.disableMining.get();
        miningCoreRange = Math.max(0, config.MAGIC_TREES.miningRange.get());
        disableSortingCore = config.MAGIC_TREES.disableSorting.get();
        sortingCoreRange = Math.max(0, config.MAGIC_TREES.sortingRange.get());
        parryNonTwilightAttacks = config.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get();
        shieldParryTicks = Math.max(0, config.SHIELD_INTERACTIONS.shieldParryTicks.get());
        syncMagicTreeMirror();
    }

    public static void rebakeClientOptions(TFClientConfig config) {
        silentCicadas = config.silentCicadas.get();
        silentCicadasOnHead = config.silentCicadasOnHead.get();
        firstPersonEffects = config.firstPersonEffects.get();
        rotateTrophyHeadsGui = config.rotateTrophyHeadsGui.get();
        disableOptifineNagScreen = config.disableOptifineNagScreen.get();
        disableLockedBiomeToasts = config.disableLockedBiomeToasts.get();
        showQuestRamCrosshairIndicator = config.showQuestRamCrosshairIndicator.get();
        showFortificationShieldIndicator = config.showFortificationShieldIndicator.get();
        showFortificationShieldIndicatorInCreative = config.showFortificationShieldIndicatorInCreative.get();
        clientCloudBlockPrecipitationDistance = Math.max(-1, config.cloudBlockPrecipitationDistance.get());
        setGiantSkinUUIDs(config.giantSkinUUIDs.get());
        auroraBiomes.clear();
        auroraBiomes.addAll(config.auroraBiomes.get());
        prettifyOreMeterGui = config.prettifyOreMeterGui.get();
        spawnCharmAnimationAsTotem = config.spawnCharmAnimationAsTotem.get();
        manualTravellersWingsGradualGlide = config.manualTravellersWingsGradualGlide.get();
        firstPersonGloveOverlay = config.firstPersonGloveOverlay.get();
        itemDisplayScreenOffsetX = config.ITEM_DISPLAY.screenOffsetX.get();
        itemDisplayScreenOffsetY = config.ITEM_DISPLAY.screenOffsetY.get();
        itemDisplayScreenScale = Math.max(0.1D, config.ITEM_DISPLAY.screenScale.get());
        twentyFourHourFormat = config.ITEM_DISPLAY.twentyFourHourFormat.get();
    }

    private static void transformationRange(int range) {
        transformationCoreRange = Math.max(0, range);
    }

    public static void setGiantSkinUUIDs(List<String> uuids) {
        giantSkinUUIDs.clear();
        GAME_PROFILES.clear();
        if (uuids == null) {
            return;
        }
        for (String uuid : uuids) {
            if (uuid == null || uuid.isBlank()) {
                continue;
            }
            try {
                UUID parsed = UUID.fromString(uuid);
                giantSkinUUIDs.add(parsed.toString());
                GAME_PROFILES.add(new GameProfile(parsed, null));
            } catch (IllegalArgumentException exception) {
                TwilightForestMod.LOGGER.warn("Ignoring invalid giant skin UUID '{}'", uuid);
            }
        }
    }

    public static boolean use24HourTimeDefault() {
        return java.time.format.DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                java.time.format.FormatStyle.SHORT,
                java.time.format.FormatStyle.SHORT,
                java.time.chrono.IsoChronology.INSTANCE,
                java.util.Locale.getDefault())
            .contains("H");
    }

    private static void syncMagicTreeMirror() {
        COMMON_CONFIG.MAGIC_TREES.disableTime = disableTimeCore;
        COMMON_CONFIG.MAGIC_TREES.timeRange = timeCoreRange;
        COMMON_CONFIG.MAGIC_TREES.disableMining = disableMiningCore;
        COMMON_CONFIG.MAGIC_TREES.miningRange = miningCoreRange;
        COMMON_CONFIG.MAGIC_TREES.disableSorting = disableSortingCore;
        COMMON_CONFIG.MAGIC_TREES.sortingRange = sortingCoreRange;
        COMMON_CONFIG.MAGIC_TREES.disableTransformation = disableTransformationCore;
        COMMON_CONFIG.MAGIC_TREES.transformationRange = transformationCoreRange;
    }

    private static final class ConfigData {
        boolean enforcedProgression = TFConfig.enforcedProgression;
        boolean bossDropChests = TFConfig.bossDropChests;
        String originDimension = TFConfig.originDimension;
        String portalLockingAdvancement = TFConfig.portalLockingAdvancement;
        boolean checkPortalPlacement = TFConfig.checkPortalPlacement;
        boolean destructivePortalLightning = TFConfig.destructivePortalLightning;
        boolean shouldReturnPortalBeUsable = TFConfig.shouldReturnPortalBeUsable;
        int maxPortalSize = TFConfig.maxPortalSize;
        boolean silentCicadas = TFConfig.silentCicadas;
        boolean silentCicadasOnHead = TFConfig.silentCicadasOnHead;
        boolean firstPersonEffects = TFConfig.firstPersonEffects;
        boolean rotateTrophyHeadsGui = TFConfig.rotateTrophyHeadsGui;
        boolean disableOptifineNagScreen = TFConfig.disableOptifineNagScreen;
        boolean disableLockedBiomeToasts = TFConfig.disableLockedBiomeToasts;
        boolean showQuestRamCrosshairIndicator = TFConfig.showQuestRamCrosshairIndicator;
        boolean showFortificationShieldIndicator = TFConfig.showFortificationShieldIndicator;
        boolean showFortificationShieldIndicatorInCreative = TFConfig.showFortificationShieldIndicatorInCreative;
        int clientCloudBlockPrecipitationDistance = TFConfig.clientCloudBlockPrecipitationDistance;
        List<String> giantSkinUUIDs = new ArrayList<>(TFConfig.giantSkinUUIDs);
        List<String> auroraBiomes = new ArrayList<>(TFConfig.auroraBiomes);
        boolean prettifyOreMeterGui = TFConfig.prettifyOreMeterGui;
        boolean spawnCharmAnimationAsTotem = TFConfig.spawnCharmAnimationAsTotem;
        boolean manualTravellersWingsGradualGlide = TFConfig.manualTravellersWingsGradualGlide;
        boolean firstPersonGloveOverlay = TFConfig.firstPersonGloveOverlay;
        int itemDisplayScreenOffsetX = TFConfig.itemDisplayScreenOffsetX;
        int itemDisplayScreenOffsetY = TFConfig.itemDisplayScreenOffsetY;
        double itemDisplayScreenScale = TFConfig.itemDisplayScreenScale;
        boolean twentyFourHourFormat = TFConfig.twentyFourHourFormat;
        boolean disableSkullCandles = TFConfig.disableSkullCandles;
        boolean casketUUIDLocking = TFConfig.casketUUIDLocking;
        boolean defaultItemEnchants = TFConfig.defaultItemEnchants;
        int commonCloudBlockPrecipitationDistance = TFConfig.commonCloudBlockPrecipitationDistance;
        boolean newPlayersSpawnInTF = TFConfig.newPlayersSpawnInTF;
        boolean portalForNewPlayerSpawn = TFConfig.portalForNewPlayerSpawn;
        boolean allowPortalsInOtherDimensions = TFConfig.allowPortalsInOtherDimensions;
        int portalCreationPermission = TFConfig.portalCreationPermission;
        boolean disablePortalCreation = TFConfig.disablePortalCreation;
        boolean parryNonTwilightAttacks = TFConfig.parryNonTwilightAttacks;
        int shieldParryTicks = TFConfig.shieldParryTicks;

        boolean disableEntireTable = TFConfig.disableEntireTable;
        boolean disableUncraftingOnly = TFConfig.disableUncraftingOnly;
        boolean flipUncraftingTableHotkey = TFConfig.flipUncraftingTableHotkey;
        boolean allowShapelessUncrafting = TFConfig.allowShapelessUncrafting;
        boolean disableIngredientSwitching = TFConfig.disableIngredientSwitching;
        boolean reverseRecipeBlacklist = TFConfig.reverseRecipeBlacklist;
        boolean flipUncraftingModIdList = TFConfig.flipUncraftingModIdList;
        double uncraftingXpCostMultiplier = TFConfig.uncraftingXpCostMultiplier;
        double repairingXpCostMultiplier = TFConfig.repairingXpCostMultiplier;
        List<String> disableUncraftingRecipes = new ArrayList<>(TFConfig.disableUncraftingRecipes);
        List<String> blacklistedUncraftingModIds = new ArrayList<>(TFConfig.blacklistedUncraftingModIds);

        boolean disableTimeCore = TFConfig.disableTimeCore;
        int timeCoreRange = TFConfig.timeCoreRange;
        boolean disableTransformationCore = TFConfig.disableTransformationCore;
        int transformationCoreRange = TFConfig.transformationCoreRange;
        boolean disableMiningCore = TFConfig.disableMiningCore;
        int miningCoreRange = TFConfig.miningCoreRange;
        boolean disableSortingCore = TFConfig.disableSortingCore;
        int sortingCoreRange = TFConfig.sortingCoreRange;
        MultiplayerFightAdjuster multiplayerFightAdjuster = TFConfig.multiplayerFightAdjuster;

        static ConfigData capture() {
            return new ConfigData();
        }

        void apply() {
            TFConfig.enforcedProgression = this.enforcedProgression;
            TFConfig.bossDropChests = this.bossDropChests;
            TFConfig.originDimension = this.originDimension == null || this.originDimension.isBlank()
                    ? Level.OVERWORLD.location().toString()
                    : this.originDimension;
            TFConfig.portalLockingAdvancement = this.portalLockingAdvancement == null ? "" : this.portalLockingAdvancement;
            TFConfig.checkPortalPlacement = this.checkPortalPlacement;
            TFConfig.destructivePortalLightning = this.destructivePortalLightning;
            TFConfig.shouldReturnPortalBeUsable = this.shouldReturnPortalBeUsable;
            TFConfig.maxPortalSize = Math.max(4, this.maxPortalSize);
            TFConfig.silentCicadas = this.silentCicadas;
            TFConfig.silentCicadasOnHead = this.silentCicadasOnHead;
            TFConfig.firstPersonEffects = this.firstPersonEffects;
            TFConfig.rotateTrophyHeadsGui = this.rotateTrophyHeadsGui;
            TFConfig.disableOptifineNagScreen = this.disableOptifineNagScreen;
            TFConfig.disableLockedBiomeToasts = this.disableLockedBiomeToasts;
            TFConfig.showQuestRamCrosshairIndicator = this.showQuestRamCrosshairIndicator;
            TFConfig.showFortificationShieldIndicator = this.showFortificationShieldIndicator;
            TFConfig.showFortificationShieldIndicatorInCreative = this.showFortificationShieldIndicatorInCreative;
            TFConfig.clientCloudBlockPrecipitationDistance = Math.max(-1, this.clientCloudBlockPrecipitationDistance);
            TFConfig.setGiantSkinUUIDs(this.giantSkinUUIDs);
            TFConfig.auroraBiomes.clear();
            if (this.auroraBiomes != null) {
                TFConfig.auroraBiomes.addAll(this.auroraBiomes);
            }
            TFConfig.prettifyOreMeterGui = this.prettifyOreMeterGui;
            TFConfig.spawnCharmAnimationAsTotem = this.spawnCharmAnimationAsTotem;
            TFConfig.manualTravellersWingsGradualGlide = this.manualTravellersWingsGradualGlide;
            TFConfig.firstPersonGloveOverlay = this.firstPersonGloveOverlay;
            TFConfig.itemDisplayScreenOffsetX = this.itemDisplayScreenOffsetX;
            TFConfig.itemDisplayScreenOffsetY = this.itemDisplayScreenOffsetY;
            TFConfig.itemDisplayScreenScale = Math.max(0.1D, this.itemDisplayScreenScale);
            TFConfig.twentyFourHourFormat = this.twentyFourHourFormat;
            TFConfig.disableSkullCandles = this.disableSkullCandles;
            TFConfig.casketUUIDLocking = this.casketUUIDLocking;
            TFConfig.defaultItemEnchants = this.defaultItemEnchants;
            TFConfig.commonCloudBlockPrecipitationDistance = this.commonCloudBlockPrecipitationDistance;
            TFConfig.newPlayersSpawnInTF = this.newPlayersSpawnInTF;
            TFConfig.portalForNewPlayerSpawn = this.portalForNewPlayerSpawn;
            TFConfig.allowPortalsInOtherDimensions = this.allowPortalsInOtherDimensions;
            TFConfig.portalCreationPermission = Math.max(0, this.portalCreationPermission);
            TFConfig.disablePortalCreation = this.disablePortalCreation;
            TFConfig.parryNonTwilightAttacks = this.parryNonTwilightAttacks;
            TFConfig.shieldParryTicks = Math.max(0, this.shieldParryTicks);

            TFConfig.disableEntireTable = this.disableEntireTable;
            TFConfig.disableUncraftingOnly = this.disableUncraftingOnly;
            TFConfig.flipUncraftingTableHotkey = this.flipUncraftingTableHotkey;
            TFConfig.allowShapelessUncrafting = this.allowShapelessUncrafting;
            TFConfig.disableIngredientSwitching = this.disableIngredientSwitching;
            TFConfig.reverseRecipeBlacklist = this.reverseRecipeBlacklist;
            TFConfig.flipUncraftingModIdList = this.flipUncraftingModIdList;
            TFConfig.uncraftingXpCostMultiplier = this.uncraftingXpCostMultiplier;
            TFConfig.repairingXpCostMultiplier = this.repairingXpCostMultiplier;
            TFConfig.disableUncraftingRecipes.clear();
            if (this.disableUncraftingRecipes != null) {
                TFConfig.disableUncraftingRecipes.addAll(this.disableUncraftingRecipes);
            }
            TFConfig.blacklistedUncraftingModIds.clear();
            if (this.blacklistedUncraftingModIds != null) {
                TFConfig.blacklistedUncraftingModIds.addAll(this.blacklistedUncraftingModIds);
            }

            TFConfig.disableTimeCore = this.disableTimeCore;
            TFConfig.timeCoreRange = Math.max(0, this.timeCoreRange);
            TFConfig.disableTransformationCore = this.disableTransformationCore;
            TFConfig.transformationCoreRange = Math.max(0, this.transformationCoreRange);
            TFConfig.disableMiningCore = this.disableMiningCore;
            TFConfig.miningCoreRange = Math.max(0, this.miningCoreRange);
            TFConfig.disableSortingCore = this.disableSortingCore;
            TFConfig.sortingCoreRange = Math.max(0, this.sortingCoreRange);
            TFConfig.multiplayerFightAdjuster = this.multiplayerFightAdjuster == null
                    ? MultiplayerFightAdjuster.NONE
                    : this.multiplayerFightAdjuster;
        }
    }

    public enum MultiplayerFightAdjuster {
        NONE(false, false),
        MORE_LOOT(true, false),
        MORE_HEALTH(false, true),
        MORE_LOOT_AND_HEALTH(true, true);

        private final boolean moreLoot;
        private final boolean moreHealth;

        MultiplayerFightAdjuster(boolean loot, boolean health) {
            this.moreLoot = loot;
            this.moreHealth = health;
        }

        public boolean adjustsLootRolls() {
            return this.moreLoot;
        }

        public boolean adjustsHealth() {
            return this.moreHealth;
        }
    }
}
