package twilightforest.client.event;

import net.fabricmc.loader.api.FabricLoader;

public final class ClientRegistrationEvents {
	private static boolean optifinePresent = false;

	private ClientRegistrationEvents() {
	}

	public static void bootstrap() {
		optifinePresent = FabricLoader.getInstance().isModLoaded("optifine") || FabricLoader.getInstance().isModLoaded("optifabric");
	}

	public static boolean isOptifinePresent() {
		return optifinePresent;
	}
}
