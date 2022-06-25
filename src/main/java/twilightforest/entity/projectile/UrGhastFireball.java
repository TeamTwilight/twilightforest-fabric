package twilightforest.entity.projectile;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.UrGhast;

public class UrGhastFireball extends LargeFireball implements ITFProjectile {

	private final int power;

	public UrGhastFireball(Level world, UrGhast entityTFTowerBoss, double x, double y, double z, int power) {
		super(world, entityTFTowerBoss, x, y, z, power);
		this.power = power;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.getLevel().isClientSide() && !(result.getEntity() instanceof AbstractHurtingProjectile)) {
			// TF - up damage by 10
			result.getEntity().hurt(DamageSource.fireball(this, this.getOwner()), 16.0F);
			this.doEnchantDamageEffects((LivingEntity) this.getOwner(), result.getEntity());

			boolean flag = ForgeEventFactory.getMobGriefingEvent(this.getLevel(), this.getOwner());
			this.getLevel().explode(null, this.getX(), this.getY(), this.getZ(), this.power, flag, flag ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE);
			this.discard();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		//explode and leave fire when hitting a block, but dont destroy them
		boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
		this.getLevel().explode(null, this.getX(), this.getY(), this.getZ(), (float) this.power, flag, Explosion.BlockInteraction.NONE);
		this.discard();
	}

	@Override
	public void shoot(double x, double y, double z, float scale, float dist) {
		Vec3 vec3d = (new Vec3(x, y, z))
				.normalize()
				.add(this.random.nextGaussian() * 0.0075F * dist, this.random.nextGaussian() * 0.0075F * dist, this.random.nextGaussian() * 0.0075F * dist)
				.scale(scale);
		this.setDeltaMovement(vec3d);
		float f = Mth.sqrt((float) distanceToSqr(vec3d));
		this.setYRot((float) (Mth.atan2(vec3d.x(), z) * (180F / Mth.PI)));
		this.setXRot((float) (Mth.atan2(vec3d.y(), f) * (180F / Mth.PI)));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}
}
