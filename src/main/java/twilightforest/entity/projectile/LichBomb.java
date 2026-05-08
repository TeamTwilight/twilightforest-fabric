package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import twilightforest.init.TFEntities;
import twilightforest.init.TFDamageTypes;

public class LichBomb extends ThrowableProjectile implements ItemSupplier {
    public LichBomb(EntityType<? extends LichBomb> type, Level level) {
        super(type, level);
    }

    public LichBomb(Level level, LivingEntity owner) {
        super(TFEntities.LICH_BOMB.get(), owner, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        this.makeTrail();
        if (!this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.029D, 0.0D));
        }
    }

    private void makeTrail() {
        for (int i = 0; i < 1; i++) {
            double sx = 0.5D * (this.random.nextDouble() - this.random.nextDouble()) + this.getDeltaMovement().x();
            double sy = 0.5D * (this.random.nextDouble() - this.random.nextDouble()) + this.getDeltaMovement().y();
            double sz = 0.5D * (this.random.nextDouble() - this.random.nextDouble()) + this.getDeltaMovement().z();
            this.level().addParticle(ParticleTypes.FLAME, this.getX() + sx, this.getY() + sy, this.getZ() + sz, sx * -0.25D, sy * -0.25D, sz * -0.25D);
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 1.0F;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        if (source.getDirectEntity() != null) {
            if (!source.is(DamageTypeTags.IS_EXPLOSION)) {
                this.explode();
            }
            return true;
        }
        return false;
    }

    private void explode() {
        if (!this.level().isClientSide()) {
            this.level().explode(this, TFDamageTypes.indirectSource(this.level(), TFDamageTypes.LICH_BOMB, this, this.getOwner()), null, this.getX(), this.getY(), this.getZ(), 2.0F, false, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.explode();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LichBolt || result.getEntity() instanceof LichBomb) {
            return;
        }
        this.explode();
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.MAGMA_CREAM);
    }
}