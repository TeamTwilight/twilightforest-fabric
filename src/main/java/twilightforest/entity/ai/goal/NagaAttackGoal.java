package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.boss.Naga;

public class NagaAttackGoal extends Goal {

	private final Naga naga;
	private int attackTick = 20;

	public NagaAttackGoal(Naga naga) {
		this.naga = naga;
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.naga.getTarget();

		return target != null
				&& target.getBoundingBox().maxY > this.naga.getBoundingBox().minY - 2.5
				&& target.getBoundingBox().minY < this.naga.getBoundingBox().maxY + 2.5
				&& this.naga.distanceToSqr(target) <= 4.0D
				&& this.naga.getSensing().hasLineOfSight(target);

	}

	@Override
	public void tick() {
		if (this.attackTick > 0) {
			this.attackTick--;
		}
	}

	@Override
	public void stop() {
		attackTick = 20;
	}

	@Override
	public void start() {
		this.naga.doHurtTarget(naga.getTarget());
		this.attackTick = 20;
	}
}