package twilightforest.entity.passive;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;

/**
 * Twilight Forest bighorn sheep with server-side TF behavior and paired-client
 * display visuals.
 */
public class Bighorn extends Sheep {
    public Bighorn(EntityType<? extends Bighorn> type, Level level) {
        super(type, level);
    }

    @Override
    public ResourceKey<LootTable> getDefaultLootTable() {
        if (this.isSheared()) {
            return this.getType().getDefaultLootTable();
        }
        return switch (this.getColor()) {
            case ORANGE -> TFLootTables.BIGHORN_SHEEP_ORANGE;
            case MAGENTA -> TFLootTables.BIGHORN_SHEEP_MAGENTA;
            case LIGHT_BLUE -> TFLootTables.BIGHORN_SHEEP_LIGHT_BLUE;
            case YELLOW -> TFLootTables.BIGHORN_SHEEP_YELLOW;
            case LIME -> TFLootTables.BIGHORN_SHEEP_LIME;
            case PINK -> TFLootTables.BIGHORN_SHEEP_PINK;
            case GRAY -> TFLootTables.BIGHORN_SHEEP_GRAY;
            case LIGHT_GRAY -> TFLootTables.BIGHORN_SHEEP_LIGHT_GRAY;
            case CYAN -> TFLootTables.BIGHORN_SHEEP_CYAN;
            case PURPLE -> TFLootTables.BIGHORN_SHEEP_PURPLE;
            case BLUE -> TFLootTables.BIGHORN_SHEEP_BLUE;
            case BROWN -> TFLootTables.BIGHORN_SHEEP_BROWN;
            case GREEN -> TFLootTables.BIGHORN_SHEEP_GREEN;
            case RED -> TFLootTables.BIGHORN_SHEEP_RED;
            case BLACK -> TFLootTables.BIGHORN_SHEEP_BLACK;
            default -> TFLootTables.BIGHORN_SHEEP_WHITE;
        };
    }

    private static DyeColor getRandomFleeceColor(RandomSource random) {
        return random.nextBoolean() ? DyeColor.BROWN : DyeColor.byId(random.nextInt(16));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        spawnData = super.finalizeSpawn(accessor, difficulty, reason, spawnData);
        this.setColor(getRandomFleeceColor(accessor.getRandom()));
        return spawnData;
    }

    @Nullable
    @Override
    public Sheep getBreedOffspring(ServerLevel level, AgeableMob mate) {
        if (!(mate instanceof Bighorn otherParent)) {
            TwilightForestMod.LOGGER.error("Code was called to breed a Bighorn with a non Bighorn! Cancelling!");
            return null;
        }

        Bighorn babySheep = TFEntities.BIGHORN_SHEEP.get().create(level);
        if (babySheep != null) {
            babySheep.setColor(level.getRandom().nextBoolean() ? this.getColor() : otherParent.getColor());
        }
        return babySheep;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader reader) {
        BlockState state = reader.getBlockState(pos.below());
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.PODZOL) ? 10.0F : reader.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.BIGHORN_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.BIGHORN_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.BIGHORN_SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.BIGHORN_SHEEP_STEP, 0.15F, 1.0F);
    }

}
