package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import twilightforest.entity.boss.Naga;
import twilightforest.init.TFSounds;
import twilightforest.util.EntityUtil;

import java.util.EnumSet;

public class NagaMovementPattern extends Goal {

	private final Naga naga;
	private MovementState state;
	private int stateCounter;
	private boolean clockwise;

	public NagaMovementPattern(Naga naga) {
		this.naga = naga;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		this.stop();
	}

	@Override
	public boolean canUse() {
		return this.naga.getTarget() != null;
	}

	@Override
	public void stop() {
		this.state = MovementState.CIRCLE;
		this.stateCounter = 15;
		this.clockwise = false;
	}

	@Override
	public void tick() {
		if (!this.naga.getNavigation().isDone()) {
			// If we still have an uncompleted path don't run yet
			// This isn't in shouldExecute/shouldContinueExecuting because we don't want to reset the task

			//ok im pretty sure this bit right here was what was causing the naga to become undazed after hitting a shield sometimes
			//this.naga.setDazed(false); // Since we have a path, we shouldn't be dazed anymore.
			return;
		}

		switch (this.state) {
			case INTIMIDATE -> {
				this.naga.getNavigation().stop();
				this.naga.getLookControl().setLookAt(this.naga.getTarget(), 30.0F, 30.0F);
				this.naga.lookAt(this.naga.getTarget(), 30.0F, 30.0F);
				this.naga.zza = 0.1f;
			}
			case CRUMBLE -> {
				this.naga.getNavigation().stop();
				this.crumbleBelowTarget(2);
				this.crumbleBelowTarget(3);
			}
			case CHARGE -> {
				BlockPos tpoint = this.findCirclePoint(clockwise, 14, Math.PI);
				this.naga.getNavigation().moveTo(tpoint.getX(), tpoint.getY(), tpoint.getZ(), 1.0D);
				this.naga.setCharging(true);
			}
			case CIRCLE -> {
				// normal radius is 13
				double radius = this.stateCounter % 2 == 0 ? 12.0D : 14.0D;
				double rotation = 1; // in radians

				// hook out slightly before circling
				if (this.stateCounter == 2) {
					radius = 16;
				}

				// head almost straight at the player at the end
				if (this.stateCounter == 1) {
					rotation = 0.1D;
				}

				BlockPos tpoint = this.findCirclePoint(this.clockwise, radius, rotation);
				this.naga.getNavigation().moveTo(tpoint.getX(), tpoint.getY(), tpoint.getZ(), 1.0D);
			}
			case DAZE -> {
				this.naga.getNavigation().stop();
				this.naga.setDazed(true);
				this.naga.setCharging(false);
			}
		}

		this.stateCounter--;
		if (this.stateCounter <= 0) {
			this.transitionState();
		}
	}

	private void transitionState() {
		this.naga.setDazed(false);
		this.naga.setCharging(false);
		switch (this.state) {
			case INTIMIDATE -> {
				this.clockwise = !this.clockwise;

				if (this.naga.getTarget().getBoundingBox().minY > this.naga.getBoundingBox().maxY) {
					this.doCrumblePlayer();
				} else {
					this.doCharge();
				}
			}
			case CRUMBLE -> this.doCharge();
			case CHARGE, DAZE -> this.doCircle();
			case CIRCLE -> this.doIntimidate();
		}
	}

	public void doDaze() {
		this.state = MovementState.DAZE;
		this.naga.getNavigation().stop();
		this.stateCounter = 60 + this.naga.getRandom().nextInt(40);
	}

	public void doCircle() {
		this.state = MovementState.CIRCLE;
		this.stateCounter += 10 + this.naga.getRandom().nextInt(10);
		this.naga.goNormal();
	}

	public void doCrumblePlayer() {
		this.state = MovementState.CRUMBLE;
		this.stateCounter = 20 + this.naga.getRandom().nextInt(20);
		this.naga.goSlow();
	}

	/**
	 * Charge the player.  Although the count is 3, we actually charge only 2 times.
	 */
	private void doCharge() {
		this.state = MovementState.CHARGE;
		this.stateCounter = 3;
		this.naga.goFast();
	}

	private void doIntimidate() {
		this.state = MovementState.INTIMIDATE;
		this.naga.playSound(TFSounds.NAGA_RATTLE.get(), 4.0F, this.naga.getVoicePitch());

		this.stateCounter += 15 + this.naga.getRandom().nextInt(10);
		this.naga.goSlow();
	}

	private void crumbleBelowTarget(int range) {
		if (!this.naga.getLevel().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) return;

		int floor = (int) this.naga.getBoundingBox().minY;
		int targetY = (int) this.naga.getTarget().getBoundingBox().minY;

		if (targetY > floor) {
			int dx = (int) this.naga.getTarget().getX() + this.naga.getRandom().nextInt(range) - this.naga.getRandom().nextInt(range);
			int dz = (int) this.naga.getTarget().getZ() + this.naga.getRandom().nextInt(range) - this.naga.getRandom().nextInt(range);
			int dy = targetY - this.naga.getRandom().nextInt(range) + this.naga.getRandom().nextInt(range > 1 ? range - 1 : range);

			if (dy <= floor) {
				dy = targetY;
			}

			BlockPos pos = new BlockPos(dx, dy, dz);

			if (EntityUtil.canDestroyBlock(this.naga.getLevel(), pos, this.naga)) {
				// todo limit what can be broken
				this.naga.getLevel().destroyBlock(pos, true);

				// sparkle!!
				for (int k = 0; k < 20; k++) {
					double d = this.naga.getRandom().nextGaussian() * 0.02D;
					double d1 = this.naga.getRandom().nextGaussian() * 0.02D;
					double d2 = this.naga.getRandom().nextGaussian() * 0.02D;

					this.naga.getLevel().addParticle(ParticleTypes.CRIT,
							(this.naga.getX() + this.naga.getRandom().nextFloat() * this.naga.getBbWidth() * 2.0F) - this.naga.getBbWidth(),
							this.naga.getY() + this.naga.getRandom().nextFloat() * this.naga.getBbHeight(),
							(this.naga.getZ() + this.naga.getRandom().nextFloat() * this.naga.getBbWidth() * 2.0F) - this.naga.getBbWidth(),
							d, d1, d2);
				}
			}
		}
	}

	/**
	 * Finds a point that allows us to circle the target clockwise.
	 */
	private BlockPos findCirclePoint(boolean clockwise, double radius, double rotation) {
		LivingEntity toCircle = this.naga.getTarget();

		// compute angle
		double vecx = this.naga.getX() - toCircle.getX();
		double vecz = this.naga.getZ() - toCircle.getZ();
		float rangle = (float) (Math.atan2(vecz, vecx));

		// add a little, so he circles (clockwise)
		rangle += clockwise ? rotation : -rotation;

		// figure out where we're headed from the target angle
		double dx = Mth.cos(rangle) * radius;
		double dz = Mth.sin(rangle) * radius;

		double dy = Math.min(this.naga.getBoundingBox().minY, toCircle.getY());

		// add that to the target entity's position, and we have our destination
		return new BlockPos(toCircle.getX() + dx, dy, toCircle.getZ() + dz);
	}

	public MovementState getState() {
		return this.state;
	}

	public enum MovementState {
		INTIMIDATE,
		CRUMBLE,
		CHARGE,
		CIRCLE,
		DAZE
	}
}