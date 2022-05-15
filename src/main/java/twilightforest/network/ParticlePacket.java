package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ParticlePacket {
    private final List<QueuedParticle> queuedParticles = new ArrayList<>();

    public ParticlePacket() { }

    @SuppressWarnings("deprecation")
    public ParticlePacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            ParticleOptions particleOptions = this.readParticle(Objects.requireNonNull(Registry.PARTICLE_TYPE.byId(buf.readInt())), buf);
            this.queuedParticles.add(new QueuedParticle(particleOptions, buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()));
        }
    }

    private <T extends ParticleOptions> T readParticle(ParticleType<T> particleType, FriendlyByteBuf buf) {
        return particleType.getDeserializer().fromNetwork(particleType, buf);
    }

    @SuppressWarnings("deprecation")
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.queuedParticles.size());
        for (QueuedParticle queuedParticle : this.queuedParticles) {
            int d = Registry.PARTICLE_TYPE.getId(queuedParticle.particleOptions.getType());
            buf.writeInt(d);
            buf.writeBoolean(queuedParticle.b);
            buf.writeDouble(queuedParticle.x);
            buf.writeDouble(queuedParticle.y);
            buf.writeDouble(queuedParticle.z);
            buf.writeDouble(queuedParticle.x2);
            buf.writeDouble(queuedParticle.y2);
            buf.writeDouble(queuedParticle.z2);
            queuedParticle.particleOptions.writeToNetwork(buf);
        }
    }

    public void queueParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) {
        this.queuedParticles.add(new QueuedParticle(particleOptions, b, x, y, z, x2, y2, z2));
    }

    public void queueParticle(ParticleOptions particleOptions, boolean b, Vec3 vec3, double x2, double y2, double z2) {
        this.queuedParticles.add(new QueuedParticle(particleOptions, b, vec3.x, vec3.y, vec3.z, x2, y2, z2));
    }

    private record QueuedParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) { }

    public static class Handler {
        public static boolean onMessage(ParticlePacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;
                for (QueuedParticle queuedParticle : message.queuedParticles) {
                    level.addParticle(queuedParticle.particleOptions, queuedParticle.b, queuedParticle.x, queuedParticle.y, queuedParticle.z, queuedParticle.x2, queuedParticle.y2, queuedParticle.z2);
                }
            });
            return true;
        }
    }
}
