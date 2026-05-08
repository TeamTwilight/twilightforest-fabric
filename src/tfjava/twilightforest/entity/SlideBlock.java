package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;

import java.util.List;

public class SlideBlock extends Entity {

    private static final int WARMUP_TIME = 20;
    private static final EntityDataAccessor<Direction> MOVE_DIRECTION = SynchedEntityData.defineId(SlideBlock.class, EntityDataSerializers.DIRECTION);

    private BlockState myState;
    private int slideTime;

    public SlideBlock(EntityType<? extends SlideBlock> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
        this.myState = TFBlocks.SLIDER.get().defaultBlockState();
    }

    public SlideBlock(EntityType<? extends SlideBlock> type, Level level, double x, double y, double z, BlockState state) {
        super(type, level);
        this.myState = state;
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.determineMoveDirection();
    }

    private void determineMoveDirection() {
        BlockPos pos = this.blockPosition();

        Direction[] directions = switch (this.myState.getValue(RotatedPillarBlock.AXIS)) {
            case X -> new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
            case Z -> new Direction[]{Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST};
            case Y -> new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
        };

        for (Direction direction : directions) {
            if (this.level().isEmptyBlock(pos.relative(direction)) && !this.level().isEmptyBlock(pos.relative(direction.getOpposite()))) {
                this.getEntityData().set(MOVE_DIRECTION, direction);
                return;
            }
        }

        for (Direction direction : directions) {
            if (this.level().isEmptyBlock(pos.relative(direction))) {
                this.getEntityData().set(MOVE_DIRECTION, direction);
                return;
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MOVE_DIRECTION, Direction.DOWN);
    }

    @Override
    public boolean isSteppingCarefully() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    @Override
    public void tick() {
        if (this.myState == null || this.myState.isAir()) {
            this.discard();
            return;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        ++this.slideTime;

        if (this.slideTime > WARMUP_TIME) {
            Direction moveDirection = this.getEntityData().get(MOVE_DIRECTION);
            double acceleration = 0.04D;
            this.setDeltaMovement(this.getDeltaMovement().add(
                    moveDirection.getStepX() * acceleration,
                    moveDirection.getStepY() * acceleration,
                    moveDirection.getStepZ() * acceleration));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(0.98D, 0.98D, 0.98D));

        if (!this.level().isClientSide()) {
            if (this.slideTime % 5 == 0) {
                this.playSound(TFSounds.SLIDER, 1.0F, 0.9F + this.random.nextFloat() * 0.4F);
            }

            BlockPos pos = this.blockPosition();

            if (this.slideTime == 1) {
                if (this.level().getBlockState(pos) != this.myState) {
                    this.discard();
                    return;
                }

                this.level().removeBlock(pos, false);
            }

            if (this.slideTime == WARMUP_TIME + 40) {
                this.setDeltaMovement(Vec3.ZERO);
                this.getEntityData().set(MOVE_DIRECTION, this.getEntityData().get(MOVE_DIRECTION).getOpposite());
            }

            if (this.verticalCollision || this.horizontalCollision) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, 0.7D, 0.7D));
                this.discard();

                if (this.level().isUnobstructed(this.myState, pos, CollisionContext.empty())) {
                    this.level().setBlockAndUpdate(pos, this.myState);
                } else {
                    this.spawnAtLocation(new ItemStack(this.myState.getBlock()), 0.0F);
                }
            } else if ((this.slideTime > 100 && (pos.getY() < this.level().getMinBuildHeight() + 1 || pos.getY() > this.level().getMaxBuildHeight())) || this.slideTime > 600) {
                this.spawnAtLocation(new ItemStack(this.myState.getBlock()), 0.0F);
                this.discard();
            }

            this.damageKnockbackEntities(this.level().getEntities(this, this.getBoundingBox()));
        }
    }

    private void damageKnockbackEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                living.hurt(TFDamageTypes.source(this.level(), TFDamageTypes.SLIDER), 5.0F);

                double xKnockback = (this.getX() - entity.getX()) * 2.0D;
                double zKnockback = (this.getZ() - entity.getZ()) * 2.0D;

                living.knockback(2.0D, xKnockback, zKnockback);
            }
        }
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.slideTime = compound.getInt("Time");
        this.getEntityData().set(MOVE_DIRECTION, Direction.from3DDataValue(compound.getByte("Direction")));
        this.myState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("BlockState"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Time", this.slideTime);
        compound.putByte("Direction", (byte) this.getEntityData().get(MOVE_DIRECTION).get3DDataValue());
        compound.put("BlockState", NbtUtils.writeBlockState(this.myState));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    public BlockState getBlockState() {
        return this.myState;
    }
}
