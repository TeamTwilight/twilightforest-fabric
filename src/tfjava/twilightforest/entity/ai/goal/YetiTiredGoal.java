package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.YetiTiredGoal} —
 * runs after a rampage to keep the Alpha Yeti immobile and panting for the
 * configured duration.
 *
 * <p>Codex Fabric port note: {@code TFSounds.ALPHA_YETI_PANT.get()} →
 * {@code TFSounds.ALPHA_YETI_PANT} (codex returns SoundEvent directly).</p>
 */
public class YetiTiredGoal extends Goal {

	private final AlphaYeti yeti;
	private final int tiredDuration;
	private int tiredTimer;

	@SuppressWarnings("this-escape")
	public YetiTiredGoal(AlphaYeti alpha, int i) {
		this.yeti = alpha;
		this.tiredDuration = i;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return this.yeti.isTired();
	}

	@Override
	public boolean canContinueToUse() {
		return this.tiredTimer < this.tiredDuration;
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

	@Override
	public void start() {
		this.tiredTimer = 0;
	}

	@Override
	public void stop() {
		this.tiredTimer = 0;
		this.yeti.setTired(false);
	}

	@Override
	public void tick() {
		if (++this.tiredTimer % 10 == 0)
			this.yeti.playSound(TFSounds.ALPHA_YETI_PANT, 4F, 0.5F + this.yeti.getRandom().nextFloat() * 0.5F);
	}
}
