package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

import java.util.List;

public class IceBomb extends ThrowableProjectile implements ItemSupplier {
    private int zoneTimer = 101;
    private boolean hasHit;

    public IceBomb(EntityType<? extends IceBomb> type, Level level) {
        super(type, level);
    }

    public IceBomb(Level level, LivingEntity owner) {
        super(TFEntities.THROWN_ICE.get(), owner, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasHit) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.1D));
            --this.zoneTimer;
            this.makeIceZone();
            if (!this.level().isClientSide() && this.zoneTimer <= 0) {
                this.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, this.blockPosition(), Block.getId(this.getBlockState()));
                this.discard();
            }
        } else {
            this.makeTrail(this.getOwner() instanceof Monster ? 2 : 5);
        }
    }

    private void makeTrail(int amount) {
        for (int i = 0; i < amount; i++) {
            double x = this.getX() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            double y = this.getY() + 0.5D * (this.random.nextDouble() - this.random.nextDouble()) + 0.5D;
            double z = this.getZ() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            this.level().addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    private void makeIceZone() {
        if (this.level().isClientSide()) {
            for (int i = 0; i < 16; i++) {
                double x = this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 3.5F;
                double y = this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 3.5F;
                double z = this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 3.5F;
                this.level().addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        } else {
            if (this.zoneTimer == 99) {
                this.doTerrainEffects(3);
            }
            if (this.zoneTimer % 20 == 0) {
                this.hitNearbyEntities();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.setDeltaMovement(0.0D, 0.0D, 0.0D);
        this.hasHit = true;
        this.doTerrainEffects(2);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide() && result.getEntity() instanceof LivingEntity living) {
            this.inflictDamage(living, 2);
        }
    }

    private void doTerrainEffects(int range) {
        if (this.level().isClientSide()) {
            return;
        }
        int centerX = Mth.floor(this.xOld);
        int centerY = Mth.floor(this.yOld);
        int centerZ = Mth.floor(this.zOld);
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (Math.abs(x) == range && Math.abs(z) == range) {
                    continue;
                }
                for (int y = -range; y <= range; y++) {
                    this.doTerrainEffect(new BlockPos(centerX + x, centerY + y, centerZ + z));
                }
            }
        }
    }

    private void doTerrainEffect(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        if (state.is(Blocks.WATER)) {
            this.level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
        } else if (state.is(Blocks.LAVA)) {
            this.level().setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
        } else if (this.level().isEmptyBlock(pos) && Blocks.SNOW.defaultBlockState().canSurvive(this.level(), pos)) {
            this.level().setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
        } else if (state.is(BlockTagGenerator.ICE_BOMB_REPLACEABLES)) {
            BlockState replacement = Blocks.SNOW.defaultBlockState().canSurvive(this.level(), pos) ? Blocks.SNOW.defaultBlockState() : Blocks.AIR.defaultBlockState();
            this.level().setBlock(pos, replacement, Block.UPDATE_ALL);
        } else if (state.is(Blocks.SNOW) && state.getValue(SnowLayerBlock.LAYERS) < 8) {
            this.level().setBlockAndUpdate(pos, state.setValue(SnowLayerBlock.LAYERS, state.getValue(SnowLayerBlock.LAYERS) + 1));
        }
    }

    private void hitNearbyEntities() {
        List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.0D, 2.0D, 3.0D));
        for (LivingEntity entity : nearby) {
            if (entity != this.getOwner()) {
                this.inflictDamage(entity, 1);
            }
        }
    }

    private void inflictDamage(LivingEntity entity, int multiplier) {
        if (!entity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
            float baseDamage = entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES) ? 5.0F : 1.0F;
            entity.hurt(TFDamageTypes.indirectSource(this.level(), TFDamageTypes.FROZEN, this, this.getOwner()), baseDamage * multiplier);
            entity.setTicksFrozen(Math.max(entity.getTicksFrozen(), 100 * multiplier));
        }
    }

    public BlockState getBlockState() {
        return Blocks.PACKED_ICE.defaultBlockState();
    }

    @Override
    protected double getDefaultGravity() {
        return this.hasHit ? 0.0D : 0.025D;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("ZoneTimer", this.zoneTimer);
        tag.putBoolean("HasHit", this.hasHit);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.zoneTimer = tag.getInt("ZoneTimer");
        this.hasHit = tag.getBoolean("HasHit");
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, this.getBlockState());
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return super.getOwner();
    }
}