package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class FireflyParticle extends TextureSheetParticle {

	private final int halfLife;

	public FireflyParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
		super(level, x, y, z, vx, vy, vz);
		xd *= 2.1;
		yd *= 2.1;
		zd *= 2.1;
		rCol = 0.9F;
		gCol = 1.0F;
		bCol = 0.0F;
		quadSize = 0.2f + (random.nextFloat() * 0.1f);
		lifetime = 10 + random.nextInt(21);
		halfLife = lifetime / 2;
		hasPhysics = true;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
		alpha = getGlowBrightness();
		super.render(buffer, camera, partialTicks);
	}

	@Override
	public void tick() {
		if (age++ >= lifetime) {
			remove();
		}
	}

	public float getGlowBrightness() {
		int lifeTime = lifetime - age;
		if (lifeTime <= halfLife) {
			return (float) lifeTime / (float) halfLife;
		} else {
			return Math.max(1.0f - (((float) lifeTime - halfLife) / halfLife), 0);
		}
	}

	@Override
	public int getLightColor(float partialTicks) {
		return 0xF000F0;
	}

	@Environment(EnvType.CLIENT)
	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			FireflyParticle particle = new FireflyParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
