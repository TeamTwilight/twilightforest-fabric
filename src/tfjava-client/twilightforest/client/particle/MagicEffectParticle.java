package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * Fabric-safe Twilight magic-effect particle. Vanilla's SpellParticle constructor
 * is package-private in Mojang mappings, so this mirrors its motion/alpha logic
 * while accepting Twilight's ColorParticleOption payload 1:1.
 */
public class MagicEffectParticle extends TextureSheetParticle {
	private static final RandomSource RANDOM = RandomSource.create();

	private final SpriteSet sprites;
	private float originalAlpha = 1.0F;

	protected MagicEffectParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
		super(level, x, y, z, 0.5D - RANDOM.nextDouble(), yd, 0.5D - RANDOM.nextDouble());
		this.friction = 0.96F;
		this.gravity = -0.1F;
		this.speedUpWhenYMotionIsBlocked = true;
		this.sprites = sprites;
		this.yd *= 0.20000000298023224D;
		if (xd == 0.0D && zd == 0.0D) {
			this.xd *= 0.10000000149011612D;
			this.zd *= 0.10000000149011612D;
		}
		this.quadSize *= 0.75F;
		this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		this.hasPhysics = false;
		this.setSpriteFromAge(sprites);
		if (this.isCloseToScopingPlayer()) {
			this.setAlpha(0.0F);
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.sprites);
		if (this.isCloseToScopingPlayer()) {
			this.alpha = 0.0F;
		} else {
			this.alpha = Mth.lerp(0.05F, this.alpha, this.originalAlpha);
		}
	}

	@Override
	protected void setAlpha(float alpha) {
		super.setAlpha(alpha);
		this.originalAlpha = alpha;
	}

	private boolean isCloseToScopingPlayer() {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		return player != null
				&& player.getEyePosition().distanceToSqr(this.x, this.y, this.z) <= 9.0D
				&& minecraft.options.getCameraType().isFirstPerson()
				&& player.isScoping();
	}

	public static class Factory implements ParticleProvider<ColorParticleOption> {
		private final SpriteSet sprites;

		public Factory(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(ColorParticleOption option, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
			MagicEffectParticle particle = new MagicEffectParticle(level, x, y, z, xd, yd, zd, this.sprites);
			particle.setColor(option.getRed(), option.getGreen(), option.getBlue());
			particle.setAlpha(option.getAlpha());
			return particle;
		}
	}
}
