package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.monster.UpperGoblinKnight;

import java.util.EnumSet;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.HeavySpearAttackGoal} —
 * Upper Goblin Knight's heavy-spear wind-up + slam attack, driven by the host's
 * {@code heavySpearTimer} counter. The goal fires {@link UpperGoblinKnight#landHeavySpearAttack}
 * exactly once per cycle on tick 25 (the apex of the swing).
 */
public class HeavySpearAttackGoal extends Goal {

	private final UpperGoblinKnight entity;

	@SuppressWarnings("this-escape")
	public HeavySpearAttackGoal(UpperGoblinKnight upperKnight) {
		this.entity = upperKnight;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public void tick() {
		if (this.entity.heavySpearTimer == 25) {
			this.entity.landHeavySpearAttack();
		}
	}

	@Override
	public boolean canUse() {
		return this.entity.heavySpearTimer > 0 && this.entity.heavySpearTimer < UpperGoblinKnight.HEAVY_SPEAR_TIMER_START && this.entity.getTarget() != null && EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(this.entity.getTarget());
	}
}
