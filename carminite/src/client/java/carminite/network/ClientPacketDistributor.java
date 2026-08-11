package carminite.network;

import java.util.Objects;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class ClientPacketDistributor {
	private ClientPacketDistributor() {}

	public static void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
		Objects.requireNonNull(payload, "Cannot send null payload");
		ClientPlayNetworking.send(payload);
		for (CustomPacketPayload otherPayload : payloads) {
			Objects.requireNonNull(otherPayload, "Cannot send null payload");
			ClientPlayNetworking.send(otherPayload);
		}
	}
}