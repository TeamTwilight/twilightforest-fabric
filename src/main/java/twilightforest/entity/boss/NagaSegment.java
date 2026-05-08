package twilightforest.entity.boss;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.TFPart;
import twilightforest.init.TFSounds;

import java.util.List;

public class NagaSegment extends TFPart<Naga> {
    public static final ResourceLocation RENDERER = twilightforest.TwilightForestMod.prefix("naga_segment");

    private final Naga parent;
    private boolean active;
    private int deathCounter;

    public NagaSegment(Naga parent) {
        super(parent);
        this.parent = parent;
        this.setSize(EntityDimensions.fixed(2.0F, 2.0F));
        this.setPos(parent.getX(), parent.getY(), parent.getZ());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.active) {
            this.collideWithOthers();
        }
        if (this.deathCounter > 0 && --this.deathCounter <= 0) {
            this.spawnDestructionParticles();
            this.parent.playSound(TFSounds.NAGA_HURT, 0.25F, this.parent.getVoicePitch() * 0.75F + 0.5F * this.parent.getRandom().nextFloat());
            this.parent.deathTime = 0;
            this.deactivate();
        }
    }

    private void collideWithOthers() {
        AABB bounds = this.getBoundingBox();
        List<Entity> entities = this.parent.level().getEntities(this.parent, bounds, entity -> entity.isPushable() && !this.is(entity));
        for (Entity entity : entities) {
            entity.push(this.parent);
            if (entity instanceof LivingEntity && !(entity instanceof Naga) && !this.parent.isDazed() && !this.parent.isDeadOrDying()) {
                int attackStrength = entity instanceof Animal ? 6 : 2;
                entity.hurt(entity.level().damageSources().mobAttack(this.parent), attackStrength);
            }
        }
    }

    private void spawnDestructionParticles() {
        for (int k = 0; k < 20; k++) {
            this.parent.level().addParticle(this.parent.getRandom().nextBoolean() ? ParticleTypes.EXPLOSION : ParticleTypes.EXPLOSION_EMITTER,
                    this.position().x() + (this.parent.getRandom().nextFloat() - 0.5F) * 4.0F,
                    this.position().y() + this.parent.getRandom().nextFloat() * 2.0F,
                    this.position().z() + (this.parent.getRandom().nextFloat() - 0.5F) * 4.0F,
                    this.parent.getRandom().nextGaussian() * 0.02D,
                    this.parent.getRandom().nextGaussian() * 0.02D,
                    this.parent.getRandom().nextGaussian() * 0.02D);
        }
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float damage) {
        return this.active && this.parent.hurt(source, damage * 2.0F / 3.0F);
    }

    @Override
    public boolean is(Entity entity) {
        return super.is(entity);
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isInvisible() {
        return !this.active || super.isInvisible();
    }

    @Override
    public ResourceLocation renderer() {
        return RENDERER;
    }

    public void selfDestruct(int counter) {
        this.deathCounter = counter;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
    }

    @Override
    public void setRot(float yRot, float xRot) {
        super.setRot(yRot, xRot);
    }

    public boolean isInWall() {
        return !this.parent.level().noCollision(this.getBoundingBox().deflate(0.05D));
    }

    public void follow(Entity leader, int index) {
        this.follow(leader.position(), leader.getYRot(), index);
    }

    public void follow(NagaSegment leader, int index) {
        this.follow(leader.position(), leader.getYRot(), index);
    }

    private void follow(Vec3 leaderPosition, float leaderYRot, int index) {
        float angle = ((leaderYRot + 180.0F) * Mth.PI) / 180.0F;
        double straightenForce = this.parent.isDeadOrDying() ? 0.0D : 0.05D + (1.0D / (index + 1)) * 0.5D;
        double idealX = -Mth.sin(angle) * straightenForce;
        double idealZ = Mth.cos(angle) * straightenForce;
        double groundY = this.isInWall() ? leaderPosition.y() + 2.0D : leaderPosition.y();
        double idealY = (groundY - leaderPosition.y()) * straightenForce;
        Vec3 diff = this.position().subtract(leaderPosition).normalize().add(idealX, idealY, idealZ).normalize();
        if (diff.lengthSqr() < 1.0E-7D) {
            diff = new Vec3(-Mth.sin(angle), 0.0D, Mth.cos(angle));
        }
        double distance = 2.0D;
        this.setPos(leaderPosition.x() + distance * diff.x(), leaderPosition.y() + distance * diff.y(), leaderPosition.z() + distance * diff.z());
        double horizontal = Mth.sqrt((float) (diff.x() * diff.x() + diff.z() * diff.z()));
        this.setRot((float) (Math.atan2(diff.z(), diff.x()) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y(), horizontal) * 180.0D / Math.PI));
    }
}
