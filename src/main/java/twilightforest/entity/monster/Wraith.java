package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.EnumSet;
import java.util.List;

public class Wraith extends FlyingMob implements Enemy, EnforcedHomePoint {
    @Nullable
    private GlobalPos homePoint;

    public Wraith(EntityType<? extends Wraith> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MoveTowardsHomeGoal(this, 0.85D));
        this.goalSelector.addGoal(4, new WraithAttackGoal(this));
        this.goalSelector.addGoal(5, new FlyTowardsTargetGoal(this));
        this.goalSelector.addGoal(6, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new LookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public boolean isSteppingCarefully() {
        return true;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount)) {
            Entity entity = source.getEntity();
            if (this.getVehicle() == entity || this.getPassengers().contains(entity)) {
                return true;
            }
            if (entity instanceof LivingEntity living && entity != this && !source.isCreativePlayer()) {
                this.setTarget(living);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = entity.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.HAUNT, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (hurt) {
            entity.push(this.getLookAngle().x() * 0.4D, 0.15D, this.getLookAngle().z() * 0.4D);
        }
        return hurt;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.WRAITH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.WRAITH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.WRAITH_DEATH;
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Wraith> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(level, pos, random) && checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
        if (spawnType == MobSpawnType.STRUCTURE || spawnType == MobSpawnType.SPAWNER) {
            this.setRestrictionPoint(GlobalPos.of(level.getLevel().dimension(), this.blockPosition()));
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.homePoint != null) {
            tag.putString("HomeDimension", this.homePoint.dimension().location().toString());
            tag.putLong("HomePos", this.homePoint.pos().asLong());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    public void setRestrictionPoint(GlobalPos pos) {
        this.homePoint = pos;
    }

    @Nullable
    public GlobalPos getRestrictionPoint() {
        return this.homePoint;
    }

    public int getHomeRadius() {
        return 20;
    }

    private boolean isMobWithinHomeArea() {
        if (this.homePoint == null || this.homePoint.dimension() != this.level().dimension()) {
            return true;
        }
        return this.homePoint.pos().distSqr(this.blockPosition()) < this.getHomeRadius() * this.getHomeRadius();
    }

    static class WraithAttackGoal extends Goal {
        private final Wraith wraith;
        private int attackTime;

        WraithAttackGoal(Wraith wraith) {
            this.wraith = wraith;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.wraith.getTarget() != null;
        }

        @Override
        public void tick() {
            LivingEntity target = this.wraith.getTarget();
            if (target == null) {
                return;
            }
            this.wraith.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.wraith.distanceToSqr(target) < 4.0D && this.attackTime <= 0) {
                this.wraith.doHurtTarget(target);
                this.attackTime = 20;
            }
            if (this.attackTime > 0) {
                this.attackTime--;
            }
        }
    }

    static class FlyTowardsTargetGoal extends Goal {
        private final Wraith wraith;

        FlyTowardsTargetGoal(Wraith wraith) {
            this.wraith = wraith;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.wraith.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            LivingEntity target = this.wraith.getTarget();
            if (target != null) {
                this.wraith.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.5D);
            }
        }
    }

    static class RandomFloatAroundGoal extends Goal {
        private final Wraith wraith;

        RandomFloatAroundGoal(Wraith wraith) {
            this.wraith = wraith;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.wraith.getTarget() != null || !this.wraith.isMobWithinHomeArea()) {
                return false;
            }
            MoveControl control = this.wraith.getMoveControl();
            double dx = control.getWantedX() - this.wraith.getX();
            double dy = control.getWantedY() - this.wraith.getY();
            double dz = control.getWantedZ() - this.wraith.getZ();
            double distance = dx * dx + dy * dy + dz * dz;
            return distance < 1.0D || distance > 3600.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource random = this.wraith.getRandom();
            double x = this.wraith.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double y = this.wraith.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double z = this.wraith.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            this.wraith.getMoveControl().setWantedPosition(x, y, z, 0.5D);
        }
    }

    public static class LookAroundGoal extends Goal {
        private final Wraith wraith;

        public LookAroundGoal(Wraith wraith) {
            this.wraith = wraith;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.wraith.getTarget();
            if (target == null) {
                this.wraith.setYRot(-((float) Mth.atan2(this.wraith.getDeltaMovement().x(), this.wraith.getDeltaMovement().z())) * (180.0F / (float) Math.PI));
            } else if (target.distanceToSqr(this.wraith) < 4096.0D) {
                double dx = target.getX() - this.wraith.getX();
                double dz = target.getZ() - this.wraith.getZ();
                this.wraith.setYRot(-((float) Mth.atan2(dx, dz)) * (180.0F / (float) Math.PI));
            }
            this.wraith.setYBodyRot(this.wraith.getYRot());
        }
    }

    public static class MoveTowardsHomeGoal extends Goal {
        private final Wraith mob;
        private final double speedModifier;
        private double wantedX;
        private double wantedY;
        private double wantedZ;

        public MoveTowardsHomeGoal(Wraith mob, double speedModifier) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.mob.homePoint == null || this.mob.isMobWithinHomeArea() || this.mob.getTarget() != null) {
                return false;
            }
            BlockPos pos = this.mob.homePoint.pos().relative(Direction.getRandom(this.mob.getRandom())).offset(this.mob.getRandom().nextInt(5), this.mob.getRandom().nextInt(5), this.mob.getRandom().nextInt(5));
            if (!this.mob.level().isLoaded(pos)) {
                return false;
            }
            this.wantedX = pos.getX();
            this.wantedY = pos.getY();
            this.wantedZ = pos.getZ();
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            this.mob.getMoveControl().setWantedPosition(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }
    }
}