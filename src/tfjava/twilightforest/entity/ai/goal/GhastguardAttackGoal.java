package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.CarminiteGhastguard;

public class GhastguardAttackGoal extends Goal {
	private final CarminiteGhastguard ghast;
	public int attackTimer;
	public int previousAttackTimer;

	public GhastguardAttackGoal(CarminiteGhastguard ghast) {
		this.ghast = ghast;
	}

	@Override
	public boolean canUse() {
		return this.ghast.getTarget() != null && this.ghast.shouldAttack(this.ghast.getTarget());
	}

	@Override
	public void start() {
		this.attackTimer = this.previousAttackTimer = 0;
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
			this.previousAttackTimer = this.attackTimer;
			++this.attackTimer;
			this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());
			if (this.attackTimer == 10) {
				this.ghast.playSound(this.ghast.getWarnSound(), 10.0F, this.ghast.getVoicePitch());
			}
			if (this.attackTimer == 20) {
				if (this.ghast.shouldAttack(target)) {
					this.ghast.playSound(this.ghast.getFireSound(), 10.0F, this.ghast.getVoicePitch());
					this.spitFireball();
					this.previousAttackTimer = this.attackTimer;
				}
				this.attackTimer = -40;
			}
		} else if (this.attackTimer > 0) {
			this.previousAttackTimer = this.attackTimer;
			--this.attackTimer;
		}
		this.ghast.setCharging(this.attackTimer > 10);
	}

	public void spitFireball() {
		if (this.ghast.getTarget() == null) {
			return;
		}
		Vec3 view = this.ghast.getViewVector(1.0F);
		double dx = this.ghast.getTarget().getX() - (this.ghast.getX() + view.x() * 4.0D);
		double dy = this.ghast.getTarget().getBoundingBox().minY + this.ghast.getTarget().getBbHeight() / 2.0F - (0.5D + this.ghast.getY() + this.ghast.getBbHeight() / 2.0F);
		double dz = this.ghast.getTarget().getZ() - (this.ghast.getZ() + view.z() * 4.0D);
		LargeFireball fireball = new LargeFireball(this.ghast.level(), this.ghast, new Vec3(dx, dy, dz).normalize(), this.ghast.getExplosionPower());
		fireball.setPos(this.ghast.getX() + view.x() * 4.0D, this.ghast.getY() + this.ghast.getBbHeight() / 2.0F + 0.5D, this.ghast.getZ() + view.z() * 4.0D);
		this.ghast.level().addFreshEntity(fireball);
		if (this.ghast.getRandom().nextInt(6) == 0) {
			this.ghast.setTarget(null);
		}
	}
}
