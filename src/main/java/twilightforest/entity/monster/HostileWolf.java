package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.animal.WolfVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;
import java.util.Optional;

public class HostileWolf extends Monster implements VariantHolder<Holder<WolfVariant>> {
    private static final EntityDataAccessor<Holder<WolfVariant>> VARIANT = SynchedEntityData.defineId(HostileWolf.class, EntityDataSerializers.WOLF_VARIANT);

    public HostileWolf(EntityType<? extends HostileWolf> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, this.registryAccess().registryOrThrow(Registries.WOLF_VARIANT).getHolderOrThrow(WolfVariants.PALE));
    }

    public ResourceLocation getTexture() {
        WolfVariant variant = this.getVariant().value();
        return this.isAggressive() ? variant.angryTexture() : variant.wildTexture();
    }

    @Override
    public Holder<WolfVariant> getVariant() {
        return this.getEntityData().get(VARIANT);
    }

    @Override
    public void setVariant(Holder<WolfVariant> variant) {
        this.getEntityData().set(VARIANT, variant);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("variant", this.getVariant().unwrapKey().orElse(WolfVariants.PALE).location().toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        Optional.ofNullable(ResourceLocation.tryParse(tag.getString("variant")))
                .map(location -> ResourceKey.create(Registries.WOLF_VARIANT, location))
                .flatMap(key -> this.registryAccess().registryOrThrow(Registries.WOLF_VARIANT).getHolder(key))
                .ifPresent(this::setVariant);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, HostileWolf.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected int getDisplayModel() {
        return TFItemVisuals.HOSTILE_WOLF_DISPLAY;
    }

    protected float getDisplayScale() {
        return 0.62F;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target != null && target != this.getTarget()) {
            this.playSound(this.getTargetSound(), 4.0F, this.getVoicePitch());
        }
        super.setTarget(target);
    }

    public static boolean checkWolfSpawnRules(EntityType<? extends HostileWolf> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(level, pos, random) && checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    protected SoundEvent getTargetSound() {
        return TFSounds.HOSTILE_WOLF_TARGET;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.HOSTILE_WOLF_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.HOSTILE_WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.HOSTILE_WOLF_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public float getTailAngle() {
        return this.getTarget() != null ? 1.5393804F : ((float) Math.PI / 5.0F);
    }

    public static class LeapGoal extends LeapAtTargetGoal {
        private final Mob mob;

        public LeapGoal(Mob mob, float jump) {
            super(mob, jump);
            this.mob = mob;
        }

        @Override
        public void start() {
            super.start();
            this.mob.setAggressive(true);
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
        }
    }
}