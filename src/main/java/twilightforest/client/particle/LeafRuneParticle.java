package twilightforest.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class LeafRuneParticle extends TextureSheetParticle {

	LeafRuneParticle(ClientLevel world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		// super applies jittering, reset it
		this.xd = velX;
		this.yd = velY;
		this.zd = velZ;

		this.quadSize = this.random.nextFloat() * 0.25F;
		this.lifetime = (int)(Math.random() * 10.0D) + 40;
		this.gravity = 0.3F + random.nextFloat() * 0.6F;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			LeafRuneParticle particle = new LeafRuneParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.spriteSet);
			return particle;
		}
	}
}
