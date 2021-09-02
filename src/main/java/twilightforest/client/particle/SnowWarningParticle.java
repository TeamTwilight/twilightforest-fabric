package twilightforest.client.particle;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class SnowWarningParticle extends SnowParticle {

	SnowWarningParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, vx, vy, vz, scale);
		this.lifetime = 50;
	}

	@Override
	public void tick() {
		super.tick();
		this.yd -= 0.019999999552965164D;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SnowWarningParticle particle = new SnowWarningParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 1);
			particle.pickSprite(this.spriteSet);
			return particle;
		}
	}
}
