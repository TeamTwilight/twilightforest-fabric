package twilightforest.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LargeFlameParticle extends TextureSheetParticle {

	private final float flameScale;

	LargeFlameParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
		this.xd = this.xd * 0.009999999776482582D + vx;
		this.yd = this.yd * 0.009999999776482582D + vy;
		this.zd = this.zd * 0.009999999776482582D + vz;
		this.quadSize *= 5.0D;
		this.flameScale = this.quadSize;
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.hasPhysics = false;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public float getQuadSize(float partialTicks) {
		float relativeAge = (this.age + partialTicks) / this.lifetime;
		return this.flameScale * (1.0F - relativeAge * relativeAge * 0.5F);
	}

	@Override
	public int getLightColor(float partialTicks) {
		float var2 = (this.age + partialTicks) / this.lifetime;

		if (var2 < 0.0F) {
			var2 = 0.0F;
		}

		if (var2 > 1.0F) {
			var2 = 1.0F;
		}

		int var3 = super.getLightColor(partialTicks);
		int var4 = var3 & 255;
		int var5 = var3 >> 16 & 255;
		var4 += (int) (var2 * 15.0F * 16.0F);

		if (var4 > 240) {
			var4 = 240;
		}

		return var4 | var5 << 16;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.yd += 0.004D;

		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.9599999785423279D;
		this.yd *= 0.9599999785423279D;
		this.zd *= 0.9599999785423279D;

		if (this.onGround) {
			this.xd *= 0.699999988079071D;
			this.zd *= 0.699999988079071D;
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			LargeFlameParticle particle = new LargeFlameParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.spriteSet);
			return particle;
		}
	}
}
