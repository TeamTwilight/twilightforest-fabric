package twilightforest.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFEntities;
import twilightforest.init.TFDamageTypes;

public class LichBolt extends ThrowableProjectile {
    public LichBolt(EntityType<? extends LichBolt> type, Level level) {
        super(type, level);
    }

    public LichBolt(Level level, LivingEntity owner) {
        super(TFEntities.LICH_BOLT.get(), owner, level);
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
        for (int i = 0; i < 5; i++) {
            double dx = this.getX() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            double dy = this.getY() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            double dz = this.getZ() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            this.level().addParticle(ParticleTypes.WITCH, dx, dy, dz, 0.0D, 0.0D, 0.0D);
        }
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
        if (!this.level().isClientSide() && source.getEntity() != null) {
            Vec3 look = source.getEntity().getLookAngle();
            this.shoot(look.x(), look.y(), look.z(), 1.5F, 0.1F);
            if (source.getDirectEntity() instanceof LivingEntity) {
                this.setOwner(source.getDirectEntity());
            }
            return true;
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ItemStack item = new ItemStack(Items.ENDER_PEARL);
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, item), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.level().broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hit = result.getEntity();
        if (hit instanceof LichBolt || hit instanceof LichBomb) {
            return;
        }
        if (!this.level().isClientSide()) {
            if (hit instanceof LivingEntity) {
                hit.hurt(TFDamageTypes.indirectSource(this.level(), TFDamageTypes.LICH_BOLT, this, this.getOwner()), 6.0F);
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}