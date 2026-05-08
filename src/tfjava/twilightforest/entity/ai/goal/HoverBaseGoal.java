package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.HoverBaseGoal} —
 * abstract parent for Snow-Queen-style hover goals. Picks a random unobstructed
 * spot above the target entity and stores it as {@code hoverPosX/Y/Z} for
 * subclass tick logic.
 */
public abstract class HoverBaseGoal<T extends Mob> extends Goal {

	protected final T attacker;

	protected final float hoverHeight;
	protected final float hoverRadius;

	protected double hoverPosX;
	protected double hoverPosY;
	protected double hoverPosZ;

	protected HoverBaseGoal(T snowQueen, float hoverHeight, float hoverRadius) {
		this.attacker = snowQueen;
		this.hoverHeight = hoverHeight;
		this.hoverRadius = hoverRadius;
	}

	@Override
	public void start() {
		LivingEntity target = this.attacker.getTarget();
		if (target != null) {
			this.makeNewHoverSpot(target);
		}
	}

	/**
	 * Make a new spot to hover at!
	 */
	protected void makeNewHoverSpot(LivingEntity target) {
		double hx = 0, hy = 0, hz = 0;

		boolean found = false;

		for (int i = 0; i < 100; i++) {
			hx = target.getX() + (this.attacker.getRandom().nextFloat() - this.attacker.getRandom().nextFloat()) * this.hoverRadius;
			hy = target.getY() + this.hoverHeight;
			hz = target.getZ() + (this.attacker.getRandom().nextFloat() - this.attacker.getRandom().nextFloat()) * this.hoverRadius;

			if (!this.isPositionOccupied(hx, hy, hz) && this.attacker.hasLineOfSight(target)) {
				found = true;
				break;
			}
		}

		if (!found) {
			TwilightForestMod.LOGGER.debug("Found no spots, giving up");
		}

		this.hoverPosX = hx;
		this.hoverPosY = hy;
		this.hoverPosZ = hz;
	}

	protected boolean isPositionOccupied(double hx, double hy, double hz) {
		float radius = this.attacker.getBbWidth() / 2F;
		AABB aabb = new AABB(hx - radius, hy, hz - radius, hx + radius, hy + this.attacker.getBbHeight(), hz + radius);
		return !this.attacker.level().isUnobstructed(attacker, Shapes.create(aabb)) || !this.attacker.level().noCollision(attacker, aabb);
	}
}
