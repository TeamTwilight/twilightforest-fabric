package twilightforest.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import twilightforest.init.TFDamageTypes;

public class IceSnowball extends ThrowableProjectile implements ItemSupplier {
    public IceSnowball(EntityType<? extends IceSnowball> type, Level level) {
        super(type, level);
    }

    public IceSnowball(EntityType<? extends IceSnowball> type, Level level, LivingEntity owner) {
        super(type, owner, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX() + 0.25D * (this.random.nextDouble() - this.random.nextDouble()), this.getY() + 0.25D * (this.random.nextDouble() - this.random.nextDouble()), this.getZ() + 0.25D * (this.random.nextDouble() - this.random.nextDouble()), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity living) {
            living.hurt(TFDamageTypes.indirectSource(this.level(), TFDamageTypes.SNOWBALL_FIGHT, this, this.getOwner()), 4.0F);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ItemStack item = new ItemStack(Items.SNOWBALL);
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, item), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }
}