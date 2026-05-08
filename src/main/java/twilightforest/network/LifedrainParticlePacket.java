package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.network.LifedrainParticlePacket} —
 * server pushes coordinates of a Lich/Lifedrain-Scepter victim so clients can
 * render the red trailing magic-particle line.
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} dropped — client trail
 * spawning is wired in {@code CodexNetworking}.</p>
 */
public record LifedrainParticlePacket(int entityID, Vec3 victimPos) implements CustomPacketPayload {

	public static final Type<LifedrainParticlePacket> TYPE = new Type<>(TwilightForestMod.prefix("lifedrain_particles"));
	public static final StreamCodec<RegistryFriendlyByteBuf, LifedrainParticlePacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), LifedrainParticlePacket::new);

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
}
