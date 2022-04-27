package twilightforest.client.particle;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GhastTrapParticle extends TextureSheetParticle {

	private final float reddustParticleScale;

	private final double originX;
	private final double originY;
	private final double originZ;

	GhastTrapParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz) {
		this(world, x, y, z, 3.0F, vx, vy, vz);
	}

	GhastTrapParticle(ClientLevel world, double x, double y, double z, float scale, double mx, double my, double mz) {
		super(world, x + mx, y + my, z + mz, mx, my, mz);
		this.xd = mx;
		this.yd = my;
		this.zd = mz;

		this.originX = x;
		this.originY = y;
		this.originZ = z;
		float brightness = (float) Math.random() * 0.4F;// + 0.6F;
		this.rCol = 1.0F;
		this.gCol = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * brightness;
		this.bCol  = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * brightness;
		this.quadSize *= 0.75F * scale;
		this.reddustParticleScale = this.quadSize;
		this.lifetime = (int) (10.0D / (Math.random() * 0.8D + 0.2D));
		this.hasPhysics = true;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public float getQuadSize(float partialTicks) {
		float f6 = (this.age + partialTicks) / this.lifetime * 32.0F;
		f6 = Mth.clamp(f6, 0f, 1f);

		return this.reddustParticleScale * f6;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		float proportion = (float) this.age / (float) this.lifetime;
		proportion = 1.0F - proportion;
		this.x = this.originX + this.xd * proportion;
		this.y = this.originY + this.yd * proportion;// - (double)(antiProportion * 1.2F);
		this.z = this.originZ + this.zd * proportion;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(FabricSpriteProvider sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			GhastTrapParticle particle = new GhastTrapParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.setSpriteFromAge(this.spriteSet);
			return particle;
		}
	}
}
