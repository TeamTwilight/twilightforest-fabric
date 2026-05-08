package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

/**
 * 1:1 port of upstream {@code twilightforest.entity.ai.goal.YetiRampageGoal} —
 * Alpha Yeti's rampage state: bounces in place destroying nearby blocks, makes
 * random and target-tracking blocks fall, periodically lobs ice bombs.
 *
 * <p>Codex Fabric port note: {@code TFSounds.X.get()} → {@code TFSounds.X};
 * {@code TFEntities.THROWN_ICE.get()} retains {@code .get()} (TFRegistryObject).</p>
 */
public class YetiRampageGoal extends Goal {

	private final AlphaYeti yeti;
	private int currentTimeOut;
	private int currentDuration;
	private final int maxTantrumTimeOut;
	private final int tantrumDuration;

	@SuppressWarnings("this-escape")
	public YetiRampageGoal(AlphaYeti yeti, int timeout, int duration) {
		this.yeti = yeti;
		this.currentTimeOut = timeout;
		this.maxTantrumTimeOut = timeout;
		this.tantrumDuration = duration;

		this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		if (this.yeti.getTarget() != null && this.yeti.canRampage()) {
			this.currentTimeOut--;
		}

		return this.currentTimeOut <= 0;
	}

	@Override
	public void start() {
		this.currentDuration = this.tantrumDuration;
		this.yeti.setRampaging(true);
		this.yeti.playSound(TFSounds.ALPHA_YETI_ROAR, 4F, 0.5F + yeti.getRandom().nextFloat() * 0.5F);
		this.yeti.gameEvent(GameEvent.ENTITY_ACTION);
	}

	@Override
	public boolean canContinueToUse() {
		return currentDuration > 0;
	}

	@Override
	public void tick() {
		this.currentDuration--;

		if (this.yeti.getTarget() != null) {
			this.yeti.getLookControl().setLookAt(this.yeti.getTarget(), 10.0F, this.yeti.getMaxHeadXRot());
		}

		if (this.yeti.onGround()) {
			this.yeti.setDeltaMovement(0, 0.4D, 0);
			this.yeti.gameEvent(GameEvent.HIT_GROUND);
		}

		this.yeti.destroyBlocksInAABB(this.yeti.getBoundingBox().inflate(1, 2, 1).move(0, 2, 0));

		// Regular falling blocks, twice a second.
		if (this.currentDuration % 10 == 0) {
			this.yeti.makeRandomBlockFall(30, 80);
		}

		// Blocks target players, one every second.
		if (this.currentDuration % 20 == 0) {
			this.yeti.makeBlockAboveTargetFall();
		}

		// Blocks that fall close to the yeti, twice a second near the end of the rampage.
		if (this.currentDuration < 40 && this.currentDuration % 10 == 0) {
			this.yeti.makeRandomBlockFall(15, 40);
		}

		if (this.currentDuration % 20 == 0) {
			// Codex Fabric: codex's IceBomb only exposes (Level, LivingEntity)
			// owner-binding constructor — entity type is auto-resolved internally.
			IceBomb ice = new IceBomb(this.yeti.level(), this.yeti);
			Vec3 vec = new Vec3(0.5F + this.yeti.getRandom().nextFloat() * 0.5F, 0.5F + this.yeti.getRandom().nextFloat() * 0.3F, 0).yRot(this.yeti.getRandom().nextFloat() * 360F);
			ice.shoot(vec.x(), vec.y(), vec.z(), 0.4F + yeti.getRandom().nextFloat() * 0.3F, 0);
			this.yeti.playSound(TFSounds.ALPHA_YETI_ICE, 1.0F, 1.0F / (this.yeti.getRandom().nextFloat() * 0.4F + 0.8F));
			this.yeti.gameEvent(GameEvent.PROJECTILE_SHOOT);
			this.yeti.level().addFreshEntity(ice);
		}
	}

	@Override
	public void stop() {
		this.currentTimeOut = this.maxTantrumTimeOut;
		this.yeti.setRampaging(false);
		this.yeti.setTired(true);
	}
}
