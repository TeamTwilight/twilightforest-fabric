package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFParticleType;

public abstract class BaseIceMob extends Monster {
	public BaseIceMob(EntityType<? extends BaseIceMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void aiStep() {
		if (!this.onGround && this.getDeltaMovement().y() < 0.0D) {
			Vec3 motion = getDeltaMovement();
			this.setDeltaMovement(motion.x(), motion.y() * 0.6D, motion.z());
		}
		super.aiStep();
		if (this.getLevel().isClientSide()) {
			// make snow particles
			for (int i = 0; i < 3; i++) {
				float px = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;
				float pz = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;

				this.getLevel().addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
				if (this.getLevel().getBiome(this.blockPosition()).value().shouldSnowGolemBurn(this.blockPosition()) || this.isOnFire()) {
					this.getLevel().addParticle(ParticleTypes.CLOUD, this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0.1F, 0);
					this.getLevel().addParticle(ParticleTypes.DRIPPING_WATER, this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
				}
			}
		}
		if (this.getLevel().getBiome(this.blockPosition()).value().shouldSnowGolemBurn(this.blockPosition()) && this.tickCount % 20 == 0) {
			//BURN!!!
			this.hurt(DamageSource.ON_FIRE, 1.0F);
		}
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
	}
}
