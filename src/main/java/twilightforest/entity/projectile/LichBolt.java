package twilightforest.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.Lich;
import twilightforest.util.TFDamageSources;

public class LichBolt extends TFThrowable {

	public LichBolt(EntityType<? extends LichBolt> type, Level world) {
		super(type, world);
	}

	public LichBolt(Level world, LivingEntity owner) {
		super(TFEntities.LICH_BOLT.get(), world, owner);
	}

	@Override
	public void tick() {
		super.tick();
		this.makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = getX() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			double dy = getY() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			double dz = getZ() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());

			double s1 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.17F;
			double s2 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.80F;
			double s3 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.69F;

			this.getLevel().addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
		}
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public float getPickRadius() {
		return 1.0F;
	}

	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		super.hurt(damagesource, amount);

		if (!this.getLevel().isClientSide() && damagesource.getEntity() != null) {
			Vec3 vec3d = damagesource.getEntity().getLookAngle();
			// reflect faster and more accurately
			this.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 1.5F, 0.1F);  // reflect faster and more accurately

			if (damagesource.getDirectEntity() instanceof LivingEntity)
				this.setOwner(damagesource.getDirectEntity());

			return true;
		}

		return false;
	}

	@Override
	protected float getGravity() {
		return 0.001F;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			ItemStack itemId = new ItemStack(Items.ENDER_PEARL);
			for (int i = 0; i < 8; ++i) {
				this.getLevel().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemId), this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		this.getLevel().broadcastEntityEvent(this, (byte) 3);
		this.discard();
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity hit = result.getEntity();
		if (hit instanceof LichBolt || hit instanceof LichBomb || (hit instanceof Lich lich && lich.isShadowClone())) {
			return;
		}

		if (!this.getLevel().isClientSide()) {
			if (hit instanceof LivingEntity) {
				hit.hurt(TFDamageSources.LICH_BOLT, 6);
			}
			this.getLevel().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}
}
