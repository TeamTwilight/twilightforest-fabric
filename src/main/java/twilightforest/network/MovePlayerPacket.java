package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public record MovePlayerPacket(double motionX, double motionY, double motionZ) implements CustomPacketPayload {
	public static final Type<MovePlayerPacket> TYPE = new Type<>(TwilightForestMod.prefix("move_player"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MovePlayerPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.DOUBLE, MovePlayerPacket::motionX,
		ByteBufCodecs.DOUBLE, MovePlayerPacket::motionY,
		ByteBufCodecs.DOUBLE, MovePlayerPacket::motionZ,
		MovePlayerPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
