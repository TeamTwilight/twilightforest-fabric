package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

import java.util.UUID;

public record GogglesZoomClientBoundPacket(boolean isUsingZoom, UUID playerUUID) implements CustomPacketPayload {
	public static final Type<GogglesZoomClientBoundPacket> TYPE = new Type<>(TwilightForestMod.prefix("goggles_zoom_client_bound_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GogglesZoomClientBoundPacket> STREAM_CODEC = CustomPacketPayload.codec(twilightforest.network.GogglesZoomClientBoundPacket::write, twilightforest.network.GogglesZoomClientBoundPacket::new);

	public GogglesZoomClientBoundPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this(registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readUUID());
	}

	public static void handle(GogglesZoomClientBoundPacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Player player = ctx.player().level().getPlayerByUUID(packet.playerUUID);
			if (player != null)
				player.setData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, packet.isUsingZoom);
		});
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isUsingZoom);
		registryFriendlyByteBuf.writeUUID(playerUUID);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
