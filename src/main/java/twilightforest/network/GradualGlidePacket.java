package twilightforest.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

import java.util.UUID;

public record GradualGlidePacket(boolean isGraduallyGliding, UUID playerUUID) implements CustomPacketPayload {
	public static final Type<GradualGlidePacket> TYPE = new Type<>(TwilightForestMod.prefix("gradual_glide_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GradualGlidePacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, GradualGlidePacket::isGraduallyGliding,
		UUIDUtil.STREAM_CODEC, GradualGlidePacket::playerUUID,
		GradualGlidePacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
