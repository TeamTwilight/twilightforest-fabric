package carminite.network.internal;

import carminite.Carminite;
import carminite.entity.IEntityWithComplexSpawn;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

public record AdvancedAddEntityPacket(int entityId, byte[] customPayload) implements CustomPacketPayload {
	public static final Type<AdvancedAddEntityPacket> TYPE = new Type<>(Carminite.prefix("advanced_add_entity"));
	public static final StreamCodec<FriendlyByteBuf, AdvancedAddEntityPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT,
		AdvancedAddEntityPacket::entityId,
		ByteBufCodecs.BYTE_ARRAY,
		AdvancedAddEntityPacket::customPayload,
		AdvancedAddEntityPacket::new
	);

	public AdvancedAddEntityPacket(Entity e) {
		this(e.getId(), writeCustomData(e));
	}

	@Override
	public Type<AdvancedAddEntityPacket> type() {
		return TYPE;
	}

	private static byte[] writeCustomData(final Entity entity) {
		if (!(entity instanceof IEntityWithComplexSpawn additionalSpawnData)) {
			return new byte[0];
		}
		final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), entity.registryAccess());
		try {
			additionalSpawnData.writeSpawnData(buf);
			return buf.array();
		} finally {
			buf.release();
		}
	}
}