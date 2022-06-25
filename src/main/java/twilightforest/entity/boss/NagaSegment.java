package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;

import java.util.List;

public class NagaSegment extends TFPart<Naga> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("naga_segment");

	private int deathCounter;

	public NagaSegment(Naga naga) {
		super(naga);
		this.setPos(naga.getX(), naga.getY(), naga.getZ());
	}

	@Override
	protected void defineSynchedData() {
		this.deactivate();
	}

	@Environment(EnvType.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		return !this.isInvisible() && this.getParent().hurt(src, damage * 2.0F / 3.0F);
	}

	@Override
	public boolean is(Entity entity) {
		return entity == this || entity == this.getParent();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public void tick() {
		super.tick();

		++this.tickCount;

		if (!this.isInvisible())
			this.collideWithOthers();

		if (this.deathCounter > 0) {
			this.deathCounter--;
			if (this.deathCounter <= 0) {
				for (int k = 0; k < 20; k++) {
					double d = this.random.nextGaussian() * 0.02D;
					double d1 = this.random.nextGaussian() * 0.02D;
					double d2 = this.random.nextGaussian() * 0.02D;
					SimpleParticleType explosionType = random.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION;

					this.getLevel().addParticle(explosionType,
							(this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
							this.getY() + this.random.nextFloat() * this.getBbHeight(),
							(this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
							d, d1, d2);
				}

				deactivate();
			}
		}
	}

	private void collideWithOthers() {
		List<Entity> list = this.getLevel().getEntities(this, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D));

		for (Entity entity : list) {
			if (entity.isPushable()) {
				this.collideWithEntity(entity);
			}
		}
	}

	private void collideWithEntity(Entity entity) {
		entity.push(this);

		// attack anything that's not us
		if (entity instanceof LivingEntity && !(entity instanceof Naga)) {
			int attackStrength = 2;

			// get rid of nearby deer & look impressive
			if (entity instanceof Animal) {
				attackStrength *= 3;
			}

			entity.hurt(DamageSource.mobAttack(this.getParent()), attackStrength);
		}
	}

	public void deactivate() {
		this.setSize(EntityDimensions.scalable(0.0F, 0.0F));
		this.setInvisible(true);
	}

	public void activate() {
		this.setSize(EntityDimensions.scalable(1.8F, 1.8F));
		this.setInvisible(false);
	}

	// make public
	@Override
	public void setRot(float yaw, float pitch) {
		super.setRot(yaw, pitch);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
	}

	public void selfDestruct() {
		this.deathCounter = 10;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public float getStepHeight() {
		return 2.0F;
	}
}
