package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public record PerformSidestepPacket(boolean isLeftStepSide) implements CustomPacketPayload {
	public static final Type<PerformSidestepPacket> TYPE = new Type<>(TwilightForestMod.prefix("perform_sidestep_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PerformSidestepPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, PerformSidestepPacket::isLeftStepSide,
		PerformSidestepPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
