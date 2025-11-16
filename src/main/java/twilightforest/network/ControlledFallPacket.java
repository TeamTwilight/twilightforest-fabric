package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataAttachments;

import java.util.UUID;

public record ControlledFallPacket(boolean isControlledFalling, UUID playerUUID) implements CustomPacketPayload {
	public static final Type<ControlledFallPacket> TYPE = new Type<>(TwilightForestMod.prefix("controlled_fall_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ControlledFallPacket> STREAM_CODEC = CustomPacketPayload.codec(ControlledFallPacket::write, ControlledFallPacket::new);

	public ControlledFallPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		this(registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readUUID());
	}

	public static void handle(ControlledFallPacket packet, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Player player = ctx.player().level().getPlayerByUUID(packet.playerUUID);
			if (player == null)
				return;
			if (player.level().isClientSide()) {
				player.setData(TFDataAttachments.IS_CONTROLLED_FALLING, packet.isControlledFalling);
				return;
			}
			player.setData(TFDataAttachments.IS_CONTROLLED_FALLING, packet.isControlledFalling);
			PacketDistributor.sendToPlayersTrackingEntity(player, new ControlledFallPacket(packet.isControlledFalling, player.getUUID()));
		});
	}

	private void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isControlledFalling);
		registryFriendlyByteBuf.writeUUID(playerUUID);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
