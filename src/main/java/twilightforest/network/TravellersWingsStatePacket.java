package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.TravellersWingsAttachment;

public final class TravellersWingsStatePacket implements CustomPacketPayload {
	public static final Type<TravellersWingsStatePacket> TYPE = new Type<>(TwilightForestMod.prefix("travellers_wings_state"));
	public static final StreamCodec<RegistryFriendlyByteBuf, TravellersWingsStatePacket> STREAM_CODEC =
		CustomPacketPayload.codec(TravellersWingsStatePacket::write, TravellersWingsStatePacket::new);

	private final int entityId;
	private final TravellersWingsAttachment.WingState state;
	private final boolean sidestepLeft;
	private final int doubleJumpTimer;
	private final int sidestepTimer;

	public TravellersWingsStatePacket(int entityId, TravellersWingsAttachment.WingState state, boolean sidestepLeft, int doubleJumpTimer, int sidestepTimer) {
		this.entityId = entityId;
		this.state = state;
		this.sidestepLeft = sidestepLeft;
		this.doubleJumpTimer = doubleJumpTimer;
		this.sidestepTimer = sidestepTimer;
	}

	public TravellersWingsStatePacket(RegistryFriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readEnum(TravellersWingsAttachment.WingState.class), buf.readBoolean(), buf.readVarInt(), buf.readVarInt());
	}

	private void write(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(this.entityId);
		buf.writeEnum(this.state);
		buf.writeBoolean(this.sidestepLeft);
		buf.writeVarInt(this.doubleJumpTimer);
		buf.writeVarInt(this.sidestepTimer);
	}

	public int entityId() {
		return this.entityId;
	}

	public TravellersWingsAttachment.WingState state() {
		return this.state;
	}

	public boolean sidestepLeft() {
		return this.sidestepLeft;
	}

	public int doubleJumpTimer() {
		return this.doubleJumpTimer;
	}

	public int sidestepTimer() {
		return this.sidestepTimer;
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
