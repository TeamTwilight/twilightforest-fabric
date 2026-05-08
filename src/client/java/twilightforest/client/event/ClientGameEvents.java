package twilightforest.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class ClientGameEvents {
	public static int time = 0;

	private ClientGameEvents() {
	}

	public static void bootstrap() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!client.isPaused()) {
				time++;
			}
		});
	}
}
