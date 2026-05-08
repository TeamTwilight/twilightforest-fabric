package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFSounds;

public class CarminiteGhastling extends CarminiteGhastguard {
    private boolean minion;

    public CarminiteGhastling(EntityType<? extends CarminiteGhastling> type, Level level) {
        super(type, level);
        this.wanderFactor = 4.0F;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return CarminiteGhastguard.registerAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 16;
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.5F;
    }

    @Override
    public boolean shouldAttack(LivingEntity living) {
        ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);
        if (!helmet.isEmpty() && helmet.is(Items.CARVED_PUMPKIN)) {
            return false;
        }
        if (living.distanceTo(this) <= 3.5F) {
            return living.hasLineOfSight(this);
        }
        Vec3 view = living.getViewVector(1.0F).normalize();
        Vec3 toGhast = new Vec3(this.getX() - living.getX(), this.getBoundingBox().minY + this.getEyeHeight() - (living.getY() + living.getEyeHeight()), this.getZ() - living.getZ());
        double length = toGhast.length();
        toGhast = toGhast.normalize();
        return view.dot(toGhast) > 1.0D - 0.025D / length && living.hasLineOfSight(this);
    }

    public static boolean canSpawnHere(EntityType<CarminiteGhastling> entity, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && (reason == MobSpawnType.MOB_SUMMONED || Monster.isDarkEnoughToSpawn(level, pos, random)) && checkMobSpawnRules(entity, level, reason, pos, random);
    }

    public void makeBossMinion() {
        this.wanderFactor = 0.005F;
        this.minion = true;
        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6.0D);
            this.setHealth(this.getMaxHealth());
        }
    }

    public boolean isMinion() {
        return this.minion;
    }

    @Override
    protected int getDisplayModel() {
        return twilightforest.init.TFItemVisuals.carminiteGhastlingDisplay(this.getAttackStatus(), this.isCharging() || this.isDeadOrDying());
    }

    @Override
    protected float getDisplayScale() {
        return 0.7F;
    }

    @Override
    protected float getDisplayYOffset() {
        return 0.2F;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.CARMINITE_GHASTLING_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.CARMINITE_GHASTLING_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.CARMINITE_GHASTLING_DEATH;
    }

    @Override
    public SoundEvent getFireSound() {
        return TFSounds.CARMINITE_GHASTLING_SHOOT;
    }

    @Override
    public SoundEvent getWarnSound() {
        return TFSounds.CARMINITE_GHASTLING_WARN;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("isMinion", this.minion);
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean("isMinion")) {
            this.makeBossMinion();
        }
    }
}