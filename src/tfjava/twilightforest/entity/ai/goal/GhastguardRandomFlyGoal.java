package twilightforest.entity.ai.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.monster.CarminiteGhastguard;

import java.util.EnumSet;

public class GhastguardRandomFlyGoal extends Goal {
	private final CarminiteGhastguard ghast;

	@SuppressWarnings("this-escape")
	public GhastguardRandomFlyGoal(CarminiteGhastguard ghast) {
		this.ghast = ghast;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		MoveControl control = this.ghast.getMoveControl();
		if (this.ghast.getTarget() != null) {
			return false;
		}
		if (!control.hasWanted()) {
			return true;
		}
		double dx = control.getWantedX() - this.ghast.getX();
		double dy = control.getWantedY() - this.ghast.getY();
		double dz = control.getWantedZ() - this.ghast.getZ();
		double distance = dx * dx + dy * dy + dz * dz;
		return distance < 1.0D || distance > 3600.0D;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		RandomSource random = this.ghast.getRandom();
		double x = this.ghast.getX() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		double y = this.ghast.getY() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		double z = this.ghast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		this.ghast.getMoveControl().setWantedPosition(x, y, z, 1.0D);
	}
}
