package twilightforest.config;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface TFConfigAccess {
	default @Nullable Identifier getPortalLockingAdvancement(Player player) {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default List<Holder<Biome>> getValidAuroraBiomes(RegistryAccess access) {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default List<GameProfile> getGameProfiles() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getSilentCicadas() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getSilentCicadasOnHead() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getFirstPersonEffects() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getRotateTrophyHeadsGui() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableLockedBiomeToasts() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getShowQuestRamCrosshairIndicator() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getShowFortificationShieldIndicator() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getShowFortificationShieldIndicatorInCreative() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getClientCloudBlockPrecipitationDistance() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getPrettifyOreMeterGui() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getSpawnCharmAnimationAsTotem() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getManualTravellersWingsGradualGlideDefault() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getFirstPersonGloveOverlay() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getItemDisplayXOffs() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getItemDisplayYOffs() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default double getItemDisplayScale() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getClock24HourFormat() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getCasketUUIDLocking() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableSkullCandles() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDefaultItemEnchants() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getBossDropChests() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getCommonCloudBlockPrecipitationDistance() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getNewPlayersSpawnInTF() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getPortalForNewPlayerSpawn() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default String getOriginDimension() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getAllowPortalsInOtherDimensions() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default PermissionLevel getPortalCreationPermission() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisablePortalCreation() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getCheckPortalPlacement() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDestructivePortalLightning() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getShouldReturnPortalBeUsable() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getMaxPortalSize() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default double getUncraftingXpCostMultiplier() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default double getRepairingXpCostMultiplier() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getAllowShapelessUncrafting() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableIngredientSwitching() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default List<? extends String> getDisableUncraftingRecipes() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getReverseRecipeBlacklist() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default List<? extends String> getBlacklistedUncraftingModIds() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getFlipUncraftingModIdList() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableUncraftingOnly() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableEntireTable() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getTimeCoreRange() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getTransformationCoreRange() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getMiningCoreRange() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getSortingCoreRange() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableTimeCore() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableTransformationCore() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableMiningCore() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getDisableSortingCore() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getParryNonTwilightAttacks() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default int getShieldParryTicks() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getMultiplayerAdjustHealth() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}

	default boolean getMultiplayerAdjustLootRolls() {
		throw new AssertionError("Must be implemented in a subclass of TFConfigAccess");
	}
}
