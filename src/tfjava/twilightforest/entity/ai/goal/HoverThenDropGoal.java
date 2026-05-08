package twilightforest.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.entity.boss.SnowQueen;
import twilightforest.entity.boss.SnowQueen.Phase;

import java.util.EnumSet;

public class HoverThenDropGoal extends HoverBaseGoal<SnowQueen> {

	private int hoverTimer;
	private int dropTimer;
	private int seekTimer;

	private final int maxHoverTime;
	private final int maxDropTime;
	private final int maxSeekTime;

	private double dropY;

	@SuppressWarnings("this-escape")
	public HoverThenDropGoal(SnowQueen snowQueen, int hoverTime, int dropTime) {
		super(snowQueen, 6.0F, 0.0F);

		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		this.maxHoverTime = hoverTime;
		this.maxSeekTime = hoverTime;
		this.maxDropTime = dropTime;

		this.hoverTimer = 0;
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.attacker.getTarget();

		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else {
			return this.attacker.getCurrentPhase() == Phase.DROP;
		}
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = this.attacker.getTarget();

		if (target == null || !target.isAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.DROP) {
			return false;
		} else if (this.seekTimer > this.maxSeekTime) {
			return false;
		} else if (this.attacker.distanceToSqr(this.hoverPosX, this.hoverPosY, this.hoverPosZ) <= 1.0F) {
			this.hoverTimer++;
			return true;
		} else if (this.dropTimer < this.maxDropTime) {
			return true;
		} else {
			this.attacker.incrementSuccessfulDrops();
			return false;
		}
	}

	@Override
	public void stop() {
		this.hoverTimer = 0;
		this.dropTimer = 0;
	}

	@Override
	public void tick() {
		if (this.hoverTimer > 0) {
			this.hoverTimer++;
		} else {
			this.seekTimer++;
		}

		if (this.hoverTimer < this.maxHoverTime) {
			double offsetX = this.hoverPosX - this.attacker.getX();
			double offsetY = this.hoverPosY - this.attacker.getY();
			double offsetZ = this.hoverPosZ - this.attacker.getZ();

			double distanceDesired = Mth.sqrt((float) (offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ));

			if (distanceDesired > 0.0D) {
				double velX = offsetX / distanceDesired * 0.05D;
				double velY = offsetY / distanceDesired * 0.1D;
				double velZ = offsetZ / distanceDesired * 0.05D;

				velY += 0.05D;

				this.attacker.push(velX, velY, velZ);
			}

			LivingEntity target = this.attacker.getTarget();
			if (target != null) {
				this.attacker.lookAt(target, 30.0F, 30.0F);
				this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
			}
		} else {
			this.dropTimer++;
			if (this.attacker.getY() > this.dropY) {
				this.attacker.destroyBlocksInAABB(this.attacker.getBoundingBox().inflate(1.0D, 0.5F, 1.0D));
			}
		}
	}

	@Override
	protected void makeNewHoverSpot(LivingEntity target) {
		super.makeNewHoverSpot(target);
		this.dropY = target.getY() - 1.0F;
		this.seekTimer = 0;
	}
}
