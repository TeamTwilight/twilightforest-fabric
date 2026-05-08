package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public record CycleMapSlotPacket() implements CustomPacketPayload {
	public static final CycleMapSlotPacket INSTANCE = new CycleMapSlotPacket();
	public static final Type<CycleMapSlotPacket> TYPE = new Type<>(TwilightForestMod.prefix("cycle_map_slot_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CycleMapSlotPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
