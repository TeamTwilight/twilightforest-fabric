package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.network.EnforceProgressionStatusPacket}
 * — server tells client whether progression-enforcement is on (so the client can
 * adjust UI hints / boss-bar locks). Codex Fabric drops the NF
 * {@code IPayloadContext}-based handler; the client receiver lives in
 * {@code CodexNetworking} (or the user's TFGameRules wiring once that's ported).
 */
public record EnforceProgressionStatusPacket(boolean enforce) implements CustomPacketPayload {

	public static final Type<EnforceProgressionStatusPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_progression_status"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EnforceProgressionStatusPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), EnforceProgressionStatusPacket::new);

	public EnforceProgressionStatusPacket(FriendlyByteBuf buf) {
		this(buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(this.enforce);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
