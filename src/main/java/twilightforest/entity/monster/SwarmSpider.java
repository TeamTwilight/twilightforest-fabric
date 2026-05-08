package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class SwarmSpider extends Spider {
    protected boolean shouldSpawn;

    public SwarmSpider(EntityType<? extends SwarmSpider> type, Level level) {
        this(type, level, true);
    }

    public SwarmSpider(EntityType<? extends SwarmSpider> type, Level level, boolean spawnMore) {
        super(type, level);
        this.setSpawnMore(spawnMore);
        this.xpReward = 2;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Spider.createAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true) {
            protected double getAttackReachSqr(LivingEntity target) {
                return 4.0D + target.getBbWidth();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected int getDisplayModel() {
        return TFItemVisuals.SWARM_SPIDER_DISPLAY;
    }

    protected float getDisplayScale() {
        return 0.42F;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide() && this.shouldSpawnMore()) {
            int count = 1 + this.getRandom().nextInt(2);
            for (int i = 0; i < count; i++) {
                if (!this.spawnAnother()) {
                    this.spawnAnother();
                }
            }
            this.setSpawnMore(false);
        }
        super.tick();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return this.getRandom().nextInt(4) == 0 && super.doHurtTarget(entity);
    }

    protected boolean spawnAnother() {
        SwarmSpider another = new SwarmSpider(TFEntities.SWARM_SPIDER.get(), this.level(), false);
        double sx = this.getX() + (this.getRandom().nextBoolean() ? 0.9D : -0.9D);
        double sy = this.getY();
        double sz = this.getZ() + (this.getRandom().nextBoolean() ? 0.9D : -0.9D);
        another.moveTo(sx, sy, sz, this.getRandom().nextFloat() * 360.0F, 0.0F);
        if (!another.checkSpawnRules(this.level(), MobSpawnType.MOB_SUMMONED)) {
            another.discard();
            return false;
        }
        this.level().addFreshEntity(another);
        another.spawnAnim();
        return true;
    }

    public boolean shouldSpawnMore() {
        return this.shouldSpawn;
    }

    public void setSpawnMore(boolean spawnMore) {
        this.shouldSpawn = spawnMore;
    }

    public double getMyRidingOffset() {
        return 0.15D;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 6;
    }

    @Override
    public float getVoicePitch() {
        return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.5F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, spawnData);
        if (this.getFirstPassenger() == null && accessor.getRandom().nextInt(20) <= difficulty.getDifficulty().getId()) {
            SkeletonDruid druid = TFEntities.SKELETON_DRUID.get().create(this.level());
            if (druid != null) {
                druid.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                druid.finalizeSpawn(accessor, difficulty, MobSpawnType.JOCKEY, null);
                druid.setBaby(true);
                if (this.hasPassenger(entity -> true)) {
                    this.ejectPassengers();
                }
                druid.startRiding(this);
            }
        }
        return data;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.SWARM_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.SWARM_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.SWARM_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.SWARM_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("SpawnMore", this.shouldSpawnMore());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setSpawnMore(tag.getBoolean("SpawnMore"));
    }
}