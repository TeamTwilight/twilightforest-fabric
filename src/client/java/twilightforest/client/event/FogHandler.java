package twilightforest.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;

public final class FogHandler {
	private static boolean skyChunkLoaded = false;
	private static boolean terrainChunkLoaded = false;

	private FogHandler() {
	}

	public static void bootstrap() {
		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, level) -> unloadFog());
	}

	public static void unloadFog() {
		skyChunkLoaded = false;
		terrainChunkLoaded = false;
	}

	public static boolean skyChunkLoaded() {
		return skyChunkLoaded;
	}

	public static boolean terrainChunkLoaded() {
		return terrainChunkLoaded;
	}
}
