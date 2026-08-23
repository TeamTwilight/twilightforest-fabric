package twilightforest.config;

import carminite.network.PacketDistributor;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TFMain;
import twilightforest.network.SyncUncraftingTableConfigPacket;

public final class ConfigSetup {

	private static final ModConfigSpec CLIENT_SPEC;
	private static final ModConfigSpec COMMON_SPEC;
	static final TFClientConfig CLIENT_CONFIG;
	static final TFCommonConfig COMMON_CONFIG;

	static {
		{
			final Pair<TFCommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFCommonConfig::new);
			ConfigRegistry.INSTANCE.register(TFMain.ID, ModConfig.Type.COMMON, COMMON_SPEC = specPair.getRight());
			COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFClientConfig::new);
			ConfigRegistry.INSTANCE.register(TFMain.ID, ModConfig.Type.CLIENT, CLIENT_SPEC = specPair.getRight());
			CLIENT_CONFIG = specPair.getLeft();
		}
	}

	public static void loadConfigs() {
		ModConfigEvents.loading(TFMain.ID).register(modConfig -> {
			if (modConfig.getSpec() == CLIENT_SPEC) {
				TFConfig.rebakeClientOptions(CLIENT_CONFIG);
			} else if (modConfig.getSpec() == COMMON_SPEC) {
				TFConfig.rebakeCommonOptions(COMMON_CONFIG);
			}
		});
	}

	public static void reloadConfigs() {
		ModConfigEvents.reloading(TFMain.ID).register(modConfig -> {
			if (modConfig.getSpec() == CLIENT_SPEC) {
				TFConfig.rebakeClientOptions(CLIENT_CONFIG);
			} else if (modConfig.getSpec() == COMMON_SPEC) {
				TFConfig.rebakeCommonOptions(COMMON_CONFIG);
			}
		});
	}

	//sends uncrafting settings to a player on a server when they log in. This prevents desyncs when the configs dont match up between the player and the server.
	public static void syncUncraftingConfig() {
		ServerPlayerEvents.JOIN.register(player -> {
			TFMain.LOGGER.info("[TwilightForest] Syncing Uncrafting Table config");
			PacketDistributor.sendToPlayer(player, new SyncUncraftingTableConfigPacket(
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