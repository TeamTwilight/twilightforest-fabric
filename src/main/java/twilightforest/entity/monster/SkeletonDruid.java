package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import twilightforest.TwilightForestMod;
import twilightforest.entity.projectile.NatureBolt;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;
import java.util.Objects;

public class SkeletonDruid extends AbstractSkeleton {
    private static final ResourceLocation BABY_SPEED_MODIFIER_ID = TwilightForestMod.prefix("skeleton_druid_baby_speed_boost");
    private static final AttributeModifier BABY_SPEED_MODIFIER = new AttributeModifier(BABY_SPEED_MODIFIER_ID, 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(SkeletonDruid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDimensions BABY_DIMENSIONS = TFEntities.SKELETON_DRUID.get().getDimensions().scale(0.5F).withEyeHeight(0.93F);

    private RangedAttackGoal rangedAttackGoal;

    public SkeletonDruid(EntityType<? extends SkeletonDruid> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.rangedAttackGoal = new RangedAttackGoal(this, 1.25D, 60, 5.0F);
        this.goalSelector.addGoal(4, this.rangedAttackGoal);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BABY_ID, false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.SKELETON_DRUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.SKELETON_DRUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.SKELETON_DRUID_DEATH;
    }

    @Override
    public SoundEvent getStepSound() {
        return TFSounds.SKELETON_DRUID_STEP;
    }

    @Override
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide()) {
            this.goalSelector.removeGoal(this.rangedAttackGoal);
            if (this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof HoeItem) {
                this.goalSelector.addGoal(4, this.rangedAttackGoal);
            } else {
                super.reassessWeaponGoal();
            }
        } else {
            super.reassessWeaponGoal();
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        if (this.isBaby()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, TFItemVisuals.withModel(new ItemStack(Items.STICK), TFItemVisuals.SCEPTER_OF_TWILIGHT));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, TFItemVisuals.withModel(new ItemStack(Items.GOLDEN_HOE), TFItemVisuals.STEELEAF_HOE));
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof HoeItem) {
            NatureBolt natureBolt = new NatureBolt(this.level(), this);
            this.playSound(TFSounds.SKELETON_DRUID_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));

            double targetX = target.getX() - this.getX();
            double targetY = target.getY() + target.getEyeHeight() - 2.7D - this.getY();
            double targetZ = target.getZ() - this.getZ();
            float heightOffset = Mth.sqrt((float) (targetX * targetX + targetZ * targetZ)) * 0.2F;
            natureBolt.shoot(targetX, targetY + heightOffset, targetZ, 0.6F, 6.0F);
            this.level().addFreshEntity(natureBolt);
        } else if (!this.getItemInHand(InteractionHand.MAIN_HAND).is(Items.STICK)) {
            super.performRangedAttack(target, distanceFactor);
        }
    }

    public static boolean checkDruidSpawnRules(EntityType<? extends SkeletonDruid> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(level, pos, random) && checkMobSpawnRules(type, level, reason, pos, random);
    }

    public static boolean isValidLightLevel(LevelAccessor level, BlockPos pos, RandomSource random) {
        if (level.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
            return false;
        }
        return level.getMaxLocalRawBrightness(pos) <= random.nextInt(12);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsBaby", this.isBaby());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBaby(tag.getBoolean("IsBaby"));
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    public int getBaseExperienceReward() {
        if (this.isBaby()) {
            this.xpReward = (int) (this.xpReward * 2.5F);
        }
        return super.getBaseExperienceReward();
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level().isClientSide()) {
            AttributeInstance speed = Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED));
            speed.removeModifier(BABY_SPEED_MODIFIER_ID);
            if (baby) {
                speed.addTransientModifier(BABY_SPEED_MODIFIER);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (DATA_BABY_ID.equals(dataAccessor)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }
}
