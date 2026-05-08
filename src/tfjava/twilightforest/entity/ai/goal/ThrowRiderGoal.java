package twilightforest.entity.ai.goal;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import twilightforest.components.entity.YetiThrowAttachment;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFDataAttachments;
import twilightforest.network.MovePlayerPacket;

public class ThrowRiderGoal extends MeleeAttackGoal {
	private static final TagKey<EntityType<?>> COMMON_BOSSES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", "bosses"));

	private int throwTimer;
	private int timeout;
	private int cooldown;

	public ThrowRiderGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
	}

	@Override
	public boolean canUse() {
		return this.mob.getPassengers().isEmpty()
				&& this.mob.getTarget() != null
				&& !this.mob.getTarget().getType().is(EntityTagGenerator.BOSSES)
				&& !this.mob.getTarget().getType().is(COMMON_BOSSES)
				&& TFDataAttachments.get(this.mob.getTarget(), TFDataAttachments.YETI_THROWING).getThrowCooldown() <= 0
				&& super.canUse();
	}

	@Override
	public void start() {
		this.throwTimer = 10 + this.mob.getRandom().nextInt(30);
		this.timeout = 80 + this.mob.getRandom().nextInt(40);
		super.start();
	}

	@Override
	public void tick() {
		this.timeout--;
		if (!this.mob.getPassengers().isEmpty()) {
			this.throwTimer--;
		} else {
			super.tick();
		}
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity victim) {
		if (this.canPerformAttack(victim) && this.getTicksUntilNextAttack() <= 0 && this.mob.getPassengers().isEmpty() && this.cooldown-- == 0) {
			this.cooldown = 3;
			this.resetAttackCooldown();
			this.mob.swing(InteractionHand.MAIN_HAND);
			if (this.mob.getPassengers().isEmpty()) {
				Entity vehicle = victim.getVehicle();

				if (vehicle == null || !vehicle.getType().is(EntityTagGenerator.RIDES_OBSTRUCT_SNATCHING)) {
					HostileMountEvents.hostileDismount(victim);
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

			Vec3 throwVec = new Vec3(this.mob.getLookAngle().x() * 2.0D, 0.9D, this.mob.getLookAngle().z() * 2.0D);

			if (rider instanceof Player player) {
				YetiThrowAttachment attachment = TFDataAttachments.get(player, TFDataAttachments.YETI_THROWING);
				attachment.setThrown(player, true, this.mob);
				attachment.setThrowVector(throwVec);
				attachment.setThrowCooldown(player, YetiThrowAttachment.THROW_COOLDOWN);
				player.push(throwVec.x(), throwVec.y(), throwVec.z());

				if (player instanceof ServerPlayer server && ServerPlayNetworking.canSend(server, MovePlayerPacket.TYPE)) {
					ServerPlayNetworking.send(server, new MovePlayerPacket(throwVec.x(), throwVec.y(), throwVec.z()));
				}
			} else {
				rider.push(throwVec.x(), throwVec.y(), throwVec.z());
			}
		}
		super.stop();
	}

	@Override
	public boolean canContinueToUse() {
		return (this.throwTimer > 0 && !this.mob.getPassengers().isEmpty())
				|| (this.timeout > 0 && super.canContinueToUse() && this.mob.getPassengers().isEmpty());
	}
}
