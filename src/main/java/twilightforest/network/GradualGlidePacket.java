package twilightforest.network;

import carminite.network.PacketDistributor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import twilightforest.TFMain;
import twilightforest.init.TFDataAttachments;

import java.util.UUID;

public record GradualGlidePacket(boolean isGraduallyGliding, UUID playerUUID) implements CustomPacketPayload {
	public static final Type<GradualGlidePacket> TYPE = new Type<>(TFMain.prefix("gradual_glide_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GradualGlidePacket> STREAM_CODEC = CustomPacketPayload.codec(GradualGlidePacket::write, GradualGlidePacket::new);

	public GradualGlidePacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this(registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readUUID());
	}

	public static void handleServer(GradualGlidePacket packet, ServerPlayNetworking.Context ctx) {
		Player player = ctx.player().level().getPlayerByUUID(packet.playerUUID);
		if (player == null)
			return;
		player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding);
		PacketDistributor.sendToPlayersTrackingEntity(player, new GradualGlidePacket(packet.isGraduallyGliding, player.getUUID()));
	}

	public static void handleClient(GradualGlidePacket packet, ClientPlayNetworking.Context ctx) {
		Player player = ctx.client().level.getPlayerByUUID(packet.playerUUID);
		if (player == null)
			return;
		player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding);
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isGraduallyGliding);
		registryFriendlyByteBuf.writeUUID(playerUUID);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}