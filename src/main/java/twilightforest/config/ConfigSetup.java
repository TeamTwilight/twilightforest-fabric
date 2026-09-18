package twilightforest.config;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import io.github.fabricators_of_create.porting_lib.core.util.ServerLifecycleHooks;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TwilightForestMod;
import twilightforest.network.PacketDistributor;
import twilightforest.network.SyncUncraftingTableConfigPacket;

public final class ConfigSetup {

	private static final ModConfigSpec CLIENT_SPEC;
	private static final ModConfigSpec COMMON_SPEC;
	static final TFClientConfig CLIENT_CONFIG;
	static final TFCommonConfig COMMON_CONFIG;

	static {
		{
			final Pair<TFCommonConfig, ModConfigSpec> specPairCommon = new ModConfigSpec.Builder().configure(TFCommonConfig::new);
			final Pair<TFClientConfig, ModConfigSpec> specPairClient = new ModConfigSpec.Builder().configure(TFClientConfig::new);

			COMMON_CONFIG = specPairCommon.getLeft();
			CLIENT_CONFIG = specPairClient.getLeft();
			COMMON_SPEC = specPairCommon.getRight();
			CLIENT_SPEC = specPairClient.getRight();

			// Register these listeners before registering the configs, or else defaults persist until the file is changed
			NeoForgeModConfigEvents.loading(TwilightForestMod.ID).register(ConfigSetup::loadConfigs);
			NeoForgeModConfigEvents.reloading(TwilightForestMod.ID).register(ConfigSetup::reloadConfigs);

			NeoForgeConfigRegistry.INSTANCE.register(TwilightForestMod.ID, ModConfig.Type.COMMON, COMMON_SPEC);
			NeoForgeConfigRegistry.INSTANCE.register(TwilightForestMod.ID, ModConfig.Type.CLIENT, CLIENT_SPEC);
		}
	}

	private static void loadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	private static void reloadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	//sends uncrafting settings to a player on a server when they log in. This prevents desyncs when the configs dont match up between the player and the server.
	public static void syncUncraftingConfig() {
		PlayerEvents.PlayerLoggedInEvent.EVENT.register(event1 -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if (server != null && server.isDedicatedServer() && event1.getEntity() instanceof ServerPlayer player) {
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
					COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get()));
			}
		});
	}
}