package twilightforest.entity.monster;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class UpperGoblinKnight extends Monster {
    private static final int SHIELD_DAMAGE_THRESHOLD = 10;
    public static final int HEAVY_SPEAR_TIMER_START = 60;
    private static final EntityDataAccessor<Byte> DATA_EQUIP = SynchedEntityData.defineId(UpperGoblinKnight.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> SHIELD_DISABLED = SynchedEntityData.defineId(UpperGoblinKnight.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation ARMOR_ID = TwilightForestMod.prefix("upper_goblin_knight_armor");
    private static final ResourceLocation DAMAGE_ID = TwilightForestMod.prefix("upper_goblin_knight_heavy_spear");
    private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier(ARMOR_ID, 20.0D, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier DAMAGE_MODIFIER = new AttributeModifier(DAMAGE_ID, 12.0D, AttributeModifier.Operation.ADD_VALUE);

    private int shieldHits;
    private int shieldDisabledTicks;
    public int heavySpearTimer;

    public UpperGoblinKnight(EntityType<? extends UpperGoblinKnight> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR, 0.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_EQUIP, (byte) 3);
        builder.define(SHIELD_DISABLED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if ((this.level().isClientSide() || !this.isNoAi()) && this.heavySpearTimer > 0) {
            --this.heavySpearTimer;
        }
        if (this.isShieldDisabled()) {
            this.level().addParticle(ParticleTypes.SPLASH, this.getX(), this.getY() + this.getEyeHeight(), this.getZ(), (this.getRandom().nextFloat() - 0.5F) * 0.75F, 0.0D, (this.getRandom().nextFloat() - 0.5F) * 0.75F);
        }
        this.syncModifiers();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isShieldDisabled() && this.shieldDisabledTicks++ >= 100) {
            this.shieldDisabledTicks = 0;
            this.getEntityData().set(SHIELD_DISABLED, false);
        }
        if (this.isAlive()) {
            if (this.getVehicle() instanceof Monster mob && this.getTarget() == null) {
                this.setTarget(mob.getTarget());
            }
            if (!this.isPassenger() && this.hasShield()) {
                this.breakShield();
            }
        }
    }

    private void syncModifiers() {
        if (this.level().isClientSide()) {
            return;
        }
        if (this.getAttribute(Attributes.ARMOR) != null) {
            if (this.hasArmor()) {
                if (!this.getAttribute(Attributes.ARMOR).hasModifier(ARMOR_ID)) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(ARMOR_MODIFIER);
                }
            } else {
                this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_ID);
            }
        }
        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            if (this.heavySpearTimer > 0) {
                if (!this.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(DAMAGE_ID)) {
                    this.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(DAMAGE_MODIFIER);
                }
            } else {
                this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_ID);
            }
        }
    }

    public boolean hasArmor() {
        return (this.getEntityData().get(DATA_EQUIP) & 1) != 0;
    }

    private void setHasArmor(boolean armored) {
        byte flags = this.getEntityData().get(DATA_EQUIP);
        this.getEntityData().set(DATA_EQUIP, armored ? (byte) (flags | 1) : (byte) (flags & ~1));
        this.syncModifiers();
    }

    public boolean hasShield() {
        return (this.getEntityData().get(DATA_EQUIP) & 2) != 0;
    }

    private void setHasShield(boolean shield) {
        byte flags = this.getEntityData().get(DATA_EQUIP);
        this.getEntityData().set(DATA_EQUIP, shield ? (byte) (flags | 2) : (byte) (flags & ~2));
    }

    public boolean isShieldDisabled() {
        return this.getEntityData().get(SHIELD_DISABLED);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.heavySpearTimer > 0) {
            return false;
        }
        if (this.getRandom().nextInt(2) == 0) {
            this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.landHeavySpearAttack();
            return false;
        }
        this.swing(InteractionHand.MAIN_HAND);
        return super.doHurtTarget(entity);
    }

    public void landHeavySpearAttack() {
        Vec3 look = this.getLookAngle();
        double px = this.getX() + look.x() * 1.25D;
        double py = this.getBoundingBox().minY - (this.isPassenger() ? 0.75D : 0.0D);
        double pz = this.getZ() + look.z() * 1.25D;
        AABB spearBox = new AABB(px - 1.5D, py - 1.5D, pz - 1.5D, px + 1.5D, py + 1.5D, pz + 1.5D);
        List<Entity> targets = this.level().getEntities(this, spearBox, entity -> entity != this.getVehicle());
        for (Entity entity : targets) {
            super.doHurtTarget(entity);
        }
        if (!targets.isEmpty()) {
            this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundVolume(), this.getVoicePitch());
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_WALL) && this.getVehicle() != null) {
            return false;
        }
        Entity attacker = source.getEntity();
        if (attacker != null) {
            double dx = this.getX() - attacker.getX();
            double dz = this.getZ() - attacker.getZ();
            float angle = (float) (Math.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            float difference = Mth.abs((this.yBodyRot - angle) % 360.0F);
            if (this.hasShield() && difference > 150.0F && difference < 230.0F) {
                if (this.takeHitOnShield(source, amount)) {
                    return false;
                }
            } else if (this.hasShield() && this.getRandom().nextBoolean()) {
                this.damageShield();
            }
            if (this.hasArmor() && (difference > 300.0F || difference < 60.0F)) {
                this.breakArmor();
            }
        }
        return super.hurt(source, amount);
    }

    public boolean takeHitOnShield(DamageSource source, float amount) {
        if (this.isShieldDisabled()) {
            return false;
        }
        if (source.getEntity() instanceof LivingEntity living && living.getMainHandItem().getItem() instanceof AxeItem && !this.level().isClientSide()) {
            this.getEntityData().set(SHIELD_DISABLED, true);
            this.playSound(SoundEvents.SHIELD_BREAK, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
            return true;
        }
        if (amount > SHIELD_DAMAGE_THRESHOLD && !this.level().isClientSide()) {
            this.damageShield();
        } else {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
        }
        LivingEntity toKnockback = this.getVehicle() instanceof LivingEntity living ? living : this;
        if (source.getEntity() != null) {
            toKnockback.knockback(0.5D, source.getEntity().getX() - this.getX(), source.getEntity().getZ() - this.getZ());
        }
        return true;
    }

    private void damageShield() {
        this.shieldHits++;
        if (this.shieldHits > 3) {
            this.breakShield();
        } else {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
        }
    }

    private void breakArmor() {
        this.level().broadcastEntityEvent(this, (byte) 5);
        this.setHasArmor(false);
    }

    private void breakShield() {
        this.level().broadcastEntityEvent(this, (byte) 5);
        this.setHasShield(false);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
        } else if (id == 5) {
            this.playSound(SoundEvents.ITEM_BREAK, 0.8F, 0.8F + this.getRandom().nextFloat() * 0.4F);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.GOBLIN_KNIGHT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.GOBLIN_KNIGHT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.GOBLIN_KNIGHT_DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("hasArmor", this.hasArmor());
        tag.putBoolean("hasShield", this.hasShield());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHasArmor(tag.getBoolean("hasArmor"));
        this.setHasShield(tag.getBoolean("hasShield"));
    }
}