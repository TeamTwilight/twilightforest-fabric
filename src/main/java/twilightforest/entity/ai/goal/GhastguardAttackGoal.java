package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.monster.CarminiteGhastguard;

// [VanillaCopy] EntityGhast.AIFireballAttack, edits noted
public class GhastguardAttackGoal extends Goal {
	private final CarminiteGhastguard ghast;
	public int attackTimer;
	public int prevAttackTimer; // TF - add for renderer

	public GhastguardAttackGoal(CarminiteGhastguard ghast) {
		this.ghast = ghast;
	}

	@Override
	public boolean canUse() {
		return this.ghast.getTarget() != null && ghast.shouldAttack(ghast.getTarget());
	}

	@Override
	public void start() {
		this.attackTimer = this.prevAttackTimer = 0;
	}

	@Override
	public void stop() {
		this.ghast.setCharging(false);
	}

	@Override
	public void tick() {
		LivingEntity target = this.ghast.getTarget();

		if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.getSensing().hasLineOfSight(target)) {
			this.prevAttackTimer = attackTimer;
			++this.attackTimer;

			// TF face our target at all times
			this.ghast.getLookControl().setLookAt(target, 10F, this.ghast.getMaxHeadXRot());

			if (this.attackTimer == 10) {
				ghast.playSound(ghast.getWarnSound(), 10.0F, ghast.getVoicePitch());
			}

			if (this.attackTimer == 20) {
				if (this.ghast.shouldAttack(target)) {
					// TF - call custom method
					this.ghast.playSound(ghast.getFireSound(), 10.0F, this.ghast.getVoicePitch());
					this.ghast.spitFireball();
					this.prevAttackTimer = attackTimer;
				}
				this.attackTimer = -40;
			}
		} else if (this.attackTimer > 0) {
			this.prevAttackTimer = attackTimer;
			--this.attackTimer;
		}

		this.ghast.setCharging(this.attackTimer > 10);
	}
}