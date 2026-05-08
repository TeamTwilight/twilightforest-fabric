package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class CarminiteGhastguard extends Ghast {
    private static final EntityDataAccessor<Byte> ATTACK_STATUS = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ATTACK_TIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ATTACK_PREV_TIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

    protected float wanderFactor = 16.0F;
    private int attackTimer;
    private int previousAttackTimer;
    private int inTrapCounter;
    private twilightforest.entity.ai.goal.GhastguardAttackGoal attackGoal;

    public CarminiteGhastguard(EntityType<? extends CarminiteGhastguard> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Ghast.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_STATUS, (byte) 0);
        builder.define(ATTACK_TIMER, (byte) 0);
        builder.define(ATTACK_PREV_TIMER, (byte) 0);
        builder.define(HOME_POINT, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new twilightforest.entity.ai.goal.GhastguardHomedFlightGoal(this));
        this.goalSelector.addGoal(5, new twilightforest.entity.ai.goal.GhastguardRandomFlyGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 64.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, this.attackGoal = new twilightforest.entity.ai.goal.GhastguardAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected int getDisplayModel() {
        return TFItemVisuals.carminiteGhastguardDisplay(this.getAttackStatus(), this.isCharging() || this.isDeadOrDying());
    }

    protected float getDisplayScale() {
        return 2.4F;
    }

    protected float getDisplayYOffset() {
        return 1.2F;
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide() && this.getRandom().nextBoolean()) {
            this.level().addParticle(DustParticleOptions.REDSTONE, this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), 0.0D, 0.0D, 0.0D);
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.inTrapCounter > 0) {
            --this.inTrapCounter;
            this.setTarget(null);
        }
        this.attackTimer = this.attackGoal == null ? 0 : this.attackGoal.attackTimer;
        this.previousAttackTimer = this.attackGoal == null ? 0 : this.attackGoal.previousAttackTimer;
        this.getEntityData().set(ATTACK_STATUS, (byte) (this.attackTimer > 10 ? 2 : this.getTarget() != null && this.shouldAttack(this.getTarget()) ? 1 : 0));
        this.getEntityData().set(ATTACK_TIMER, (byte) this.attackTimer);
        this.getEntityData().set(ATTACK_PREV_TIMER, (byte) this.previousAttackTimer);
    }

    public void setInTrap() {
        this.inTrapCounter = 10;
    }

    public float getWanderFactor() {
        return this.wanderFactor;
    }

    public int getAttackStatus() {
        return this.getEntityData().get(ATTACK_STATUS);
    }

    public int getAttackTimer() {
        return this.getEntityData().get(ATTACK_TIMER);
    }

    public int getPrevAttackTimer() {
        return this.getEntityData().get(ATTACK_PREV_TIMER);
    }

    public boolean shouldAttack(LivingEntity living) {
        return true;
    }

    @Override
    public int getMaxHeadXRot() {
        return 500;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    public void spitFireball() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return;
        }
        Vec3 look = this.getViewVector(1.0F);
        double dx = target.getX() - (this.getX() + look.x() * 4.0D);
        double dy = target.getBoundingBox().minY + target.getBbHeight() / 2.0F - (0.5D + this.getY() + this.getBbHeight() / 2.0F);
        double dz = target.getZ() - (this.getZ() + look.z() * 4.0D);
        LargeFireball fireball = new LargeFireball(this.level(), this, new Vec3(dx, dy, dz), this.getExplosionPower());
        fireball.setPos(this.getX() + look.x() * 4.0D, this.getY() + this.getBbHeight() / 2.0F + 0.5D, this.getZ() + look.z() * 4.0D);
        this.level().addFreshEntity(fireball);
        this.playSound(this.getFireSound(), 1.0F, this.getVoicePitch());
        if (this.getRandom().nextInt(6) == 0) {
            this.setTarget(null);
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this) && !level.containsAnyLiquid(this.getBoundingBox());
    }

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    public SoundEvent getFireSound() {
        return TFSounds.CARMINITE_GHASTGUARD_SHOOT;
    }

    public SoundEvent getWarnSound() {
        return TFSounds.CARMINITE_GHASTGUARD_WARN;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.CARMINITE_GHASTGUARD_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.CARMINITE_GHASTGUARD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.CARMINITE_GHASTGUARD_DEATH;
    }

    public @Nullable GlobalPos getRestrictionPoint() {
        return this.getEntityData().get(HOME_POINT).orElse(null);
    }

    public void setRestrictionPoint(@Nullable GlobalPos pos) {
        this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
    }

    public int getHomeRadius() {
        return 64;
    }

    public boolean isRestrictionPointValid() {
        return this.getRestrictionPoint() != null && this.getRestrictionPoint().dimension().equals(this.level().dimension());
    }

    public boolean isRestrictionPointValid(net.minecraft.resources.ResourceKey<Level> dimension) {
        return this.getRestrictionPoint() != null && this.getRestrictionPoint().dimension().equals(dimension);
    }

    public boolean isMobWithinHomeArea(Entity entity) {
        if (!this.isRestrictionPointValid()) {
            return true;
        }
        BlockPos home = this.getRestrictionPoint().pos();
        return entity.blockPosition().getY() > this.level().getMinBuildHeight() + 64
                && entity.blockPosition().getY() < this.level().getMaxBuildHeight() - 64
                && home.distSqr(entity.blockPosition()) < (double) (this.getHomeRadius() * this.getHomeRadius());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        GlobalPos home = this.getRestrictionPoint();
        if (home != null) {
            tag.putInt("HomeX", home.pos().getX());
            tag.putInt("HomeY", home.pos().getY());
            tag.putInt("HomeZ", home.pos().getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("HomeX")) {
            this.setRestrictionPoint(GlobalPos.of(this.level().dimension(), new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ"))));
        }
    }

    static class GhastguardRandomFlyGoal extends Goal {
        private final CarminiteGhastguard ghast;

        GhastguardRandomFlyGoal(CarminiteGhastguard ghast) {
            this.ghast = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl control = this.ghast.getMoveControl();
            if (this.ghast.getTarget() != null) {
                return false;
            }
            if (!control.hasWanted()) {
                return true;
            }
            double dx = control.getWantedX() - this.ghast.getX();
            double dy = control.getWantedY() - this.ghast.getY();
            double dz = control.getWantedZ() - this.ghast.getZ();
            double distance = dx * dx + dy * dy + dz * dz;
            return distance < 1.0D || distance > 3600.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource random = this.ghast.getRandom();
            double x = this.ghast.getX() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
            double y = this.ghast.getY() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
            double z = this.ghast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
            this.ghast.getMoveControl().setWantedPosition(x, y, z, 1.0D);
        }
    }

    static class GhastguardHomedFlightGoal extends Goal {
        private final CarminiteGhastguard ghast;

        GhastguardHomedFlightGoal(CarminiteGhastguard ghast) {
            this.ghast = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.ghast.getTarget() != null || this.ghast.isMobWithinHomeArea(this.ghast)) {
                return false;
            }
            MoveControl control = this.ghast.getMoveControl();
            if (!control.hasWanted()) {
                return true;
            }
            double dx = control.getWantedX() - this.ghast.getX();
            double dy = control.getWantedY() - this.ghast.getY();
            double dz = control.getWantedZ() - this.ghast.getZ();
            double distance = dx * dx + dy * dy + dz * dz;
            return distance < 1.0D || distance > 3600.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (!this.ghast.isRestrictionPointValid()) {
                return;
            }
            BlockPos home = this.ghast.getRestrictionPoint().pos();
            Vec3 homeCenter = Vec3.atCenterOf(home);
            if (this.ghast.distanceToSqr(homeCenter) > 256.0D) {
                Vec3 toHome = homeCenter.subtract(this.ghast.position()).normalize();
                RandomSource random = this.ghast.getRandom();
                double x = this.ghast.getX() + toHome.x() * this.ghast.getWanderFactor() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
                double y = this.ghast.getY() + toHome.y() * this.ghast.getWanderFactor() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
                double z = this.ghast.getZ() + toHome.z() * this.ghast.getWanderFactor() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
                this.ghast.getMoveControl().setWantedPosition(x, y, z, 1.0D);
            } else {
                this.ghast.getMoveControl().setWantedPosition(home.getX() + 0.5D, home.getY(), home.getZ() + 0.5D, 1.0D);
            }
        }
    }

    static class GhastguardAttackGoal extends Goal {
        private final CarminiteGhastguard ghast;
        int attackTimer;
        int previousAttackTimer;

        GhastguardAttackGoal(CarminiteGhastguard ghast) {
            this.ghast = ghast;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.ghast.getTarget();
            return target != null && this.ghast.shouldAttack(target);
        }

        @Override
        public void start() {
            this.attackTimer = 0;
            this.previousAttackTimer = 0;
        }

        @Override
        public void stop() {
            this.ghast.setCharging(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.ghast.getTarget();
            if (target == null) {
                return;
            }
            if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.getSensing().hasLineOfSight(target)) {
                this.previousAttackTimer = this.attackTimer;
                ++this.attackTimer;
                this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());
                if (this.attackTimer == 10) {
                    this.ghast.playSound(this.ghast.getWarnSound(), 10.0F, this.ghast.getVoicePitch());
                }
                if (this.attackTimer == 20) {
                    if (this.ghast.shouldAttack(target)) {
                        this.ghast.playSound(this.ghast.getFireSound(), 10.0F, this.ghast.getVoicePitch());
                        this.ghast.spitFireball();
                        this.previousAttackTimer = this.attackTimer;
                    }
                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                this.previousAttackTimer = this.attackTimer;
                --this.attackTimer;
            }
            this.ghast.setCharging(this.attackTimer > 10);
        }
    }
}
