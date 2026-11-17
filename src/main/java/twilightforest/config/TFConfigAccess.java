package twilightforest.config;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFBiomes;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public interface TFConfigAccess {
	default @Nullable Identifier getPortalLockingAdvancement(Player player) {
		return null;
	}

	default List<Holder<Biome>> getValidAuroraBiomes(RegistryAccess access) {
		return List.of(access.carminite$holderOrThrow(TFBiomes.GLACIER));
	}

	default List<GameProfile> getGameProfiles() {
		return new ArrayList<>();
	}

	default boolean getSilentCicadas() {
		return false;
	}

	default boolean getSilentCicadasOnHead() {
		return false;
	}

	default boolean getFirstPersonEffects() {
		return true;
	}

	default boolean getRotateTrophyHeadsGui() {
		return true;
	}

	default boolean getDisableLockedBiomeToasts() {
		return false;
	}

	default boolean getShowQuestRamCrosshairIndicator() {
		return true;
	}

	default boolean getShowFortificationShieldIndicator() {
		return true;
	}

	default boolean getShowFortificationShieldIndicatorInCreative() {
		return false;
	}

	default int getClientCloudBlockPrecipitationDistance() {
		return 32;
	}

	default boolean getPrettifyOreMeterGui() {
		return true;
	}

	default boolean getSpawnCharmAnimationAsTotem() {
		return false;
	}

	default boolean getManualTravellersWingsGradualGlideDefault() {
		return true;
	}

	default boolean getFirstPersonGloveOverlay() {
		return true;
	}

	default int getItemDisplayXOffs() {
		return 4;
	}

	default int getItemDisplayYOffs() {
		return 4;
	}

	default double getItemDisplayScale() {
		return 1.0D;
	}

	default boolean getClock24HourFormat() {
		return use24HourTimeDefault();
	}

	default boolean getCasketUUIDLocking() {
		return false;
	}

	default boolean getDisableSkullCandles() {
		return false;
	}

	default boolean getDefaultItemEnchants() {
		return true;
	}

	default boolean getBossDropChests() {
		return true;
	}

	default int getCommonCloudBlockPrecipitationDistance() {
		return 32;
	}

	default boolean getNewPlayersSpawnInTF() {
		return false;
	}

	default boolean getPortalForNewPlayerSpawn() {
		return true;
	}

	default String getOriginDimension() {
		return Level.OVERWORLD.identifier().toString();
	}

	default boolean getAllowPortalsInOtherDimensions() {
		return false;
	}

	default PermissionLevel getPortalCreationPermission() {
		return PermissionLevel.ALL;
	}

	default boolean getDisablePortalCreation() {
		return false;
	}

	default boolean getCheckPortalPlacement() {
		return true;
	}

	default boolean getDestructivePortalLightning() {
		return true;
	}

	default boolean getShouldReturnPortalBeUsable() {
		return true;
	}

	default int getMaxPortalSize() {
		return 64;
	}

	default double getUncraftingXpCostMultiplier() {
		return 1.0D;
	}

	default double getRepairingXpCostMultiplier() {
		return 1.0D;
	}

	default boolean getAllowShapelessUncrafting() {
		return false;
	}

	default boolean getDisableIngredientSwitching() {
		return false;
	}

	default List<? extends String> getDisableUncraftingRecipes() {
		return List.of();
	}

	default boolean getReverseRecipeBlacklist() {
		return false;
	}

	default List<? extends String> getBlacklistedUncraftingModIds() {
		return List.of();
	}

	default boolean getFlipUncraftingModIdList() {
		return false;
	}

	default boolean getDisableUncraftingOnly() {
		return false;
	}

	default boolean getDisableEntireTable() {
		return false;
	}

	default int getTimeCoreRange() {
		return 16;
	}

	default int getTransformationCoreRange() {
		return 16;
	}

	default int getMiningCoreRange() {
		return 16;
	}

	default int getSortingCoreRange() {
		return 16;
	}

	default boolean getDisableTimeCore() {
		return false;
	}

	default boolean getDisableTransformationCore() {
		return false;
	}

	default boolean getDisableMiningCore() {
		return false;
	}

	default boolean getDisableSortingCore() {
		return false;
	}

	default boolean getParryNonTwilightAttacks() {
		return false;
	}

	default int getShieldParryTicks() {
		return 40;
	}

	default boolean getMultiplayerAdjustHealth() {
		return false;
	}

	default boolean getMultiplayerAdjustLootRolls() {
		return false;
	}

	static boolean use24HourTimeDefault() {
		try {
			String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, FormatStyle.SHORT, IsoChronology.INSTANCE, Locale.getDefault());
			return !pattern.contains("a");  // "a" is used to display AM / PM.
		} catch (Throwable throwable) {
			return true;
		}
	}
}
