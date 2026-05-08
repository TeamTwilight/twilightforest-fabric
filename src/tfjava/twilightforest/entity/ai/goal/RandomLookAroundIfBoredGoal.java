package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.RandomLookAroundIfBoredGoal}
 * — vanilla {@code RandomLookAroundGoal} variant that only fires when the mob has no
 * combat target. Used by passive/boss mobs that should idle-glance when not in combat
 * but never break combat focus to look around.
 */
public class RandomLookAroundIfBoredGoal extends Goal {
	private final Mob mob;
	private double relX;
	private double relZ;
	private int lookTime;

	public RandomLookAroundIfBoredGoal(Mob mob) {
		this.mob = mob;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() == null && this.mob.getRandom().nextFloat() < 0.02F;
	}

	@Override
	public boolean canContinueToUse() {
		return this.mob.getTarget() == null && this.lookTime >= 0;
	}

	@Override
	public void start() {
		double d0 = 6.283185307179586 * this.mob.getRandom().nextDouble();
		this.relX = Math.cos(d0);
		this.relZ = Math.sin(d0);
		this.lookTime = 20 + this.mob.getRandom().nextInt(20);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		--this.lookTime;
		this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
	}
}
