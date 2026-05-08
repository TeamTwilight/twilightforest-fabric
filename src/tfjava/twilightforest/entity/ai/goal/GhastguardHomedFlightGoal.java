package twilightforest.entity.ai.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.CarminiteGhastguard;

import java.util.EnumSet;

public class GhastguardHomedFlightGoal extends Goal {
	private final CarminiteGhastguard ghast;

	@SuppressWarnings("this-escape")
	public GhastguardHomedFlightGoal(CarminiteGhastguard ghast) {
		this.ghast = ghast;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		MoveControl control = this.ghast.getMoveControl();
		if (!control.hasWanted()) {
			return !this.ghast.isMobWithinHomeArea(this.ghast);
		}
		double dx = control.getWantedX() - this.ghast.getX();
		double dy = control.getWantedY() - this.ghast.getY();
		double dz = control.getWantedZ() - this.ghast.getZ();
		double distance = dx * dx + dy * dy + dz * dz;
		return (distance < 1.0D || distance > 3600.0D) && !this.ghast.isMobWithinHomeArea(this.ghast);
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		if (!this.ghast.isRestrictionPointValid(this.ghast.level().dimension())) {
			this.stop();
			return;
		}
		RandomSource random = this.ghast.getRandom();
		double x = this.ghast.getX() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		double y = this.ghast.getY() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		double z = this.ghast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		this.ghast.getMoveControl().setWantedPosition(x, y, z, 1.0D);
		if (this.ghast.distanceToSqr(Vec3.atLowerCornerOf(this.ghast.getRestrictionPoint().pos())) > 256.0D) {
			Vec3 toHome = Vec3.atLowerCornerOf(this.ghast.getRestrictionPoint().pos()).subtract(this.ghast.position()).normalize();
			double targetX = this.ghast.getX() + toHome.x() * this.ghast.getWanderFactor() + (this.ghast.getRandom().nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
			double targetY = this.ghast.getY() + toHome.y() * this.ghast.getWanderFactor() + (this.ghast.getRandom().nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
			double targetZ = this.ghast.getZ() + toHome.z() * this.ghast.getWanderFactor() + (this.ghast.getRandom().nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
			this.ghast.getMoveControl().setWantedPosition(targetX, targetY, targetZ, 1.0D);
		} else {
			this.ghast.getMoveControl().setWantedPosition(this.ghast.getRestrictionPoint().pos().getX() + 0.5D, this.ghast.getRestrictionPoint().pos().getY(), this.ghast.getRestrictionPoint().pos().getZ() + 0.5D, 1.0D);
		}
	}
}
