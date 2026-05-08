package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import twilightforest.TwilightForestMod;

public record WipeOreMeterPacket(InteractionHand hand) implements CustomPacketPayload {
	public static final Type<WipeOreMeterPacket> TYPE = new Type<>(TwilightForestMod.prefix("wipe_ore_meter"));
	public static final StreamCodec<RegistryFriendlyByteBuf, WipeOreMeterPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, packet -> packet.hand() == InteractionHand.MAIN_HAND,
		mainHand -> new WipeOreMeterPacket(mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND)
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
