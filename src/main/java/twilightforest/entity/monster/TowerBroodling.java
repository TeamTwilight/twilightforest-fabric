package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

public class TowerBroodling extends SwarmSpider {
    public TowerBroodling(EntityType<? extends TowerBroodling> type, Level level) {
        this(type, level, true);
    }

    public TowerBroodling(EntityType<? extends TowerBroodling> type, Level level, boolean spawnMore) {
        super(type, level, spawnMore);
        this.xpReward = 3;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return SwarmSpider.registerAttributes()
                .add(Attributes.MAX_HEALTH, 7.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected int getDisplayModel() {
        return TFItemVisuals.TOWER_BROODLING_DISPLAY;
    }

    @Override
    protected float getDisplayScale() {
        return 0.55F;
    }

    @Override
    protected boolean spawnAnother() {
        TowerBroodling another = new TowerBroodling(TFEntities.CARMINITE_BROODLING.get(), this.level(), false);
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        return data;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.CARMINITE_BROODLING_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.CARMINITE_BROODLING_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.CARMINITE_BROODLING_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.CARMINITE_BROODLING_STEP, 0.15F, 1.0F);
    }
}