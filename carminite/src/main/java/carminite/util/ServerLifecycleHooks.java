package carminite.util;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.Nullable;

public final class ServerLifecycleHooks {
	private static @Nullable MinecraftServer currentServer;

	public static void init() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> currentServer = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(_ -> currentServer = null);
	}

	public static @Nullable MinecraftServer getCurrentServer() {
		return currentServer;
	}
}