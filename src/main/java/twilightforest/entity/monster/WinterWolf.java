package twilightforest.entity.monster;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFSounds;

public class WinterWolf extends HostileWolf {
    private static final EntityDataAccessor<Boolean> BREATH_FLAG = SynchedEntityData.defineId(WinterWolf.class, EntityDataSerializers.BOOLEAN);
    private static final float BREATH_DAMAGE = 2.0F;
    private int breathCooldown;
    private int breathTicks;

    public WinterWolf(EntityType<? extends WinterWolf> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return HostileWolf.registerAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected int getDisplayModel() {
        return twilightforest.init.TFItemVisuals.WINTER_WOLF_DISPLAY;
    }

    @Override
    protected float getDisplayScale() {
        return 1.18F;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BREATH_FLAG, false);
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
        } else if (this.isBreathing()) {
            this.spawnBreathParticles();
        }
        if (this.isBreathing() && this.tickCount % 6 == 0) {
            this.playSound(TFSounds.WINTER_WOLF_SHOOT, this.getRandom().nextFloat() * 0.5F, this.getRandom().nextFloat() * 0.5F + 0.5F);
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
        double distance = this.distanceToSqr(target);
        if (this.breathTicks > 0) {
            this.breathTicks--;
            this.setBreathing(true);
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (distance < 25.0D && this.getSensing().hasLineOfSight(target) && this.tickCount % 5 == 0) {
                this.doBreathAttack(target);
            }
            if (this.breathTicks <= 0) {
                this.setBreathing(false);
                this.breathCooldown = 70;
            }
        } else if (this.breathCooldown <= 0 && distance < 25.0D && this.getSensing().hasLineOfSight(target)) {
            this.breathTicks = 30;
        }
    }

    private void spawnBreathParticles() {
        Vec3 look = this.getLookAngle();
        double px = this.getX() + look.x() * 0.5D;
        double py = this.getY() + 1.25D + look.y() * 0.5D;
        double pz = this.getZ() + look.z() * 0.5D;
        for (int i = 0; i < 10; i++) {
            double spread = 5.0D + this.getRandom().nextDouble() * 2.5D;
            double velocity = 3.0D + this.getRandom().nextDouble() * 0.15D;
            double dx = (look.x() + this.getRandom().nextGaussian() * 0.0075D * spread) * velocity;
            double dy = (look.y() + this.getRandom().nextGaussian() * 0.0075D * spread) * velocity;
            double dz = (look.z() + this.getRandom().nextGaussian() * 0.0075D * spread) * velocity;
            this.level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, dx, dy, dz);
        }
    }

    public boolean isBreathing() {
        return this.getEntityData().get(BREATH_FLAG);
    }

    public void setBreathing(boolean breathing) {
        this.getEntityData().set(BREATH_FLAG, breathing);
    }

    public void doBreathAttack(Entity target) {
        target.hurt(this.damageSources().mobAttack(this), BREATH_DAMAGE);
    }

    @Override
    protected SoundEvent getTargetSound() {
        return TFSounds.WINTER_WOLF_TARGET;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.WINTER_WOLF_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.WINTER_WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.WINTER_WOLF_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.6F;
    }
}