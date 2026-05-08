package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ITFCharger;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class Minotaur extends Monster implements ITFCharger {
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(Minotaur.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation CHARGE_SPEED_ID = TwilightForestMod.prefix("minotaur_charge_speed");
    private static final AttributeModifier CHARGE_SPEED_MODIFIER = new AttributeModifier(CHARGE_SPEED_ID, 0.35D, AttributeModifier.Operation.ADD_VALUE);

    private int chargeCooldown;
    private int chargeTicks;

    public Minotaur(EntityType<? extends Minotaur> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.updateChargeState();
        }
        if (this.isCharging()) {
            this.walkAnimation.setSpeed(this.walkAnimation.speed() + 0.6F);
        }
    }

    private void updateChargeState() {
        if (this.chargeCooldown > 0) {
            --this.chargeCooldown;
        }
        LivingEntity target = this.getTarget();
        if (this.chargeTicks > 0) {
            --this.chargeTicks;
            this.setCharging(true);
            this.setChargeSpeed(true);
            if (target != null) {
                this.getNavigation().moveTo(target, 1.5D);
            }
            return;
        }
        this.setCharging(false);
        this.setChargeSpeed(false);
        if (target != null && this.chargeCooldown <= 0 && this.distanceToSqr(target) > 16.0D && this.distanceToSqr(target) < 256.0D && this.getSensing().hasLineOfSight(target)) {
            this.chargeTicks = 35;
            this.chargeCooldown = 100 + this.random.nextInt(80);
            this.playSound(this.getChargeSound(), 1.0F, 1.0F);
        }
    }

    private void setChargeSpeed(boolean enabled) {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) == null) {
            return;
        }
        if (enabled) {
            if (!this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(CHARGE_SPEED_ID)) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(CHARGE_SPEED_MODIFIER);
            }
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(CHARGE_SPEED_ID);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        SpawnGroupData spawnData = super.finalizeSpawn(accessor, difficulty, reason, data);
        this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
        this.populateDefaultEquipmentEnchantments(accessor, accessor.getRandom(), difficulty);
        return spawnData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, TFItemVisuals.withModel(new ItemStack(Items.IRON_AXE), TFItemVisuals.DIAMOND_MINOTAUR_AXE));
    }

    @Override
    public boolean isCharging() {
        return this.getEntityData().get(CHARGING);
    }

    @Override
    public void setCharging(boolean charging) {
        this.getEntityData().set(CHARGING, charging);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        boolean hurt = entity.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.AXING, this), damage);
        if (hurt) {
            this.playSound(TFSounds.MINOTAUR_ATTACK, 1.0F, 1.0F);
            if (knockback > 0.0F && entity instanceof LivingEntity living) {
                living.knockback(knockback * 0.5F, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }
            if (this.isCharging()) {
                entity.push(this.getDirection().getStepX(), 0.35D, this.getDirection().getStepZ());
                this.playSound(this.getChargeSound(), 1.0F, 1.0F);
            }
            this.setLastHurtMob(entity);
        }
        return hurt;
    }

    protected SoundEvent getChargeSound() {
        return TFSounds.MINOTAUR_ATTACK;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.MINOTAUR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.MINOTAUR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.MINOTAUR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.MINOTAUR_STEP, 0.15F, 0.8F);
    }

    @Override
    public float getVoicePitch() {
        return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.7F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ChargeCooldown", this.chargeCooldown);
        tag.putInt("ChargeTicks", this.chargeTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.chargeCooldown = tag.getInt("ChargeCooldown");
        this.chargeTicks = tag.getInt("ChargeTicks");
    }
}