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
import java.util.List;
import java.util.Locale;

public final class DefaultConfig implements TFConfigAccess {

	@Override
	public @Nullable Identifier getPortalLockingAdvancement(Player player) {
		return null;
	}

	@Override
	public List<Holder<Biome>> getValidAuroraBiomes(RegistryAccess access) {
		return List.of(access.carminite$holderOrThrow(TFBiomes.GLACIER));
	}

	@Override
	public List<GameProfile> getGameProfiles() {
		return List.of();
	}

	@Override
	public boolean getSilentCicadas() {
		return false;
	}

	@Override
	public boolean getSilentCicadasOnHead() {
		return false;
	}

	@Override
	public boolean getFirstPersonEffects() {
		return true;
	}

	@Override
	public boolean getRotateTrophyHeadsGui() {
		return true;
	}

	@Override
	public boolean getDisableLockedBiomeToasts() {
		return false;
	}

	@Override
	public boolean getShowQuestRamCrosshairIndicator() {
		return true;
	}

	@Override
	public boolean getShowFortificationShieldIndicator() {
		return true;
	}

	@Override
	public boolean getShowFortificationShieldIndicatorInCreative() {
		return false;
	}

	@Override
	public int getClientCloudBlockPrecipitationDistance() {
		return 32;
	}

	@Override
	public boolean getPrettifyOreMeterGui() {
		return true;
	}

	@Override
	public boolean getSpawnCharmAnimationAsTotem() {
		return false;
	}

	@Override
	public boolean getManualTravellersWingsGradualGlideDefault() {
		return true;
	}

	@Override
	public boolean getFirstPersonGloveOverlay() {
		return true;
	}

	@Override
	public int getItemDisplayXOffs() {
		return 4;
	}

	@Override
	public int getItemDisplayYOffs() {
		return 4;
	}

	@Override
	public double getItemDisplayScale() {
		return 1.0D;
	}

	@Override
	public boolean getClock24HourFormat() {
		return use24HourTimeDefault();
	}

	@Override
	public boolean getCasketUUIDLocking() {
		return false;
	}

	@Override
	public boolean getDisableSkullCandles() {
		return false;
	}

	@Override
	public boolean getDefaultItemEnchants() {
		return true;
	}

	@Override
	public boolean getBossDropChests() {
		return true;
	}

	@Override
	public int getCommonCloudBlockPrecipitationDistance() {
		return 32;
	}

	@Override
	public boolean getNewPlayersSpawnInTF() {
		return false;
	}

	@Override
	public boolean getPortalForNewPlayerSpawn() {
		return true;
	}

	@Override
	public String getOriginDimension() {
		return Level.OVERWORLD.identifier().toString();
	}

	@Override
	public boolean getAllowPortalsInOtherDimensions() {
		return false;
	}

	@Override
	public PermissionLevel getPortalCreationPermission() {
		return PermissionLevel.ALL;
	}

	@Override
	public boolean getDisablePortalCreation() {
		return false;
	}

	@Override
	public boolean getCheckPortalPlacement() {
		return true;
	}

	@Override
	public boolean getDestructivePortalLightning() {
		return true;
	}

	@Override
	public boolean getShouldReturnPortalBeUsable() {
		return true;
	}

	@Override
	public int getMaxPortalSize() {
		return 64;
	}

	@Override
	public double getUncraftingXpCostMultiplier() {
		return 1.0D;
	}

	@Override
	public double getRepairingXpCostMultiplier() {
		return 1.0D;
	}

	@Override
	public boolean getAllowShapelessUncrafting() {
		return false;
	}

	@Override
	public boolean getDisableIngredientSwitching() {
		return false;
	}

	@Override
	public List<? extends String> getDisableUncraftingRecipes() {
		return List.of();
	}

	@Override
	public boolean getReverseRecipeBlacklist() {
		return false;
	}

	@Override
	public List<? extends String> getBlacklistedUncraftingModIds() {
		return List.of();
	}

	@Override
	public boolean getFlipUncraftingModIdList() {
		return false;
	}

	@Override
	public boolean getDisableUncraftingOnly() {
		return false;
	}

	@Override
	public boolean getDisableEntireTable() {
		return false;
	}

	@Override
	public int getTimeCoreRange() {
		return 16;
	}

	@Override
	public int getTransformationCoreRange() {
		return 16;
	}

	@Override
	public int getMiningCoreRange() {
		return 16;
	}

	@Override
	public int getSortingCoreRange() {
		return 16;
	}

	@Override
	public boolean getDisableTimeCore() {
		return false;
	}

	@Override
	public boolean getDisableTransformationCore() {
		return false;
	}

	@Override
	public boolean getDisableMiningCore() {
		return false;
	}

	@Override
	public boolean getDisableSortingCore() {
		return false;
	}

	@Override
	public boolean getParryNonTwilightAttacks() {
		return false;
	}

	@Override
	public int getShieldParryTicks() {
		return 40;
	}

	@Override
	public boolean getMultiplayerAdjustHealth() {
		return false;
	}

	@Override
	public boolean getMultiplayerAdjustLootRolls() {
		return false;
	}

	public static boolean use24HourTimeDefault() {
		try {
			String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, FormatStyle.SHORT, IsoChronology.INSTANCE, Locale.getDefault());
			return !pattern.contains("a");  // "a" is used to display AM / PM.
		} catch (Throwable throwable) {
			return true;
		}
	}
}
