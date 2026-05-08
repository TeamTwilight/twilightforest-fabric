package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.network.UpdateDeathTimePacket} — pushes
 * a custom {@code deathTime} value so clients can render the staggered boss-death
 * animation in sync with server-side timing.
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} receiver dropped —
 * client-side handler lives in {@code CodexNetworking}.</p>
 */
public record UpdateDeathTimePacket(int entityID, int deathTime) implements CustomPacketPayload {

	public static final Type<UpdateDeathTimePacket> TYPE = new Type<>(TwilightForestMod.prefix("death_time_update"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDeathTimePacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), UpdateDeathTimePacket::read);

	public static UpdateDeathTimePacket read(RegistryFriendlyByteBuf buf) {
		return new UpdateDeathTimePacket(buf.readInt(), buf.readInt());
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeInt(this.deathTime());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
