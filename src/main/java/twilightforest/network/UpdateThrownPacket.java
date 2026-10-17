package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TFCommon;

public record UpdateThrownPacket(int entityID, boolean thrown, int thrower, int throwCooldown) implements CustomPacketPayload {

	public static final Type<UpdateThrownPacket> TYPE = new Type<>(TFCommon.prefix("update_thrown_attachment"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateThrownPacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateThrownPacket::write, UpdateThrownPacket::new);

	public UpdateThrownPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean(), buf.readInt(), buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeBoolean(this.thrown());
		buf.writeInt(this.thrower());
		buf.writeInt(this.throwCooldown());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}