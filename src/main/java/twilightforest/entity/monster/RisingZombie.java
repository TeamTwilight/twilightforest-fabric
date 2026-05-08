package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RisingZombie extends Monster {
    private static final EntityDataAccessor<Integer> RISING_TICKS = SynchedEntityData.defineId(RisingZombie.class, EntityDataSerializers.INT);

    public RisingZombie(EntityType<? extends RisingZombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RISING_TICKS, 0);
    }

    public int getRisingTicks() {
        return this.getEntityData().get(RISING_TICKS);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (RISING_TICKS.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).scale(1.0F, this.getRisingTicks() / 130.0F);
    }

    @Override
    public boolean isInvisible() {
        return this.getRisingTicks() == 0;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getRisingTicks() > 0 && !this.isDeadOrDying()) {
            BlockPos pos = this.blockPosition().below();
            BlockState state = this.level().getBlockState(pos);
            if (!this.level().isClientSide()) {
                this.getEntityData().set(RISING_TICKS, this.getRisingTicks() + 1);
                if (this.getRisingTicks() % 10 == 0 && this.getRisingTicks() < 130) {
                    this.level().playSound(null, this.blockPosition(), state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, this.getRandom().nextFloat() * 0.15F + 0.7F);
                }
            } else if (!this.level().isEmptyBlock(this.blockPosition().below())) {
                double particleX = this.getX() + this.getRandom().nextDouble() - 0.5D;
                double particleY = this.getY() + this.getRandom().nextDouble() * 0.2D + 0.075D;
                double particleZ = this.getZ() + this.getRandom().nextDouble() - 0.5D;
                for (int i = 0, amount = this.getRandom().nextInt(10) + 5; i < amount; i++) {
                    double offsetX = this.getRandom().nextDouble() * 0.1D - 0.05D;
                    double offsetZ = this.getRandom().nextDouble() * 0.1D - 0.05D;
                    double motionX = this.getRandom().nextDouble() * 0.2D - 0.1D;
                    double motionY = this.getRandom().nextDouble() * 0.25D + 0.1D;
                    double motionZ = this.getRandom().nextDouble() * 0.2D - 0.1D;
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), particleX + offsetX, particleY, particleZ + offsetZ, motionX, motionY, motionZ);
                }
            }
        } else if (this.tickCount % 10 == 0) {
            Player player = this.level().getNearestPlayer(this, this.getAttributeValue(Attributes.FOLLOW_RANGE) / 2.0D);
            if (player != null && this.isLookingInMyDirection(player, 0.5D, false, true, this.getEyeY(), this.getY() + 0.5D * this.getScale(), (this.getEyeY() + this.getY()) / 2.0D)) {
                this.getEntityData().set(RISING_TICKS, 1);
            }
        }

        if (!this.level().isClientSide() && this.getRisingTicks() >= 130) {
            Zombie zombie = this.convertTo(EntityType.ZOMBIE, true);
            if (zombie != null) {
                zombie.setHealth(this.getHealth());
                zombie.setYRot(this.yRotO = this.getYRot());
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.isInvulnerableTo(source);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    public boolean isLookingInMyDirection(Player player, double width, boolean useLength, boolean checkAir, double... offsets) {
        Vec3 look = player.getViewVector(1.0F).normalize();
        for (double yOffset : offsets) {
            Vec3 direction = new Vec3(this.getX() - player.getX(), yOffset - player.getEyeY(), this.getZ() - player.getZ());
            double distance = direction.length();
            direction = direction.normalize();
            double dot = look.dot(direction);
            if (dot > 1.0D - width / (useLength ? distance : 1.0D) && this.hasLineOfSight(player, checkAir ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, yOffset)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLineOfSight(Player player, ClipContext.Block blockClip, ClipContext.Fluid fluidClip, double yOffset) {
        if (player.level() != this.level()) {
            return false;
        }
        Vec3 from = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        Vec3 to = new Vec3(player.getX(), yOffset, player.getZ());
        return !(to.distanceTo(from) > 128.0D) && this.level().clip(new ClipContext(from, to, blockClip, fluidClip, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canUsePortal(boolean force) {
        return false;
    }
}