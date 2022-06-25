package twilightforest.entity.ai.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.CarminiteGhastguard;

import java.util.EnumSet;

// [VanillaCopy]-ish mixture of Ghast.RandomFloatAroundGoal and MoveTowardsRestrictionGoal
public class GhastguardHomedFlightGoal extends Goal {
	private final CarminiteGhastguard parentEntity;

	public GhastguardHomedFlightGoal(CarminiteGhastguard ghast) {
		this.parentEntity = ghast;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	// From RandomFloatAroundGoal, but with extra condition from MoveTowardsRestrictionGoal
	@Override
	public boolean canUse() {
		MoveControl control = this.parentEntity.getMoveControl();

		if (!control.hasWanted()) {
			return !this.parentEntity.isWithinRestriction();
		} else {
			double d0 = control.getWantedX() - this.parentEntity.getX();
			double d1 = control.getWantedY() - this.parentEntity.getY();
			double d2 = control.getWantedZ() - this.parentEntity.getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return (d3 < 1.0D || d3 > 3600.0D)
					&& !this.parentEntity.isWithinRestriction();
		}
	}

	// From RandomFloatAroundGoal
	@Override
	public boolean canContinueToUse() {
		return false;
	}

	// From MoveTowardsRestrictionGoal but use move helper instead of PathNavigate
	@Override
	public void start() {
		RandomSource random = this.parentEntity.getRandom();
		double d0 = this.parentEntity.getX() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.getWanderFactor();
		double d1 = this.parentEntity.getY() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.getWanderFactor();
		double d2 = this.parentEntity.getZ() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.getWanderFactor();
		this.parentEntity.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);

		if (this.parentEntity.distanceToSqr(Vec3.atLowerCornerOf(this.parentEntity.getRestrictCenter())) > 256.0D) {
			Vec3 vecToHome = Vec3.atLowerCornerOf(this.parentEntity.getRestrictCenter()).subtract(this.parentEntity.position()).normalize();

			double targetX = this.parentEntity.getX() + vecToHome.x() * parentEntity.getWanderFactor() + (this.parentEntity.getRandom().nextFloat() * 2.0F - 1.0F) * parentEntity.getWanderFactor();
			double targetY = this.parentEntity.getY() + vecToHome.y() * parentEntity.getWanderFactor() + (this.parentEntity.getRandom().nextFloat() * 2.0F - 1.0F) * parentEntity.getWanderFactor();
			double targetZ = this.parentEntity.getZ() + vecToHome.z() * parentEntity.getWanderFactor() + (this.parentEntity.getRandom().nextFloat() * 2.0F - 1.0F) * parentEntity.getWanderFactor();

			this.parentEntity.getMoveControl().setWantedPosition(targetX, targetY, targetZ, 1.0D);
		} else {
			this.parentEntity.getMoveControl().setWantedPosition(this.parentEntity.getRestrictCenter().getX() + 0.5D, this.parentEntity.getRestrictCenter().getY(), this.parentEntity.getRestrictCenter().getZ() + 0.5D, 1.0D);
		}
	}
}