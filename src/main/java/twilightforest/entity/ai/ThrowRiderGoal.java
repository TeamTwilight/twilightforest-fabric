package twilightforest.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFEventListener;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.ThrowPlayerPacket;

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
				!this.mob.getTarget().getType().is(EntityTagGenerator.BOSSES) &&
				super.canUse();
	}

	@Override
	public void start() {
		this.throwTimer = 10 + mob.getRandom().nextInt(30); // Wait 0.5 to 2 seconds before we throw the target
		timeout = 80 + mob.getRandom().nextInt(40); // Lets only try to chase for around 4-6 seconds
		super.start();
	}

	@Override
	public void tick() {
		timeout--;
		if (!mob.getPassengers().isEmpty())
			throwTimer--;
		else
			super.tick();
	}

	// Vanilla Copy with edits
	@Override
	protected void checkAndPerformAttack(LivingEntity victim, double p_190102_2_) {
		double d0 = this.getAttackReachSqr(victim);

		if (p_190102_2_ <= d0 && this.getTicksUntilNextAttack() <= 0 && mob.getPassengers().isEmpty() && cooldown-- == 0) {
			cooldown = 3; // Gives the thrower a pause so it doesn't pick the target back up immediately after throwing; for whatever reason the attack cooldown isn't enough...
			this.resetAttackCooldown();
			this.mob.swing(InteractionHand.MAIN_HAND);
			if (mob.getPassengers().isEmpty()) {
				var v = victim.getVehicle();

				if (v == null || !v.getType().is(EntityTagGenerator.RIDES_OBSTRUCT_SNATCHING)) {
					// Pluck them from the boat, minecart, donkey, or whatever
					victim.stopRiding();

					victim.startRiding(mob, true);
				}
			}
		}
	}

	@Override
	public void stop() {
		if (!mob.getPassengers().isEmpty()) {
			Entity rider = mob.getPassengers().get(0);
			TFEventListener.allowDismount = true;
			rider.stopRiding();
			TFEventListener.allowDismount = false;

			Vec3 throwVec = mob.getLookAngle().scale(2);
			throwVec = new Vec3(throwVec.x, 0.9, throwVec.z);

			rider.push(throwVec.x, throwVec.y, throwVec.z);

			if (rider instanceof ServerPlayer player) {

				ThrowPlayerPacket message = new ThrowPlayerPacket((float) throwVec.x, (float) throwVec.y, (float) throwVec.z);
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
			}
		}
		super.stop();
	}

	@Override
	public boolean canContinueToUse() {
		return (throwTimer > 0 && !mob.getPassengers().isEmpty()) || (timeout > 0 && super.canContinueToUse() && mob.getPassengers().isEmpty());
	}

}
