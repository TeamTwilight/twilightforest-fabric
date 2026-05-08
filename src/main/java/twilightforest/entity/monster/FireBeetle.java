package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
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

public class FireBeetle extends Monster {
    private static final EntityDataAccessor<Boolean> BREATHING = SynchedEntityData.defineId(FireBeetle.class, EntityDataSerializers.BOOLEAN);
    private int breathTicks;
    private int breathCooldown;

    public FireBeetle(EntityType<? extends FireBeetle> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BREATHING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.updateBreathAttack();
        }
        if (this.isBreathing()) {
            this.spawnBreathParticles();
            if (this.tickCount % 5 == 0) {
                this.playSound(TFSounds.FIRE_BEETLE_SHOOT, this.getRandom().nextFloat() * 0.5F, this.getRandom().nextFloat() * 0.5F + 0.5F);
            }
        }
    }

    private void updateBreathAttack() {
        if (this.breathCooldown > 0) {
            this.breathCooldown--;
        }
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            this.setBreathing(false);
            return;
        }
        if (this.breathTicks > 0) {
            this.breathTicks--;
            this.setBreathing(true);
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.distanceToSqr(target) < 25.0D && this.getSensing().hasLineOfSight(target) && this.tickCount % 5 == 0) {
                this.doBreathAttack(target);
            }
            if (this.breathTicks <= 0) {
                this.setBreathing(false);
                this.breathCooldown = 30;
            }
        } else if (this.breathCooldown <= 0 && this.distanceToSqr(target) < 25.0D && this.getSensing().hasLineOfSight(target)) {
            this.breathTicks = 10;
        }
    }

    private void spawnBreathParticles() {
        Vec3 look = this.getLookAngle();
        double px = this.getX() + look.x() * 0.9D;
        double py = this.getY() + 0.25D + look.y() * 0.9D;
        double pz = this.getZ() + look.z() * 0.9D;
        for (int i = 0; i < 2; i++) {
            double spread = 5.0D + this.getRandom().nextDouble() * 2.5D;
            double velocity = 0.15D + this.getRandom().nextDouble() * 0.15D;
            this.level().addParticle(ParticleTypes.FLAME, px, py, pz, (look.x() + this.getRandom().nextGaussian() * 0.0075D * spread) * velocity, (look.y() + this.getRandom().nextGaussian() * 0.0075D * spread) * velocity, (look.z() + this.getRandom().nextGaussian() * 0.0075D * spread) * velocity);
        }
    }

    public boolean isBreathing() {
        return this.getEntityData().get(BREATHING);
    }

    public void setBreathing(boolean breathing) {
        this.getEntityData().set(BREATHING, breathing);
    }

    public void doBreathAttack(Entity target) {
        if (!target.fireImmune() && target.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.SCORCHED, this), 2.0F)) {
            target.igniteForSeconds(10.0F);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.isBreathing()) {
            this.doBreathAttack(entity);
            return true;
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public int getMaxHeadXRot() {
        return 500;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.FIRE_BEETLE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.FIRE_BEETLE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.FIRE_BEETLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.FIRE_BEETLE_STEP, 0.15F, 1.0F);
    }
}