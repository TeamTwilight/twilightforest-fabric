package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.ai.goal.ChargeAttackGoal;
import twilightforest.init.TFDamageSources;
import twilightforest.init.TFSounds;

public class PinchBeetle extends Monster implements IHostileMount {

	public PinchBeetle(EntityType<? extends PinchBeetle> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new ChargeAttackGoal(this, 1.5F, false));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 2.0D);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.PINCH_BEETLE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.PINCH_BEETLE_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(TFSounds.PINCH_BEETLE_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	public void aiStep() {

		super.aiStep();
		this.dimensions = this.getDimensions(this.getPose());

		if (!this.getPassengers().isEmpty()) {
			this.getLookControl().setLookAt(this.getPassengers().get(0), 100.0F, 100.0F);
			//always set our passenger as our target
			if (this.getPassengers().get(0) instanceof LivingEntity entity) {
				this.setTarget(entity);
			}

			//if our held player switches gamemodes let them go
			if (this.getPassengers().get(0) instanceof Player player && player.getAbilities().invulnerable) {
				player.stopRiding();
				this.setTarget(null);
			}
		}
	}

	@Override
	public void knockback(double x, double y, double z) {
		//only take knockback if not holding something
		if(this.getPassengers().isEmpty()) {
			super.knockback(x, y, z);
		}
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (this.getPassengers().isEmpty()) {
			var v = entity.getVehicle();

			if (v == null || !v.getType().is(EntityTagGenerator.RIDES_OBSTRUCT_SNATCHING)) {
				// Pluck them from the boat, minecart, donkey, or whatever
				entity.stopRiding();

				entity.startRiding(this, true);
			}
		}
		entity.hurt(TFDamageSources.clamped(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		return super.doHurtTarget(entity);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return 0.25F;
	}

	@Override
	public void positionRider(Entity passenger) {
		if (!this.getPassengers().isEmpty()) {
			Vec3 riderPos = this.getRiderPosition();

			this.getPassengers().get(0).setPos(riderPos.x(), riderPos.y(), riderPos.z());
		}
	}

	@Override
	public double getMyRidingOffset() {
		return -0.1D;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0.75D;
	}

	private Vec3 getRiderPosition() {
		if (!this.getPassengers().isEmpty()) {
			float distance = 0.75F;

			double dx = Math.cos((this.getYRot() + 90) * Math.PI / 180.0D) * distance;
			double dz = Math.sin((this.getYRot() + 90) * Math.PI / 180.0D) * distance;

			return new Vec3(this.getX() + dx, this.getY() + this.getPassengersRidingOffset() + this.getPassengers().get(0).getMyRidingOffset(), this.getZ() + dz);
		} else {
			return new Vec3(this.getX(), this.getY(), this.getZ());
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {

		if (!this.getPassengers().isEmpty()) {
			return EntityDimensions.scalable(2.25F, 1.25F);
		} else {
			return super.getDimensions(pose);
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}
}
