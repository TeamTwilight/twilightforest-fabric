package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.AlwaysWatchTargetGoal} —
 * forces the mob's look-control onto its current target every tick with effectively
 * unbounded yaw/pitch step. Used by mobs whose attack/defense AI relies on the head
 * always tracking the target (Lich, Snow Queen).
 */
public class AlwaysWatchTargetGoal extends Goal {

	private final Mob mob;

	public AlwaysWatchTargetGoal(Mob mob) {
		this.mob = mob;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.mob.getTarget() != null) {
			this.mob.getLookControl().setLookAt(this.mob.getTarget(), 100.0F, 100.0F);
		}
	}
}
