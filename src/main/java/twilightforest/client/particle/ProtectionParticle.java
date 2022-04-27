package twilightforest.client.particle;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class ProtectionParticle extends SuspendedTownParticle {

	ProtectionParticle(ClientLevel world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
	}

	@Override
	public int getLightColor(float partialTicks) {
		return 0xF000F0;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(FabricSpriteProvider spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle createParticle(SimpleParticleType data, ClientLevel world, double x, double y, double z, double vx, double vy, double vz) {
			ProtectionParticle particle = new ProtectionParticle(world, x, y, z, vx, vy, vz);
			particle.pickSprite(this.spriteSet);
			particle.setColor(1.0F, 1.0F, 1.0F);
			return particle;
		}
	}

}
