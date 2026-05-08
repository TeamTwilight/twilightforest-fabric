package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
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

public class KingSpider extends Spider {
    public KingSpider(EntityType<? extends KingSpider> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Spider.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData);
        SkeletonDruid druid = TFEntities.SKELETON_DRUID.get().create(this.level());
        if (druid != null) {
            druid.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            druid.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
            Entity lastRider = this;
            while (!lastRider.getPassengers().isEmpty()) {
                lastRider = lastRider.getPassengers().get(0);
            }
            druid.startRiding(lastRider);
        }
        return data;
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.75D;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.KING_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.KING_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.KING_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.KING_SPIDER_STEP, 0.15F, 1.0F);
    }
}