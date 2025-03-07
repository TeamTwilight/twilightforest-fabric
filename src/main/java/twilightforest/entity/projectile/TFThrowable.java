package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;

public abstract class TFThrowable extends ThrowableProjectile implements ITFProjectile {

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn) {
		super(type, worldIn);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn, double x, double y, double z) {
		super(type, x, y, z, worldIn);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn, LivingEntity throwerIn) {
		super(type, throwerIn, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	public void makeTrail(ParticleOptions particle, int amount) {
		this.makeTrail(particle, 0.0D, 0.0D, 0.0D, amount);
	}

	public void makeTrail(ParticleOptions particle, double r, double g, double b, int amount) {
		for (int i = 0; i < amount; i++) {
			double dx = this.getX() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			double dy = this.getY() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			double dz = this.getZ() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			this.level().addParticle(particle, dx, dy, dz, r, g, b);
		}
	}

	protected void deflectedAndEffects(LivingEntity deflector) {
		this.deflectedByEntity(deflector);
		deflector.playSound(TFSounds.SHIELD_BLOCK.get(), 0.5F, deflector.getVoicePitch() * 1.5F);
		deflector.swing(InteractionHand.MAIN_HAND);
	}

	private void deflectedByEntity(Entity deflector) {
		this.setDeltaMovement(this.getDeltaMovement().add(0.5D - this.random.nextDouble(), 0.75D, 0.5D - this.random.nextDouble()).multiply(0.75D, 1.5D, 0.75D));
		this.setOwner(deflector);
	}
}
