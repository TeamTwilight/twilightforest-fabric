package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class PinchBeetle extends Monster {
    private int chargeCooldown;

    public PinchBeetle(EntityType<? extends PinchBeetle> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.chargeCooldown > 0) {
            this.chargeCooldown--;
        }
        if (!this.getPassengers().isEmpty()) {
            Entity passenger = this.getPassengers().get(0);
            this.getLookControl().setLookAt(passenger, 100.0F, 100.0F);
            if (passenger instanceof LivingEntity living) {
                this.setTarget(living);
            }
            if (passenger instanceof Player player && player.getAbilities().invulnerable) {
                player.stopRiding();
                this.setTarget(null);
            }
        } else if (!this.level().isClientSide()) {
            LivingEntity target = this.getTarget();
            if (target != null && this.chargeCooldown <= 0 && this.distanceToSqr(target) > 9.0D && this.distanceToSqr(target) < 100.0D) {
                Vec3 direction = target.position().subtract(this.position()).normalize().scale(0.65D);
                this.setDeltaMovement(direction.x(), 0.15D, direction.z());
                this.hasImpulse = true;
                this.chargeCooldown = 60;
            }
        }
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        if (this.getPassengers().isEmpty()) {
            super.knockback(strength, xRatio, zRatio);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.getPassengers().isEmpty()) {
            entity.stopRiding();
            entity.startRiding(this, true);
            this.playSound(TFSounds.PINCH_BEETLE_ATTACK, 1.0F, 1.0F);
        }
        return entity.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.CLAMPED, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction callback) {
        if (!this.getPassengers().isEmpty()) {
            Vec3 pos = this.getRiderPosition();
            callback.accept(passenger, pos.x(), pos.y(), pos.z());
        }
    }

    public double getMyRidingOffset() {
        return -0.1D;
    }

    public double getPassengersRidingOffset() {
        return 0.75D;
    }

    private Vec3 getRiderPosition() {
        if (!this.getPassengers().isEmpty()) {
            double dx = Math.cos((this.getYRot() + 90.0F) * Math.PI / 180.0D) * 0.75D;
            double dz = Math.sin((this.getYRot() + 90.0F) * Math.PI / 180.0D) * 0.75D;
            return new Vec3(this.getX() + dx, this.getY() + this.getPassengersRidingOffset(), this.getZ() + dz);
        }
        return this.position();
    }

    public boolean canRiderInteract() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.PINCH_BEETLE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.PINCH_BEETLE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.PINCH_BEETLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.PINCH_BEETLE_STEP, 0.15F, 1.0F);
    }
}