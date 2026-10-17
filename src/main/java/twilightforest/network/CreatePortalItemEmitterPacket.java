package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TFCommon;

public record CreatePortalItemEmitterPacket(int entityID, int lifetime) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<CreatePortalItemEmitterPacket> TYPE = new CustomPacketPayload.Type<>(TFCommon.prefix("create_portal_item_emitter_packet"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CreatePortalItemEmitterPacket> STREAM_CODEC = CustomPacketPayload.codec(CreatePortalItemEmitterPacket::write, CreatePortalItemEmitterPacket::new);

	public CreatePortalItemEmitterPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityID());
		buf.writeInt(lifetime());
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}