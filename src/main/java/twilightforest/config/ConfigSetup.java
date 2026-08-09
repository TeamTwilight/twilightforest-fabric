package twilightforest.config;

import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ModConfig;
import io.github.fabricators_of_create.porting_lib.config.ModConfigEvent;
import io.github.fabricators_of_create.porting_lib.config.ModConfigSpec;
import io.github.fabricators_of_create.porting_lib.core.util.ServerLifecycleHooks;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
			final Pair<TFCommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFCommonConfig::new);
			ConfigRegistry.registerConfig(TwilightForestMod.ID, ModConfig.Type.COMMON, COMMON_SPEC = specPair.getRight());
			COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFClientConfig::new);
			ConfigRegistry.registerConfig(TwilightForestMod.ID, ModConfig.Type.CLIENT, CLIENT_SPEC = specPair.getRight());
			CLIENT_CONFIG = specPair.getLeft();
		}
	}

	public static void loadConfigs() {
		ModConfigEvent.Loading.EVENT.register(event1 -> {
			if (event1.getConfig().getSpec() == CLIENT_SPEC) {
				TFConfig.rebakeClientOptions(CLIENT_CONFIG);
			} else if (event1.getConfig().getSpec() == COMMON_SPEC) {
				TFConfig.rebakeCommonOptions(COMMON_CONFIG);
			}
		});
	}

	public static void reloadConfigs() {
		ModConfigEvent.Reloading.EVENT.register(event1 -> {
			if (event1.getConfig().getSpec() == CLIENT_SPEC) {
				TFConfig.rebakeClientOptions(CLIENT_CONFIG);
			} else if (event1.getConfig().getSpec() == COMMON_SPEC) {
				TFConfig.rebakeCommonOptions(COMMON_CONFIG);
			}
		});
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