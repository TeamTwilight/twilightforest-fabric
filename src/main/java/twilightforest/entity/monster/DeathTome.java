package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.projectile.TomeBolt;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class DeathTome extends Monster implements RangedAttackMob {
    private boolean onLectern;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;

    public DeathTome(EntityType<? extends DeathTome> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 100, 5.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public void setOnLectern(boolean onLectern) {
        this.onLectern = onLectern;
    }

    public boolean isOnLectern() {
        return this.onLectern;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean isPersistenceRequired() {
        return super.isPersistenceRequired() || this.isOnLectern();
    }

    @Override
    public void aiStep() {
        if (this.isOnLectern()) {
            BlockState state = this.level().getBlockState(this.blockPosition());
            if (state.getBlock() instanceof LecternBlock) {
                Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
                this.yRotO = direction.toYRot();
                this.setYRot(this.yRotO);
                this.yBodyRot = this.yRotO;
                this.yBodyRotO = this.yRotO;
                this.yHeadRot = this.yRotO;
                this.yHeadRotO = this.yRotO;
                if (!this.level().isClientSide()) {
                    this.targetSelector.tick();
                }
                if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 20.0D) {
                    this.setOnLectern(false);
                    this.jumpControl.jump();
                    this.performRangedAttack(this.getTarget(), 1.0F);
                }
                return;
            }
            this.setOnLectern(false);
        }
        super.aiStep();
        Vec3 velocity = this.getDeltaMovement();
        if (!this.onGround() && velocity.y() < 0.0D) {
            this.setDeltaMovement(velocity.multiply(1.0D, 0.6D, 1.0D));
        }
    }

    @Override
    public void tick() {
        if (this.isOnLectern()) {
            this.ambientSoundTime = -1;
        }
        if (this.level().isClientSide) {
            float previousTarget = this.flipT;
            if (this.random.nextInt(this.isOnLectern() ? 120 : 30) == 0) {
                do {
                    this.flipT += (float) (this.random.nextInt(4) - this.random.nextInt(4));
                } while (previousTarget == this.flipT);
            }
            this.oFlip = this.flip;
            float delta = (this.flipT - this.flip) * 0.4F;
            delta = Mth.clamp(delta, -0.2F, 0.2F);
            this.flipA += (delta - this.flipA) * 0.9F;
            this.flip += this.flipA;
        }
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isOnLectern()) {
            this.jumpControl.jump();
            this.setOnLectern(false);
        }
        if (source.is(DamageTypeTags.IS_FIRE)) {
            amount *= 2.0F;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isOnLectern() ? null : TFSounds.DEATH_TOME_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.DEATH_TOME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.DEATH_TOME_DEATH;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ThrowableProjectile projectile = new TomeBolt(TFEntities.TOME_BOLT.get(), this.level(), this);
        double targetX = target.getX() - this.getX();
        double targetY = target.getY() + target.getEyeHeight() - 1.1D - projectile.getY();
        double targetZ = target.getZ() - this.getZ();
        double horizontal = Math.sqrt(targetX * targetX + targetZ * targetZ) * 0.2D;
        projectile.shoot(targetX, targetY + horizontal, targetZ, 0.6F, 6.0F);
        this.gameEvent(GameEvent.PROJECTILE_SHOOT);
        this.level().addFreshEntity(projectile);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.onLectern = tag.getBoolean("on_lectern");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("on_lectern", this.onLectern);
    }
}