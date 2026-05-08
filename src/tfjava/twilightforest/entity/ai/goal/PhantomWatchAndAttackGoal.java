package twilightforest.entity.ai.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;
import twilightforest.entity.boss.KnightPhantom;

public class PhantomWatchAndAttackGoal extends Goal {

	private final KnightPhantom boss;
	private int attackTime;
	private int guardCoolDownTime;
	private boolean guard;

	public PhantomWatchAndAttackGoal(KnightPhantom entity) {
		this.boss = entity;
	}

	@Override
	public boolean canUse() {
		return this.boss.getTarget() != null;
	}

	@Override
	public void tick() {
		LivingEntity target = this.boss.getTarget();
		if (target == null || !target.isAlive()) {
			return;
		}
		this.boss.lookAt(target, 10.0F, 500.0F);
		float distance = target.distanceTo(this.boss);
		if (this.boss.getSensing().hasLineOfSight(target)
				&& this.attackTime-- <= 0
				&& distance < 2.0F
				&& target.getBoundingBox().maxY > this.boss.getBoundingBox().minY
				&& target.getBoundingBox().minY < this.boss.getBoundingBox().maxY) {
			this.attackTime = 20;
			this.boss.doHurtTarget(target);
		}

		if (this.boss.getOffhandItem().getItem() instanceof ShieldItem && this.boss.getCurrentFormation() != KnightPhantom.Formation.ATTACK_PLAYER_ATTACK && this.guard) {
			this.boss.startUsingItem(InteractionHand.OFF_HAND);
		} else {
			this.boss.stopUsingItem();
		}

		if (this.guard) {
			if (this.guardCoolDownTime <= 180) {
				++this.guardCoolDownTime;
			} else {
				this.guard = false;
			}
		} else if (this.guardCoolDownTime > 0) {
			--this.guardCoolDownTime;
		} else {
			this.guard = true;
		}
	}
}
