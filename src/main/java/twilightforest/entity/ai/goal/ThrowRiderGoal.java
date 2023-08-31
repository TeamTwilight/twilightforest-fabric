package twilightforest.entity.ai.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import io.github.fabricators_of_create.porting_lib.tags.Tags;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.thrown.YetiThrowCapabilityHandler;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.events.HostileMountEvents;

public class ThrowRiderGoal extends MeleeAttackGoal {

	private int throwTimer;
	private int timeout;
	private int cooldown;

	public ThrowRiderGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
	}

	@Override
	public boolean canUse() {
		return this.mob.getPassengers().isEmpty() &&
				this.mob.getTarget() != null &&
				!this.mob.getTarget().getType().is(Tags.EntityTypes.BOSSES) &&
				CapabilityList.YETI_THROWN.maybeGet(this.mob.getTarget()).map(cap -> cap.getThrowCooldown() <= 0).orElse(true) &&
				super.canUse();
	}

	@Override
	public void start() {
		this.throwTimer = 10 + this.mob.getRandom().nextInt(30); // Wait 0.5 to 2 seconds before we throw the target
		this.timeout = 80 + this.mob.getRandom().nextInt(40); // Lets only try to chase for around 4-6 seconds
		super.start();
	}

	@Override
	public void tick() {
		this.timeout--;
		if (!this.mob.getPassengers().isEmpty())
			this.throwTimer--;
		else
			super.tick();
	}

	// Vanilla Copy with edits
	@Override
	protected void checkAndPerformAttack(LivingEntity victim, double p_190102_2_) {
		double d0 = this.getAttackReachSqr(victim);

		if (p_190102_2_ <= d0 && this.getTicksUntilNextAttack() <= 0 && this.mob.getPassengers().isEmpty() && this.cooldown-- == 0) {
			this.cooldown = 3; // Gives the thrower a pause so it doesn't pick the target back up immediately after throwing; for whatever reason the attack cooldown isn't enough...
			this.resetAttackCooldown();
			this.mob.swing(InteractionHand.MAIN_HAND);
			if (this.mob.getPassengers().isEmpty()) {
				var v = victim.getVehicle();

				if (v == null || !v.getType().is(EntityTagGenerator.RIDES_OBSTRUCT_SNATCHING)) {
					// Pluck them from the boat, minecart, donkey, or whatever
					victim.stopRiding();

					victim.startRiding(this.mob, true);
				}
			}
		}
	}

	@Override
	public void stop() {
		if (!this.mob.getPassengers().isEmpty()) {
			Entity rider = this.mob.getPassengers().get(0);
			HostileMountEvents.hostileDismount(rider);

			Vec3 throwVec = new Vec3(this.mob.getLookAngle().x() * 2.0D, 0.9, this.mob.getLookAngle().z() * 2.0D);

			if (rider instanceof LivingEntity living) {
				CapabilityList.YETI_THROWN.maybeGet(living).ifPresent(cap -> {
					if (living instanceof Player) cap.setThrown(true, this.mob);
					// Make it so other yetis won't try to pick us up for a bit, 10 seconds seems fair
					cap.setThrowVector(throwVec);
					cap.setThrowCooldown(YetiThrowCapabilityHandler.THROW_COOLDOWN);
				});
			} else rider.push(throwVec.x(), throwVec.y(), throwVec.z());
		}
		super.stop();
	}

	@Override
	public boolean canContinueToUse() {
		return (this.throwTimer > 0 && !this.mob.getPassengers().isEmpty()) || (this.timeout > 0 && super.canContinueToUse() && this.mob.getPassengers().isEmpty());
	}

}
