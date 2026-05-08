package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.Kobold;

import java.util.EnumSet;
import java.util.List;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.PanicOnFlockDeathGoal} —
 * causes flock-creatures (Kobolds primarily) to flee for ~2 seconds when any
 * member of the flock within 4 blocks dies. Toggles the Kobold panic animation
 * flag while active.
 */
public class PanicOnFlockDeathGoal extends Goal {
	private final PathfinderMob flockCreature;
	private final float speed;
	private double fleeX;
	private double fleeY;
	private double fleeZ;

	private int fleeTimer;

	@SuppressWarnings("this-escape")
	public PanicOnFlockDeathGoal(PathfinderMob creature, float speed) {
		this.flockCreature = creature;
		this.speed = speed;
		this.setFlags(EnumSet.of(Flag.MOVE));
		this.fleeTimer = 0;
	}

	@Override
	public boolean canUse() {
		boolean yikes = fleeTimer > 0;

		// Check if any of us is dead within 4 squares.
		List<? extends PathfinderMob> flockList = this.flockCreature.level().getEntitiesOfClass(this.flockCreature.getClass(), this.flockCreature.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
		for (LivingEntity flocker : flockList) {
			if (flocker.deathTime > 0) {
				yikes = true;
				break;
			}
		}

		if (!yikes) {
			return false;
		} else {
			Vec3 target = DefaultRandomPos.getPos(this.flockCreature, 5, 4);

			if (target == null) {
				return false;
			} else {
				this.fleeX = target.x();
				this.fleeY = target.y();
				this.fleeZ = target.z();
				return true;
			}
		}
	}

	@Override
	public void start() {
		this.fleeTimer = 40;
		this.flockCreature.getNavigation().moveTo(this.fleeX, this.fleeY, this.fleeZ, this.speed);

		if (flockCreature instanceof Kobold kobold) {
			kobold.setPanicked(true);
		}
	}

	@Override
	public boolean canContinueToUse() {
		return fleeTimer > 0 && !this.flockCreature.getNavigation().isDone();
	}

	@Override
	public void tick() {
		fleeTimer--;
	}

	@Override
	public void stop() {
		fleeTimer -= 20;

		if (flockCreature instanceof Kobold kobold) {
			kobold.setPanicked(false);
		}
	}
}
