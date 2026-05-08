package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public record PerformDoubleJumpPacket() implements CustomPacketPayload {
	public static final PerformDoubleJumpPacket INSTANCE = new PerformDoubleJumpPacket();
	public static final Type<PerformDoubleJumpPacket> TYPE = new Type<>(TwilightForestMod.prefix("perform_double_jump_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PerformDoubleJumpPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
