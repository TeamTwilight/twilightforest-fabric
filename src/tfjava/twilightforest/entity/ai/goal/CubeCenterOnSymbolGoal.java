package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.RovingCube;

import java.util.EnumSet;

public class CubeCenterOnSymbolGoal extends Goal {
	private final RovingCube cube;
	private final double speed;

	private double xPosition;
	private double yPosition;
	private double zPosition;

	@SuppressWarnings("this-escape")
	public CubeCenterOnSymbolGoal(RovingCube cube, double speed) {
		this.cube = cube;
		this.xPosition = this.cube.symbolX;
		this.yPosition = this.cube.symbolY;
		this.zPosition = this.cube.symbolZ;
		this.speed = speed;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		this.xPosition = this.cube.symbolX;
		this.yPosition = this.cube.symbolY;
		this.zPosition = this.cube.symbolZ;
		return this.cube.getNavigation().isDone() && this.isCloseToSymbol();
	}

	@Override
	public boolean canContinueToUse() {
		this.cube.getMoveControl().setWantedPosition(this.xPosition + 0.5F, this.yPosition, this.zPosition + 0.5F, this.speed);
		return this.distanceFromSymbol() > 0.1F && this.isCourseTraversable();
	}

	private boolean isCourseTraversable() {
		return this.distanceFromSymbol() < 100.0D;
	}

	private boolean isCloseToSymbol() {
		double distance = this.distanceFromSymbol();
		return distance > 0.25F && distance < 10.0F;
	}

	private double distanceFromSymbol() {
		double dx = this.xPosition - this.cube.getX() + 0.5F;
		double dy = this.yPosition - this.cube.getY();
		double dz = this.zPosition - this.cube.getZ() + 0.5F;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
}
