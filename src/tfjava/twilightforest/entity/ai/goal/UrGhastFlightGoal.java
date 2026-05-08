package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.boss.UrGhast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class UrGhastFlightGoal extends Goal {

	private static final int HOVER_ALTITUDE = 20;
	private final UrGhast ghast;

	private List<BlockPos> pointsToVisit = new ArrayList<>();
	private int currentPoint;

	@SuppressWarnings("this-escape")
	public UrGhastFlightGoal(UrGhast ghast) {
		this.ghast = ghast;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		MoveControl moveControl = this.ghast.getMoveControl();
		this.pointsToVisit = this.createPath();
		if (this.pointsToVisit.isEmpty()) {
			return false;
		}

		if (!moveControl.hasWanted()) {
			return true;
		}

		double d0 = moveControl.getWantedX() - this.ghast.getX();
		double d1 = moveControl.getWantedY() - this.ghast.getY();
		double d2 = moveControl.getWantedZ() - this.ghast.getZ();
		double distance = d0 * d0 + d1 * d1 + d2 * d2;
		return distance < 1.0D || distance > 3600.0D;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		if (this.pointsToVisit.isEmpty()) {
			this.pointsToVisit.addAll(this.createPath());
			return;
		}

		if (this.currentPoint >= this.pointsToVisit.size()) {
			this.currentPoint = 0;

			if (this.ghast.level() instanceof ServerLevel serverLevel && !this.ghast.checkGhastsAtTraps()) {
				this.ghast.spawnGhastsAtTraps(serverLevel);
			}
		}

		BlockPos point = this.pointsToVisit.get(this.currentPoint);
		double x = point.getX();
		double y = point.getY() + (this.ghast.getRestrictionPoint() != null ? HOVER_ALTITUDE : 0);
		double z = point.getZ();
		this.ghast.getMoveControl().setWantedPosition(x, y, z, 1.0F);
		this.currentPoint++;

		this.ghast.noPhysics = false;
	}

	private List<BlockPos> createPath() {
		List<BlockPos> potentialPoints = new ArrayList<>();
		BlockPos pos = this.ghast.getLogicalScanPoint();

		if (!this.ghast.getTrapLocations().isEmpty()) {
			potentialPoints.addAll(this.ghast.getTrapLocations());
		} else {
			potentialPoints.add(pos.offset(20, 0, 0));
			potentialPoints.add(pos.offset(0, 0, -20));
			potentialPoints.add(pos.offset(-20, 0, 0));
			potentialPoints.add(pos.offset(0, 0, 20));
		}

		Collections.shuffle(potentialPoints);

		if (this.ghast.getTrapLocations().isEmpty()) {
			potentialPoints.add(pos);
		}

		return potentialPoints;
	}
}
