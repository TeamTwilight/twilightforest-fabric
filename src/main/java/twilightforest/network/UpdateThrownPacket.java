package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public record UpdateThrownPacket(int entityID, boolean thrown, int throwerID, int throwCooldown) implements CustomPacketPayload {
	public static final Type<UpdateThrownPacket> TYPE = new Type<>(TwilightForestMod.prefix("update_thrown_attachment"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateThrownPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, UpdateThrownPacket::entityID,
		ByteBufCodecs.BOOL, UpdateThrownPacket::thrown,
		ByteBufCodecs.VAR_INT, UpdateThrownPacket::throwerID,
		ByteBufCodecs.VAR_INT, UpdateThrownPacket::throwCooldown,
		UpdateThrownPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
