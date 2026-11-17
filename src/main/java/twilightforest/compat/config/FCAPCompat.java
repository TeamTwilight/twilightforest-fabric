package twilightforest.compat.config;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TFCommon;

import java.net.Proxy;
import java.util.UUID;

public final class FCAPCompat {
	private static final ModConfigSpec CLIENT_SPEC;
	private static final ModConfigSpec SERVER_SPEC;
	private static final ModConfigSpec COMMON_SPEC;

	public static final TFClientConfig CLIENT_CONFIG;
	public static final TFServerConfig SERVER_CONFIG;
	public static final TFCommonConfig COMMON_CONFIG;

	public static void setup() {
		TFCommon.LOGGER.info("Initializing Forge Config API Port compat!");
		TFCommon.setConfig(new FCAPConfig());
	}

	static {
		final Pair<TFCommonConfig, ModConfigSpec> specPairCommon = new ModConfigSpec.Builder().configure(TFCommonConfig::new);
		final Pair<TFServerConfig, ModConfigSpec> specPairServer = new ModConfigSpec.Builder().configure(TFServerConfig::new);
		final Pair<TFClientConfig, ModConfigSpec> specPairClient = new ModConfigSpec.Builder().configure(TFClientConfig::new);

		COMMON_CONFIG = specPairCommon.getLeft();
		SERVER_CONFIG = specPairServer.getLeft();
		CLIENT_CONFIG = specPairClient.getLeft();

		COMMON_SPEC = specPairCommon.getRight();
		SERVER_SPEC = specPairServer.getRight();
		CLIENT_SPEC = specPairClient.getRight();

		// Register these listeners before registering the configs, or else defaults persist until the file is changed
		ModConfigEvents.loading(TFCommon.ID).register(FCAPCompat::loadConfigs);
		ModConfigEvents.reloading(TFCommon.ID).register(FCAPCompat::reloadConfigs);

		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.COMMON, COMMON_SPEC);
		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.SERVER, SERVER_SPEC);
		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.CLIENT, CLIENT_SPEC);
	}

	public static void loadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == SERVER_SPEC) {
			rebakeServerOptions(SERVER_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	public static void reloadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == SERVER_SPEC) {
			rebakeServerOptions(SERVER_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	private static void rebakeCommonOptions(TFCommonConfig config) {
		FCAPConfig.casketUUIDLocking = config.casketUUIDLocking.get();
		FCAPConfig.disableSkullCandles = config.disableSkullCandles.get();
		FCAPConfig.defaultItemEnchants = config.defaultItemEnchants.get();
		FCAPConfig.bossDropChests = config.bossDropChests.get();
		FCAPConfig.commonCloudBlockPrecipitationDistance = config.cloudBlockPrecipitationDistance.get();
		FCAPConfig.multiplayerFightAdjuster = config.multiplayerFightAdjuster.get();

		//Dimension
		FCAPConfig.newPlayersSpawnInTF = config.DIMENSION.newPlayersSpawnInTF.get();
		FCAPConfig.portalForNewPlayerSpawn = config.DIMENSION.portalForNewPlayerSpawn.get();

		//Portal
		FCAPConfig.originDimension = config.PORTAL.originDimension.get();
		FCAPConfig.allowPortalsInOtherDimensions = config.PORTAL.allowPortalsInOtherDimensions.get();
		FCAPConfig.portalCreationPermission = config.PORTAL.portalCreationPermission.get();
		FCAPConfig.disablePortalCreation = config.PORTAL.disablePortalCreation.get();
		FCAPConfig.checkPortalPlacement = config.PORTAL.checkPortalPlacement.get();
		FCAPConfig.destructivePortalLightning = config.PORTAL.destructivePortalLightning.get();
		FCAPConfig.shouldReturnPortalBeUsable = config.PORTAL.shouldReturnPortalBeUsable.get();
		FCAPConfig.maxPortalSize = config.PORTAL.maxPortalSize.get();

		// Tree Cores
		FCAPConfig.disableTimeCore = config.MAGIC_TREES.timeRange.get() <= 0;
		FCAPConfig.timeCoreRange = config.MAGIC_TREES.timeRange.get();
		FCAPConfig.disableTransformationCore = config.MAGIC_TREES.transformationRange.get() <= 0;
		FCAPConfig.transformationCoreRange = config.MAGIC_TREES.transformationRange.get();
		FCAPConfig.disableMiningCore = config.MAGIC_TREES.miningRange.get() <= 0;
		FCAPConfig.miningCoreRange = config.MAGIC_TREES.miningRange.get();
		FCAPConfig.disableSortingCore = config.MAGIC_TREES.sortingRange.get() <= 0;
		FCAPConfig.sortingCoreRange = config.MAGIC_TREES.sortingRange.get();

		//Parrying
		FCAPConfig.parryNonTwilightAttacks = config.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get();
		FCAPConfig.shieldParryTicks = config.SHIELD_INTERACTIONS.shieldParryTicks.get();

		//sets cached portal locking advancement to null just in case it changed
		FCAPConfig.portalLockingAdvancement = null;
	}

	private static void rebakeServerOptions(TFServerConfig config) {
		//Uncrafting Table
		FCAPConfig.uncraftingXpCostMultiplier = config.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.get();
		FCAPConfig.repairingXpCostMultiplier = config.UNCRAFTING_STUFFS.repairingXpCostMultiplier.get();
		FCAPConfig.allowShapelessUncrafting = config.UNCRAFTING_STUFFS.allowShapelessUncrafting.get();
		FCAPConfig.disableIngredientSwitching = config.UNCRAFTING_STUFFS.disableIngredientSwitching.get();
		FCAPConfig.disableUncraftingOnly = config.UNCRAFTING_STUFFS.disableUncraftingOnly.get();
		FCAPConfig.disableEntireTable = config.UNCRAFTING_STUFFS.disableEntireTable.get();
		FCAPConfig.disableUncraftingRecipes = config.UNCRAFTING_STUFFS.disableUncraftingRecipes.get();
		FCAPConfig.reverseRecipeBlacklist = config.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get();
		FCAPConfig.blacklistedUncraftingModIds = config.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get();
		FCAPConfig.flipUncraftingModIdList = config.UNCRAFTING_STUFFS.flipUncraftingModIdList.get();
	}

	private static void rebakeClientOptions(TFClientConfig config) {
		reloadGiantSkins(config);
		FCAPConfig.VALID_AURORA_BIOMES.clear();
		FCAPConfig.silentCicadas = config.silentCicadas.get();
		FCAPConfig.silentCicadasOnHead = config.silentCicadasOnHead.get();
		FCAPConfig.firstPersonEffects = config.firstPersonEffects.get();
		FCAPConfig.rotateTrophyHeadsGui = config.rotateTrophyHeadsGui.get();
		FCAPConfig.disableLockedBiomeToasts = config.disableLockedBiomeToasts.get();
		FCAPConfig.showFortificationShieldIndicator = config.showFortificationShieldIndicator.get();
		FCAPConfig.showFortificationShieldIndicatorInCreative = config.showFortificationShieldIndicatorInCreative.get();
		FCAPConfig.showQuestRamCrosshairIndicator = config.showQuestRamCrosshairIndicator.get();
		FCAPConfig.clientCloudBlockPrecipitationDistance = config.cloudBlockPrecipitationDistance.get();
		FCAPConfig.prettifyOreMeterGui = config.prettifyOreMeterGui.get();
		FCAPConfig.spawnCharmAnimationAsTotem = config.spawnCharmAnimationAsTotem.get();
		FCAPConfig.manualTravellersWingsGradualGlideDefault = config.manualTravellersWingsGradualGlide.get();
		FCAPConfig.firstPersonGloveOverlay = config.firstPersonGloveOverlay.get();

		FCAPConfig.itemDisplayXOffs = config.ITEM_DISPLAY.screenOffsetX.get();
		FCAPConfig.itemDisplayYOffs = config.ITEM_DISPLAY.screenOffsetY.get();
		FCAPConfig.itemDisplayScale = config.ITEM_DISPLAY.screenScale.get();
		FCAPConfig.clock24HourFormat = config.ITEM_DISPLAY.twentyFourHourFormat.get();
	}

	private static void reloadGiantSkins(TFClientConfig config) {
		if (!config.giantSkinUUIDs.get().isEmpty()) {
			new Thread() {
				@Override
				public void run() {
					FCAPConfig.GAME_PROFILES.clear();
					YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY);
					MinecraftSessionService session = service.createMinecraftSessionService();
					for (String stringUUID : config.giantSkinUUIDs.get()) {
						try {
							ProfileResult result = session.fetchProfile(UUID.fromString(stringUUID), false);
							if (result != null) {
								FCAPConfig.GAME_PROFILES.add(result.profile());
							}
						} catch (IllegalArgumentException e) {
							TFCommon.LOGGER.error("\"{}\" is not a valid UUID!", stringUUID);
						}
					}
					super.run();
				}
			}.start();
		}
	}
}
