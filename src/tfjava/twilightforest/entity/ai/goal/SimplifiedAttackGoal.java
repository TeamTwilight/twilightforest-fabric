package twilightforest.entity.ai.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.SimplifiedAttackGoal} —
 * stripped-down replacement for vanilla {@code MeleeAttackGoal} that doesn't move,
 * just swings + does damage when target is in melee reach. Used by stationary or
 * AI-driven mobs that handle their own movement.
 */
public class SimplifiedAttackGoal extends Goal {

	private final Mob mob;
	private int attackTick;

	public SimplifiedAttackGoal(Mob mob) {
		this.mob = mob;
	}

	@Override
	public boolean canUse() {
		LivingEntity target = mob.getTarget();
		return target != null && this.mob.isWithinMeleeAttackRange(target);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.attackTick = 0;
	}

	@Override
	public void stop() {
		this.attackTick = 0;
	}

	@Override
	public void tick() {
		if (this.attackTick > 0) {
			this.attackTick--;
		} else {
			LivingEntity livingentity = this.mob.getTarget();
			if (livingentity == null) {
				this.stop();
				return;
			}
			this.checkAndPerformAttack(livingentity);
		}
	}

	protected void checkAndPerformAttack(LivingEntity entity) {
		if (this.attackTick <= 0 && this.mob.isWithinMeleeAttackRange(entity) && this.mob.hasLineOfSight(entity)) {
			this.attackTick = this.adjustedTickDelay(20);
			this.mob.swing(InteractionHand.MAIN_HAND);
			this.mob.doHurtTarget(entity);
		}
	}
}
