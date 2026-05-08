package twilightforest.network;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

/**
 * 1:1 port of upstream {@code twilightforest.network.ParticlePacket} — batched
 * particle dispatch: queue many particle spawns and ship them in a single payload.
 *
 * <p>Codex Fabric port note: most callers in this codebase have already been
 * translated to {@code ServerLevel.sendParticles} per particle (established
 * convention from P2.f Trophy / P3.i CastleDoor / P4.d IBossLootBuffer). This
 * payload class exists so the few remaining server-broadcast callers compile and
 * for completeness with upstream's API. Handler is wired in {@code CodexNetworking}
 * client side.</p>
 */
public class ParticlePacket implements CustomPacketPayload {

	public static final Type<ParticlePacket> TYPE = new Type<>(TwilightForestMod.prefix("particle_queue"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ParticlePacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), ParticlePacket::new);

	private final List<QueuedParticle> queuedParticles = new ArrayList<>();

	public ParticlePacket() {
	}

	public ParticlePacket(RegistryFriendlyByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.byId(buf.readInt());
			if (type == null)
				break; // Fail silently and end execution entirely. Due to Type serialization we now have completely unknown data in the pipeline without any way to safely read it all.
			this.queuedParticles.add(new QueuedParticle(ParticleTypes.STREAM_CODEC.decode(buf), buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.queuedParticles.size());
		for (QueuedParticle queuedParticle : this.queuedParticles) {
			int d = BuiltInRegistries.PARTICLE_TYPE.getId(queuedParticle.particleOptions.getType());
			buf.writeInt(d);
			ParticleTypes.STREAM_CODEC.encode(buf, queuedParticle.particleOptions);
			buf.writeBoolean(queuedParticle.b);
			buf.writeDouble(queuedParticle.x);
			buf.writeDouble(queuedParticle.y);
			buf.writeDouble(queuedParticle.z);
			buf.writeDouble(queuedParticle.x2);
			buf.writeDouble(queuedParticle.y2);
			buf.writeDouble(queuedParticle.z2);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public List<QueuedParticle> queuedParticles() {
		return this.queuedParticles;
	}

	public void queueParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) {
		this.queuedParticles.add(new QueuedParticle(particleOptions, b, x, y, z, x2, y2, z2));
	}

	public void queueParticle(ParticleOptions particleOptions, boolean b, Vec3 xyz, Vec3 xyz2) {
		this.queuedParticles.add(new QueuedParticle(particleOptions, b, xyz.x, xyz.y, xyz.z, xyz2.x, xyz2.y, xyz2.z));
	}

	public record QueuedParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2,
								  double y2, double z2) {
	}
}
