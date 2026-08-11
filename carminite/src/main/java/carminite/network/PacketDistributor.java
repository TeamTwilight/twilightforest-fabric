package carminite.network;

import java.util.Objects;

import carminite.util.ServerLifecycleHooks;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.jspecify.annotations.Nullable;

public final class PacketDistributor {
	private PacketDistributor() {}

	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		send(player, payload, payloads);
	}

	public static void sendToPlayersInDimension(ServerLevel level, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		for (ServerPlayer player : PlayerLookup.level(level)) {
			send(player, payload, payloads);
		}
	}

	public static void sendToPlayersNear(ServerLevel level, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		for (ServerPlayer player : PlayerLookup.around(level, new net.minecraft.core.Vec3i( (int) x, (int) y, (int) z ), radius)) {
			if (player != excluded) {
				send(player, payload, payloads);
			}
		}
	}

	public static void sendToAllPlayers(CustomPacketPayload payload, CustomPacketPayload... payloads) {
		MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
		for (ServerPlayer player : PlayerLookup.all(server)) {
			send(player, payload, payloads);
		}
	}

	public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		if (entity.level().isClientSide()) {
			throw new IllegalStateException("Cannot send clientbound payloads on the client");
		}
		for (ServerPlayer player : PlayerLookup.tracking(entity)) {
			send(player, payload, payloads);
		}
	}

	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		sendToPlayersTrackingEntity(entity, payload, payloads);
		if (entity instanceof ServerPlayer player) {
			send(player, payload, payloads);
		}
	}

	public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
		for (ServerPlayer player : PlayerLookup.tracking(level, chunkPos)) {
			send(player, payload, payloads);
		}
	}

	private static void send(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads ) {
		Objects.requireNonNull(payload, "Cannot send null payload");
		ServerPlayNetworking.send(player, payload);
		for (CustomPacketPayload otherPayload : payloads) {
			Objects.requireNonNull(otherPayload, "Cannot send null payload");
			ServerPlayNetworking.send(player, otherPayload);
		}
	}
}