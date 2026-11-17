package twilightforest.compat.config;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jspecify.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.config.TFConfigAccess;
import twilightforest.util.PlayerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class FCAPConfig implements TFConfigAccess {
	public static final String CONFIG_ID = "config." + TFCommon.ID + ".";

	@Nullable
	public static Identifier portalLockingAdvancement;
	public static final List<Holder<Biome>> VALID_AURORA_BIOMES = new ArrayList<>();
	public static final List<GameProfile> GAME_PROFILES = new ArrayList<>();

	// --- CLIENT ---
	public static boolean silentCicadas = false;
	public static boolean silentCicadasOnHead = false;
	public static boolean firstPersonEffects = true;
	public static boolean rotateTrophyHeadsGui = true;
	public static boolean disableLockedBiomeToasts = false;
	public static boolean showQuestRamCrosshairIndicator = true;
	public static boolean showFortificationShieldIndicator = true;
	public static boolean showFortificationShieldIndicatorInCreative = false;
	public static int clientCloudBlockPrecipitationDistance = 32;
	public static boolean prettifyOreMeterGui = true;
	public static boolean spawnCharmAnimationAsTotem = false;
	public static boolean manualTravellersWingsGradualGlideDefault = true;
	public static boolean firstPersonGloveOverlay = true;

	// --- Item Display ---
	public static int itemDisplayXOffs = 4;
	public static int itemDisplayYOffs = 4;
	public static double itemDisplayScale = 1.0D;
	public static boolean clock24HourFormat = TFConfigAccess.use24HourTimeDefault();

	// --- COMMON ---
	public static boolean casketUUIDLocking = false;
	public static boolean disableSkullCandles = false;
	public static boolean defaultItemEnchants = true;
	public static boolean bossDropChests = true;
	public static MultiplayerFightAdjuster multiplayerFightAdjuster = MultiplayerFightAdjuster.NONE;
	public static int commonCloudBlockPrecipitationDistance = 32;

	// -- Dimension --
	public static boolean newPlayersSpawnInTF = false;
	public static boolean portalForNewPlayerSpawn = true;

	// -- Portal --
	public static String originDimension = Level.OVERWORLD.identifier().toString();
	public static boolean allowPortalsInOtherDimensions = false;
	public static PermissionLevel portalCreationPermission = PermissionLevel.ALL;
	public static boolean disablePortalCreation = false;
	public static boolean checkPortalPlacement = true;
	public static boolean destructivePortalLightning = true;
	public static boolean shouldReturnPortalBeUsable = true;
	public static int maxPortalSize = 64;

	// -- Uncrafting Table --
	public static double uncraftingXpCostMultiplier = 1.0D;
	public static double repairingXpCostMultiplier = 1.0D;
	public static boolean allowShapelessUncrafting = false;
	public static boolean disableIngredientSwitching = false;
	public static List<? extends String> disableUncraftingRecipes = new ArrayList<>();
	public static boolean reverseRecipeBlacklist = false;
	public static List<? extends String> blacklistedUncraftingModIds = new ArrayList<>();
	public static boolean flipUncraftingModIdList = false;
	public static boolean disableUncraftingOnly = false;
	public static boolean disableEntireTable = false;

	// -- Magic Trees --
	public static boolean disableTimeCore = false;
	public static int timeCoreRange = 16;
	public static boolean disableTransformationCore = false;
	public static int transformationCoreRange = 16;
	public static boolean disableMiningCore = false;
	public static int miningCoreRange = 16;
	public static boolean disableSortingCore = false;
	public static int sortingCoreRange = 16;

	// -- Shield Parrying --
	public static boolean parryNonTwilightAttacks = false;
	public static int shieldParryTicks = 40;

	@Override
	public @Nullable Identifier getPortalLockingAdvancement(Player player) {
		//only run assigning logic if the config has an advancement set and the RL is null
		if (portalLockingAdvancement == null && !ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.get().isEmpty()) {

			Identifier lock = Identifier.tryParse(ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.get());
			if (lock == null || PlayerHelper.getAdvancement(player, lock) == null) {
				//if the RL is not a valid advancement fail us
				TFCommon.LOGGER.error("The portal locking advancement is not a valid advancement! Setting to null!");
				ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.set("");
			} else {
				portalLockingAdvancement = Identifier.tryParse(ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.get());
				TFCommon.LOGGER.debug("Portal locking advancement reloaded. Current advancement to check for is: {}", portalLockingAdvancement);
			}
		}
		//always return the RL, even if its null. We can use this to run logic less often
		return portalLockingAdvancement;
	}

	//Forge's biome registry doesn't contain biomes done via datapacks, so we have to use registryaccess
	@Override
	public List<Holder<Biome>> getValidAuroraBiomes(RegistryAccess access) {
		if (VALID_AURORA_BIOMES.isEmpty() && !ConfigSetup.CLIENT_CONFIG.auroraBiomes.get().isEmpty()) {
			ConfigSetup.CLIENT_CONFIG.auroraBiomes.get().forEach(s -> {
				Optional<Holder<Biome>> holder = Optional.ofNullable(Identifier.tryParse(s)).flatMap(key -> access.lookupOrThrow(Registries.BIOME).get(key));
				if (holder.isEmpty()) {
					TFCommon.LOGGER.warn("Biome {} in Twilight Forest's validAuroraBiomes config option is not a valid biome. Skipping!", s);
				} else {
					VALID_AURORA_BIOMES.add(holder.get());
				}
			});
		}
		return VALID_AURORA_BIOMES;
	}

	@Override
	public List<GameProfile> getGameProfiles() {
		return GAME_PROFILES;
	}

	@Override
	public boolean getSilentCicadas() {
		return silentCicadas;
	}

	@Override
	public boolean getSilentCicadasOnHead() {
		return silentCicadasOnHead;
	}

	@Override
	public boolean getFirstPersonEffects() {
		return firstPersonEffects;
	}

	@Override
	public boolean getRotateTrophyHeadsGui() {
		return rotateTrophyHeadsGui;
	}

	@Override
	public boolean getDisableLockedBiomeToasts() {
		return disableLockedBiomeToasts;
	}

	@Override
	public boolean getShowQuestRamCrosshairIndicator() {
		return showQuestRamCrosshairIndicator;
	}

	@Override
	public boolean getShowFortificationShieldIndicator() {
		return showFortificationShieldIndicator;
	}

	@Override
	public boolean getShowFortificationShieldIndicatorInCreative() {
		return showFortificationShieldIndicatorInCreative;
	}

	@Override
	public int getClientCloudBlockPrecipitationDistance() {
		return clientCloudBlockPrecipitationDistance == -1 ? commonCloudBlockPrecipitationDistance : clientCloudBlockPrecipitationDistance;
	}

	@Override
	public boolean getPrettifyOreMeterGui() {
		return prettifyOreMeterGui;
	}

	@Override
	public boolean getSpawnCharmAnimationAsTotem() {
		return spawnCharmAnimationAsTotem;
	}

	@Override
	public boolean getManualTravellersWingsGradualGlideDefault() {
		return manualTravellersWingsGradualGlideDefault;
	}

	@Override
	public boolean getFirstPersonGloveOverlay() {
		return firstPersonGloveOverlay;
	}

	@Override
	public int getItemDisplayXOffs() {
		return itemDisplayXOffs;
	}

	@Override
	public int getItemDisplayYOffs() {
		return itemDisplayYOffs;
	}

	@Override
	public double getItemDisplayScale() {
		return itemDisplayScale;
	}

	@Override
	public boolean getClock24HourFormat() {
		return clock24HourFormat;
	}

	@Override
	public boolean getCasketUUIDLocking() {
		return casketUUIDLocking;
	}

	@Override
	public boolean getDisableSkullCandles() {
		return disableSkullCandles;
	}

	@Override
	public boolean getDefaultItemEnchants() {
		return defaultItemEnchants;
	}

	@Override
	public boolean getBossDropChests() {
		return bossDropChests;
	}

	@Override
	public int getCommonCloudBlockPrecipitationDistance() {
		return commonCloudBlockPrecipitationDistance;
	}

	@Override
	public boolean getNewPlayersSpawnInTF() {
		return newPlayersSpawnInTF;
	}

	@Override
	public boolean getPortalForNewPlayerSpawn() {
		return portalForNewPlayerSpawn;
	}

	@Override
	public String getOriginDimension() {
		return originDimension;
	}

	@Override
	public boolean getAllowPortalsInOtherDimensions() {
		return allowPortalsInOtherDimensions;
	}

	@Override
	public PermissionLevel getPortalCreationPermission() {
		return portalCreationPermission;
	}

	@Override
	public boolean getDisablePortalCreation() {
		return disablePortalCreation;
	}

	@Override
	public boolean getCheckPortalPlacement() {
		return checkPortalPlacement;
	}

	@Override
	public boolean getDestructivePortalLightning() {
		return destructivePortalLightning;
	}

	@Override
	public boolean getShouldReturnPortalBeUsable() {
		return shouldReturnPortalBeUsable;
	}

	@Override
	public int getMaxPortalSize() {
		return maxPortalSize;
	}

	@Override
	public double getUncraftingXpCostMultiplier() {
		return uncraftingXpCostMultiplier;
	}

	@Override
	public double getRepairingXpCostMultiplier() {
		return repairingXpCostMultiplier;
	}

	@Override
	public boolean getAllowShapelessUncrafting() {
		return allowShapelessUncrafting;
	}

	@Override
	public boolean getDisableIngredientSwitching() {
		return disableIngredientSwitching;
	}

	@Override
	public List<? extends String> getDisableUncraftingRecipes() {
		return disableUncraftingRecipes;
	}

	@Override
	public boolean getReverseRecipeBlacklist() {
		return reverseRecipeBlacklist;
	}

	@Override
	public List<? extends String> getBlacklistedUncraftingModIds() {
		return blacklistedUncraftingModIds;
	}

	@Override
	public boolean getFlipUncraftingModIdList() {
		return flipUncraftingModIdList;
	}

	@Override
	public boolean getDisableUncraftingOnly() {
		return disableUncraftingOnly;
	}

	@Override
	public boolean getDisableEntireTable() {
		return disableEntireTable;
	}

	@Override
	public int getTimeCoreRange() {
		return timeCoreRange;
	}

	@Override
	public int getTransformationCoreRange() {
		return transformationCoreRange;
	}

	@Override
	public int getMiningCoreRange() {
		return miningCoreRange;
	}

	@Override
	public int getSortingCoreRange() {
		return sortingCoreRange;
	}

	@Override
	public boolean getDisableTimeCore() {
		return disableTimeCore;
	}

	@Override
	public boolean getDisableTransformationCore() {
		return disableTransformationCore;
	}

	@Override
	public boolean getDisableMiningCore() {
		return disableMiningCore;
	}

	@Override
	public boolean getDisableSortingCore() {
		return disableSortingCore;
	}

	@Override
	public boolean getParryNonTwilightAttacks() {
		return parryNonTwilightAttacks;
	}

	@Override
	public int getShieldParryTicks() {
		return shieldParryTicks;
	}

	@Override
	public boolean getMultiplayerAdjustHealth() {
		return multiplayerFightAdjuster.adjustsHealth();
	}

	@Override
	public boolean getMultiplayerAdjustLootRolls() {
		return multiplayerFightAdjuster.adjustsLootRolls();
	}

	public enum MultiplayerFightAdjuster implements TranslatableEnum {
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

		@Override
		public Component getTranslatedName() {
			return Component.translatable(CONFIG_ID + "multiplayer_fight_adjuster." + this.name().toLowerCase(Locale.ROOT));
		}
	}
}
