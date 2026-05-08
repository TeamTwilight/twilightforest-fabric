package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

import java.util.List;

public class FallingIce extends Entity {
    private static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingIce.class, EntityDataSerializers.BLOCK_POS);
    private int hangTime = 100;
    private int time;
    private BlockState blockState = Blocks.PACKED_ICE.defaultBlockState();

    public BlockState getBlockState() {
        return this.blockState;
    }

    public FallingIce(EntityType<? extends FallingIce> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public FallingIce(Level level, double x, double y, double z, BlockState state, int hangTime) {
        this(TFEntities.FALLING_ICE.get(), level);
        this.hangTime = hangTime;
        this.blockState = state.isAir() ? Blocks.PACKED_ICE.defaultBlockState() : state;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_START_POS, BlockPos.ZERO);
    }

    public void setStartPos(BlockPos pos) {
        this.getEntityData().set(DATA_START_POS, pos);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
            return;
        }
        ++this.time;
        this.setNoGravity(this.time < this.hangTime);
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        } else if (this.level().isClientSide()) {
            this.makeWarningTrail();
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (!this.level().isClientSide()) {
            if (this.onGround()) {
                this.shatterAndDamage();
                this.discard();
            } else if (this.time > 1000 || this.getY() < this.level().getMinBuildHeight() - 4) {
                this.discard();
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
    }

    private void makeWarningTrail() {
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, this.blockState);
        for (int i = 0; i < 10; i++) {
            double x = this.getX() + 1.5F * (this.random.nextFloat() - this.random.nextFloat());
            double y = this.getY() - 4.0F * (this.random.nextFloat() - this.random.nextFloat()) - 3.0F;
            double z = this.getZ() + 1.5F * (this.random.nextFloat() - this.random.nextFloat());
            this.level().addAlwaysVisibleParticle(particle, x, y, z, 0.0D, -1.0D, 0.0D);
        }
    }

    private void shatterAndDamage() {
        int fallDistance = Mth.ceil(this.fallDistance - 5.0F);
        if (fallDistance >= 0) {
            float[] damagePerDifficulty = {0.0F, 0.5F, 1.0F, 2.0F};
            float damage = Math.min(Mth.floor(fallDistance * damagePerDifficulty[this.level().getDifficulty().getId()]), 100.0F);
            for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D), EntitySelector.NO_SPECTATORS)) {
                if (!(entity instanceof AlphaYeti)) {
                    entity.hurt(TFDamageTypes.source(this.level(), TFDamageTypes.FALLING_ICE), damage);
                }
            }
        }
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, this.blockState);
        for (int i = 0; i < 80; i++) {
            double x = this.getX() + 3.0F * (this.random.nextFloat() - this.random.nextFloat());
            double y = this.getY() + 3.0F * (this.random.nextFloat() - this.random.nextFloat());
            double z = this.getZ() + 3.0F * (this.random.nextFloat() - this.random.nextFloat());
            this.level().addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        this.playSound(Blocks.PACKED_ICE.defaultBlockState().getSoundType().getBreakSound(), 3.0F, 0.5F);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("HangTime", this.hangTime);
        tag.putInt("Time", this.time);
        tag.put("BlockState", NbtUtils.writeBlockState(this.blockState));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.hangTime = tag.getInt("HangTime");
        this.time = tag.getInt("Time");
        this.blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("BlockState"));
    }
}