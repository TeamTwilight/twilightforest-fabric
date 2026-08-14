package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFMain;
import twilightforest.item.LifedrainScepterItem;

public record LifedrainParticlePacket(int entityID, Vec3 victimPos) implements CustomPacketPayload {

	public static final Type<LifedrainParticlePacket> TYPE = new Type<>(TFMain.prefix("lifedrain_particles"));
	public static final StreamCodec<RegistryFriendlyByteBuf, LifedrainParticlePacket> STREAM_CODEC = CustomPacketPayload.codec(LifedrainParticlePacket::write, LifedrainParticlePacket::new);

	public LifedrainParticlePacket(RegistryFriendlyByteBuf buf) {
		this(buf.readInt(), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
		buf.writeDouble(this.victimPos.x());
		buf.writeDouble(this.victimPos.y());
		buf.writeDouble(this.victimPos.z());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(LifedrainParticlePacket packet, ClientPlayNetworking.Context ctx) {
		Entity entity = ctx.client().level.getEntity(packet.entityID());
		if (entity instanceof LivingEntity living) {
			LifedrainScepterItem.makeRedMagicTrail(living.level(), living, packet.victimPos());
		}
	}
}