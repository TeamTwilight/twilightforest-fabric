package twilightforest.config;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TFCommon;

public final class ConfigSetup {

	private static final ModConfigSpec CLIENT_SPEC;
	private static final ModConfigSpec SERVER_SPEC;
	private static final ModConfigSpec COMMON_SPEC;

	static final TFClientConfig CLIENT_CONFIG;
	static final TFServerConfig SERVER_CONFIG;
	static final TFCommonConfig COMMON_CONFIG;

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
		ModConfigEvents.loading(TFCommon.ID).register(ConfigSetup::loadConfigs);
		ModConfigEvents.reloading(TFCommon.ID).register(ConfigSetup::reloadConfigs);

		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.COMMON, COMMON_SPEC);
		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.SERVER, SERVER_SPEC);
		ConfigRegistry.INSTANCE.register(TFCommon.ID, ModConfig.Type.CLIENT, CLIENT_SPEC);
	}

	public static void loadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == SERVER_SPEC) {
			TFConfig.rebakeServerOptions(SERVER_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	public static void reloadConfigs(ModConfig config) {
		if (config.getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (config.getSpec() == SERVER_SPEC) {
			TFConfig.rebakeServerOptions(SERVER_CONFIG);
		} else if (config.getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}
}