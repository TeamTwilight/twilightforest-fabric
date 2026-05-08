package twilightforest.entity.projectile;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

public class TwilightWandBolt extends TFThrowable {

    public TwilightWandBolt(EntityType<? extends TwilightWandBolt> type, Level level) {
        super(type, level);
    }

    public TwilightWandBolt(Level level, LivingEntity thrower) {
        super(TFEntities.WAND_BOLT.get(), level, thrower);
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0F, 1.5F, 1.0F);
    }

    public TwilightWandBolt(Level level, double x, double y, double z) {
        super(TFEntities.WAND_BOLT.get(), level, x, y, z);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.003D;
    }

    @Override
    public void tick() {
        super.tick();
        this.makeTrail();
    }

    private void makeTrail() {
        for (int i = 0; i < 5; i++) {
            double dx = this.getX() + 0.25D * (this.random.nextDouble() - this.random.nextDouble());
            double dy = this.getY() + 0.25D * (this.random.nextDouble() - this.random.nextDouble());
            double dz = this.getZ() + 0.25D * (this.random.nextDouble() - this.random.nextDouble());
            float s1 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.17F;
            float s2 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.80F;
            float s3 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.69F;

            this.level().addParticle(ColorParticleOption.create(TFParticleType.MAGIC_EFFECT, s1, s2, s3), dx, dy, dz, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && !(target instanceof LichBolt) && !(target instanceof LichBomb);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.DEATH) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(TFParticleType.TWILIGHT_ORB, false, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide()) {
            Entity hit = result.getEntity();
            if (hit instanceof LivingEntity) {
                hit.hurt(TFDamageTypes.indirectSource(this.level(), TFDamageTypes.TWILIGHT_SCEPTER, this, this.getOwner()), 6.0F);
            }
            this.level().playSound(null, hit.blockPosition(), TFSounds.TWILIGHT_SCEPTER_HIT, this.getOwner() != null ? this.getOwner().getSoundSource() : SoundSource.PLAYERS, 1.0F, 1.0F);
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
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
}
