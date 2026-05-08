package twilightforest.config;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.network.SyncUncraftingTableConfigPacket;

public final class ConfigSetup {

	public static final TFClientConfig CLIENT_CONFIG = new TFClientConfig();
	public static final TFCommonConfig COMMON_CONFIG = new TFCommonConfig();

	private ConfigSetup() {
	}

	public static void loadConfigs() {
		TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		TFConfig.rebakeClientOptions(CLIENT_CONFIG);
	}

	public static void reloadConfigs() {
		loadConfigs();
	}

	public static void syncUncraftingConfig(ServerPlayer player) {
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
			COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get()));
	}
}
