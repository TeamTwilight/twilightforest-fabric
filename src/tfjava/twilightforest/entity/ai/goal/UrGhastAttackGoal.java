package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.UrGhast;
import twilightforest.entity.projectile.UrGhastFireball;
import twilightforest.init.TFSounds;

public class UrGhastAttackGoal extends Goal {
	private final UrGhast ghast;
	public int attackTimer;
	public int prevAttackTimer;

	public UrGhastAttackGoal(UrGhast ghast) {
		this.ghast = ghast;
	}

	@Override
	public boolean canUse() {
		return this.ghast.getTarget() != null && !this.ghast.isInTantrum();
	}

	@Override
	public void start() {
		this.attackTimer = 0;
		this.prevAttackTimer = 0;
	}

	@Override
	public void stop() {
		this.ghast.setCharging(false);
	}

	@Override
	public void tick() {
		LivingEntity target = this.ghast.getTarget();
		if (target == null) {
			return;
		}

		if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.getSensing().hasLineOfSight(target)) {
			this.prevAttackTimer = this.attackTimer;
			++this.attackTimer;

			this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());

			if (this.attackTimer == 10) {
				this.ghast.playSound(TFSounds.UR_GHAST_WARN, 10.0F, this.ghast.getVoicePitch());
			}

			if (this.attackTimer == 20) {
				if (!this.ghast.isInTantrum()) {
					this.ghast.playSound(TFSounds.UR_GHAST_SHOOT, 10.0F, this.ghast.getVoicePitch());
					this.spitFireball();
					this.prevAttackTimer = this.attackTimer;
				}
				this.attackTimer = -40;
			}
		} else if (this.attackTimer > 0) {
			this.prevAttackTimer = this.attackTimer;
			--this.attackTimer;
		}

		this.ghast.setCharging(this.attackTimer > 10);
	}

	public void spitFireball() {
		LivingEntity target = this.ghast.getTarget();
		if (target == null) {
			return;
		}

		double offsetX = target.getX() - this.ghast.getX();
		double offsetY = target.getBoundingBox().minY + target.getBbHeight() / 2.0F - (this.ghast.getY() + this.ghast.getBbHeight() / 2.0F);
		double offsetZ = target.getZ() - this.ghast.getZ();
		double shotSpawnDistance = 8.5D;
		Vec3 lookVec = this.ghast.getViewVector(1.0F);

		UrGhastFireball fireball = new UrGhastFireball(this.ghast.level(), this.ghast, offsetX, offsetY, offsetZ, 1);
		fireball.setPos(
				this.ghast.getX() + lookVec.x() * shotSpawnDistance,
				this.ghast.getY() + this.ghast.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance + 2.0D,
				this.ghast.getZ() + lookVec.z() * shotSpawnDistance);
		this.ghast.level().addFreshEntity(fireball);

		for (int i = 0; i < 2; i++) {
			fireball = new UrGhastFireball(this.ghast.level(), this.ghast, offsetX + (this.ghast.getRandom().nextFloat() - this.ghast.getRandom().nextFloat()) * 8.0D, offsetY, offsetZ + (this.ghast.getRandom().nextFloat() - this.ghast.getRandom().nextFloat()) * 8.0D, 1);
			fireball.setPos(
					this.ghast.getX() + lookVec.x() * shotSpawnDistance,
					this.ghast.getY() + this.ghast.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance,
					this.ghast.getZ() + lookVec.z() * shotSpawnDistance);
			this.ghast.level().addFreshEntity(fireball);
		}
	}
}
