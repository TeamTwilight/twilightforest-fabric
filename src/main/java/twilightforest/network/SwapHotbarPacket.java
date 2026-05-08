package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public final class SwapHotbarPacket implements CustomPacketPayload {
	public static final SwapHotbarPacket INSTANCE = new SwapHotbarPacket();
	public static final Type<SwapHotbarPacket> TYPE = new Type<>(TwilightForestMod.prefix("swap_hotbar"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SwapHotbarPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private SwapHotbarPacket() {
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
