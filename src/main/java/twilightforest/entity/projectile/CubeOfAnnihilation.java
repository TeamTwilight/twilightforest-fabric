package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

import java.util.List;

public class CubeOfAnnihilation extends ThrowableProjectile {
    private boolean hasHitObstacle = false;
    private ItemStack stack = ItemStack.EMPTY;

    public CubeOfAnnihilation(EntityType<? extends CubeOfAnnihilation> type, Level level) {
        super(type, level);
    }

    public CubeOfAnnihilation(EntityType<? extends CubeOfAnnihilation> type, Level level, LivingEntity owner, ItemStack stack) {
        super(type, owner, level);
        this.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0.0F, 1.5F, 1.0F);
        this.stack = stack;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean canUsePortal(boolean force) {
        return false;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0F;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity && result.getEntity().hurt(this.getDamageSource(), 10.0F)) {
            this.tickCount += 60;
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isEmptyBlock(result.getBlockPos())) {
            this.affectBlocksInAABB(this.getBoundingBox().inflate(0.2F, 0.2F, 0.2F));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) result);
        } else if (result.getType() == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
    }

    private DamageSource getDamageSource() {
        LivingEntity thrower = this.getOwner() instanceof LivingEntity living ? living : null;
        if (thrower instanceof Player player) {
            return this.damageSources().playerAttack(player);
        }
        if (thrower != null) {
            return this.damageSources().mobAttack(thrower);
        }
        return this.damageSources().thrown(this, null);
    }

    private void affectBlocksInAABB(AABB box) {
        int minX = Mth.floor(box.minX);
        int minY = Mth.floor(box.minY);
        int minZ = Mth.floor(box.minZ);
        int maxX = Mth.floor(box.maxX);
        int maxY = Mth.floor(box.maxY);
        int maxZ = Mth.floor(box.maxZ);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    this.affectBlock(new BlockPos(x, y, z));
                }
            }
        }
    }

    private void affectBlock(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        if (state.isAir()) {
            return;
        }
        if (this.canAnnihilate(pos, state)) {
            this.level().removeBlock(pos, false);
            this.level().playSound(null, pos, TFSounds.CUBE_OF_ANNIHILATION_USE, SoundSource.BLOCKS, 0.125F, this.random.nextFloat() * 0.25F + 0.75F);
            this.annihilateParticles(pos);
        } else {
            this.hasHitObstacle = true;
        }
    }

    private boolean canAnnihilate(BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        return !state.hasBlockEntity()
                && !state.is(BlockTags.WITHER_IMMUNE)
                && !state.is(BlockTags.DRAGON_IMMUNE)
                && block.getExplosionResistance() < 8.0F
                && state.getDestroySpeed(this.level(), pos) >= 0.0F;
    }

    private void annihilateParticles(BlockPos pos) {
        for (int dx = 0; dx < 3; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                for (int dz = 0; dz < 3; dz++) {
                    this.level().addParticle(TFParticleType.ANNIHILATE,
                            pos.getX() + (dx + 0.5D) / 4.0D,
                            pos.getY() + (dy + 0.5D) / 4.0D,
                            pos.getZ() + (dz + 0.5D) / 4.0D,
                            this.random.nextGaussian() * 0.2D,
                            this.random.nextGaussian() * 0.2D,
                            this.random.nextGaussian() * 0.2D);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getOwner() == null) {
                this.discard();
                return;
            }
            Vec3 destination = new Vec3(this.getOwner().getX(), this.getOwner().getY() + this.getOwner().getEyeHeight(), this.getOwner().getZ());
            double distToOwner = this.distanceTo(this.getOwner());
            if (this.isReturning()) {
                if (distToOwner < 2.0F) {
                    this.discard();
                }
            } else {
                destination = destination.add(this.getOwner().getLookAngle().scale(16.0F));
            }
            Vec3 velocity = new Vec3(this.getX() - destination.x(), (this.getY() + this.getBbHeight() / 2.0F) - destination.y(), this.getZ() - destination.z());
            this.setDeltaMovement(-velocity.x(), -velocity.y(), -velocity.z());
            float currentSpeed = Mth.sqrt((float) (this.getDeltaMovement().x() * this.getDeltaMovement().x() + this.getDeltaMovement().y() * this.getDeltaMovement().y() + this.getDeltaMovement().z() * this.getDeltaMovement().z()));
            float maxSpeed = 0.5F;
            if (currentSpeed > maxSpeed) {
                this.setDeltaMovement(new Vec3(
                        this.getDeltaMovement().x() / (currentSpeed / maxSpeed),
                        this.getDeltaMovement().y() / (currentSpeed / maxSpeed),
                        this.getDeltaMovement().z() / (currentSpeed / maxSpeed)));
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5F));
            }
            this.affectBlocksInAABB(this.getBoundingBox().inflate(0.2F));
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() instanceof LivingEntity thrower && thrower.getUseItem().is(TFItems.CUBE_OF_ANNIHILATION.get())) {
            thrower.stopUsingItem();
        }
    }

    private boolean isReturning() {
        if (this.hasHitObstacle || this.getOwner() == null || !(this.getOwner() instanceof Player player)) {
            return true;
        }
        return !player.isUsingItem();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("CubeOfAnnihilationStack", 10)) {
            this.stack = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("CubeOfAnnihilationStack"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("CubeOfAnnihilationStack", this.stack.save(this.registryAccess()));
    }
}
