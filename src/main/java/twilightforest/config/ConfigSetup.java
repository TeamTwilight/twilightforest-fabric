package twilightforest.config;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TFCommon;
import twilightforest.network.SyncUncraftingTableConfigPacket;

public final class ConfigSetup {

	private static final ModConfigSpec CLIENT_SPEC;
	private static final ModConfigSpec COMMON_SPEC;
	static final TFClientConfig CLIENT_CONFIG;
	static final TFCommonConfig COMMON_CONFIG;

	static {
		final Pair<TFCommonConfig, ModConfigSpec> specPairCommon = new ModConfigSpec.Builder().configure(TFCommonConfig::new);
		final Pair<TFClientConfig, ModConfigSpec> specPairClient = new ModConfigSpec.Builder().configure(TFClientConfig::new);

		COMMON_CONFIG = specPairCommon.getLeft();
		CLIENT_CONFIG = specPairClient.getLeft();
		COMMON_SPEC = specPairCommon.getRight();
		CLIENT_SPEC = specPairClient.getRight();

		// Register these listeners before registering the configs, or else defaults persist until the file is changed
		ModConfigEvents.loading(TFCommon.ID).register(ConfigSetup::loadConfigs);
		ModConfigEvents.reloading(TFCommon.ID).register(ConfigSetup::reloadConfigs);

		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.COMMON, COMMON_SPEC);
		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.CLIENT, CLIENT_SPEC);
	}

	public static void loadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	public static void reloadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	//sends uncrafting settings to a player on a server when they log in. This prevents desyncs when the configs dont match up between the player and the server.
	public static void syncUncraftingConfig() {
		ServerPlayerEvents.JOIN.register(player -> {
			TFCommon.LOGGER.info("[TwilightForest] Syncing Uncrafting Table config");
			ServerPlayNetworking.send(player, new SyncUncraftingTableConfigPacket(
				COMMON_CONFIG.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.repairingXpCostMultiplier.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.allowShapelessUncrafting.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableIngredientSwitching.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingOnly.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get())
			);
		});
	}
}