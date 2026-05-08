package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ITFCharger;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;

import java.util.List;

public class Minoshroom extends BaseTFBoss implements ITFCharger {
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> GROUND_ATTACK = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> GROUND_CHARGE = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.INT);
    private static final ResourceLocation CHARGE_SPEED_ID = TwilightForestMod.prefix("minoshroom_charge_speed");
    private static final AttributeModifier CHARGE_SPEED_MODIFIER = new AttributeModifier(CHARGE_SPEED_ID, 0.35D, AttributeModifier.Operation.ADD_VALUE);

    private int chargeCooldown;
    private int chargeTicks;
    private int groundAttackCooldown;
    private int groundAttackTicks;
    private boolean groundSmashState;
    private float prevClientSideChargeAnimation;
    private float clientSideChargeAnimation;

    public Minoshroom(EntityType<? extends Minoshroom> type, Level level) {
        super(type, level);
        this.xpReward = 100;
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGING, false);
        builder.define(GROUND_ATTACK, false);
        builder.define(GROUND_CHARGE, 0);
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
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.RED;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            this.prevClientSideChargeAnimation = this.clientSideChargeAnimation;
            if (this.isGroundAttackCharge()) {
                this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation + (1.0F / Math.max(1.0F, (float) this.getEntityData().get(GROUND_CHARGE)) * 6.0F), 0.0F, 6.0F);
            } else {
                this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation - 1.0F, 0.0F, 6.0F);
            }
            this.clientGroundParticles();
        } else {
            this.updateChargeState();
            this.updateGroundAttack();
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

    private void updateGroundAttack() {
        if (this.groundAttackCooldown > 0) {
            --this.groundAttackCooldown;
        }
        LivingEntity target = this.getTarget();
        if (target != null && this.groundAttackCooldown <= 0 && this.distanceToSqr(target) < 144.0D && this.onGround()) {
            this.groundAttackTicks = 36;
            this.groundAttackCooldown = 160 + this.random.nextInt(80);
            this.setMaxCharge(this.groundAttackTicks);
            this.setGroundAttackCharge(true);
            this.playSound(this.getChargeSound(), 2.0F, this.getVoicePitch());
        }
        if (this.groundAttackTicks > 0) {
            --this.groundAttackTicks;
            this.setGroundAttackCharge(true);
            if (this.groundAttackTicks == 1) {
                this.doGroundSmash();
            }
        } else {
            this.setGroundAttackCharge(false);
        }
    }

    private void doGroundSmash() {
        this.groundSmashState = true;
        AABB smashBox = this.getBoundingBox().inflate(5.0D, 1.5D, 5.0D);
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, smashBox, entity -> entity != this)) {
            if (entity.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.SLAM, this), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F))) {
                double deltaX = entity.getX() - this.getX();
                double deltaZ = entity.getZ() - this.getZ();
                entity.push(deltaX * 0.25D, 0.55D, deltaZ * 0.25D);
            }
        }
        this.playSound(TFSounds.MINOSHROOM_SLAM, 2.0F, 1.0F + this.random.nextFloat() * 0.1F);
    }

    private void clientGroundParticles() {
        if (!this.groundSmashState && !this.isGroundAttackCharge()) {
            return;
        }
        BlockState block = this.level().getBlockState(this.blockPosition().below());
        int count = this.isGroundAttackCharge() ? 8 : 80;
        for (int particle = 0; particle < count; particle++) {
            double particleX = this.blockPosition().getX() + this.level().getRandom().nextFloat() * 10.0F - 5.0F;
            double particleY = this.getBoundingBox().minY + 0.1F + this.level().getRandom().nextFloat() * 0.3F;
            double particleZ = this.blockPosition().getZ() + this.level().getRandom().nextFloat() * 10.0F - 5.0F;
            this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
        }
        this.groundSmashState = false;
    }

    @Override
    public boolean isCharging() {
        return this.getEntityData().get(CHARGING);
    }

    @Override
    public void setCharging(boolean charging) {
        this.getEntityData().set(CHARGING, charging);
    }

    public boolean isGroundAttackCharge() {
        return this.getEntityData().get(GROUND_ATTACK);
    }

    public void setGroundAttackCharge(boolean charging) {
        this.getEntityData().set(GROUND_ATTACK, charging);
    }

    public void setMaxCharge(int charge) {
        this.getEntityData().set(GROUND_CHARGE, charge);
    }

    public float getChargeAnimationScale(float scale) {
        return (this.prevClientSideChargeAnimation + (this.clientSideChargeAnimation - this.prevClientSideChargeAnimation) * scale) / 6.0F;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.DIAMOND_MINOTAUR_AXE.get()));
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        boolean hurt = entity.hurt(TFDamageTypes.entitySource(this.level(), TFDamageTypes.AXING, this), damage);
        if (hurt) {
            this.playSound(TFSounds.MINOSHROOM_ATTACK, 1.0F, 1.0F);
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
        return TFSounds.MINOSHROOM_ATTACK;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.MINOSHROOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.MINOSHROOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.MINOSHROOM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.MINOSHROOM_STEP, 0.15F, 0.8F);
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
        tag.putInt("GroundAttackCooldown", this.groundAttackCooldown);
        tag.putInt("GroundAttackTicks", this.groundAttackTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.chargeCooldown = tag.getInt("ChargeCooldown");
        this.chargeTicks = tag.getInt("ChargeTicks");
        this.groundAttackCooldown = tag.getInt("GroundAttackCooldown");
        this.groundAttackTicks = tag.getInt("GroundAttackTicks");
    }

    @Override
    public int getHomeRadius() {
        return 30;
    }

    @Override
    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.LABYRINTH;
    }

    @Override
    public Block getDeathContainer(RandomSource random) {
        return TFBlocks.MANGROVE_CHEST.get();
    }

    @Override
    public Block getBossSpawner() {
        return TFBlocks.MINOSHROOM_BOSS_SPAWNER.get();
    }
}
