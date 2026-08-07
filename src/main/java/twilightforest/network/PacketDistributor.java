package twilightforest.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Fabric version of PacketDistributor
 */
public class PacketDistributor {

	public static <T extends CustomPacketPayload> void sendToAllPlayers(MinecraftServer server, T packet) {
		for (ServerPlayer player : PlayerLookup.all(server)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T packet) {
		ServerPlayNetworking.send(player, packet);
	}

	public static <T extends CustomPacketPayload> void sendToServer(T packet) {
		ClientPlayNetworking.send(packet);
	}

	public static <T extends CustomPacketPayload> void sendToPlayersTrackingEntity(Entity entity, T packet) {
		for (ServerPlayer player : PlayerLookup.tracking(entity)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static <T extends CustomPacketPayload> void sendToPlayersTrackingEntityAndSelf(Entity entity, T packet) {
		for (ServerPlayer player : PlayerLookup.tracking(entity)) {
			ServerPlayNetworking.send(player, packet);
		}
		if (entity instanceof ServerPlayer sp) {
			ServerPlayNetworking.send(sp, packet);
		}
	}

	public static <T extends CustomPacketPayload> void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, T packet) {
		for (ServerPlayer player : PlayerLookup.tracking(level, chunkPos)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static <T extends CustomPacketPayload> void sendToPlayersNear(ServerLevel level, BlockPos pos, double distance, T packet) {
		Vec3 vec3 = Vec3.atCenterOf(pos);
		for (ServerPlayer player : PlayerLookup.around(level, vec3, distance)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static <T extends CustomPacketPayload> void sendToPlayersNear(ServerLevel level, Object dummy, double x, double y, double z, double distance, T packet) {
		Vec3 vec3 = new Vec3(x, y, z);
		for (ServerPlayer player : PlayerLookup.around(level, vec3, distance)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static <T extends CustomPacketPayload> void sendToPlayersNear(Level level, Object dummy, double x, double y, double z, double distance, T packet) {
		if (level instanceof ServerLevel serverLevel) {
			sendToPlayersNear(serverLevel, null, x, y, z, distance, packet);
		}
	}
}