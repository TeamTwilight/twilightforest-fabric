package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TFCommon;

public record UpdateDeathTimePacket(int entityID, int deathTime) implements CustomPacketPayload {

	public static final Type<UpdateDeathTimePacket> TYPE = new Type<>(TFCommon.prefix("death_time_update"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDeathTimePacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateDeathTimePacket::write, UpdateDeathTimePacket::read);

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