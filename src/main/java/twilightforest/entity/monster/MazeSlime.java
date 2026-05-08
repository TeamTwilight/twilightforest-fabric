package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class MazeSlime extends Slime {
    private static final ResourceLocation DOUBLE_HEALTH_ID = TwilightForestMod.prefix("maze_slime_double_health");
    private static final AttributeModifier DOUBLE_HEALTH = new AttributeModifier(DOUBLE_HEALTH_ID, 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public MazeSlime(EntityType<? extends MazeSlime> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH);
    }

    public static boolean getCanSpawnHere(EntityType<MazeSlime> entity, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(entity, level, reason, pos, random) && Monster.isDarkEnoughToSpawn(level, pos, random);
    }

    @Override
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.xpReward += 3;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health != null && !health.hasModifier(DOUBLE_HEALTH_ID)) {
            health.addPermanentModifier(DOUBLE_HEALTH);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    public double getMyRidingOffset() {
        return 0.25D;
    }

    @Override
    protected boolean isDealsDamage() {
        return true;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isTiny() ? TFSounds.MAZE_SLIME_HURT_SMALL : TFSounds.MAZE_SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTiny() ? TFSounds.MAZE_SLIME_DEATH_SMALL : TFSounds.MAZE_SLIME_DEATH;
    }

    @Override
    protected SoundEvent getSquishSound() {
        return this.isTiny() ? TFSounds.MAZE_SLIME_SQUISH_SMALL : TFSounds.MAZE_SLIME_SQUISH;
    }

    @Override
    protected SoundEvent getJumpSound() {
        return this.isTiny() ? TFSounds.MAZE_SLIME_SQUISH_SMALL : TFSounds.MAZE_SLIME_SQUISH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.1F * this.getSize();
    }

}