package twilightforest.world.components.structures.start;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.List;

// Landmark structure without progression lock; Hollow Hills/Hedge Maze/Naga Courtyard/Quest Grove
public abstract class LandmarkStructure extends Structure implements DecorationClearance, ControlledSpawns {
    protected static <S extends LandmarkStructure> Products.P3<RecordCodecBuilder.Mu<S>, ControlledSpawningConfig, DecorationConfig, StructureSettings> landmarkCodec(RecordCodecBuilder.Instance<S> instance) {
        return instance.group(
                ControlledSpawningConfig.FLAT_CODEC.forGetter(s -> s.controlledSpawningConfig),
                DecorationConfig.FLAT_CODEC.forGetter(s -> s.decorationConfig),
                Structure.settingsCodec(instance)
        );
    }

    final ControlledSpawningConfig controlledSpawningConfig;
    final DecorationConfig decorationConfig;

    public LandmarkStructure(ControlledSpawningConfig controlledSpawningConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(structureSettings);
        this.controlledSpawningConfig = controlledSpawningConfig;
        this.decorationConfig = decorationConfig;
    }

    @Override
    public boolean isSurfaceDecorationsAllowed() {
        return this.decorationConfig.surfaceDecorations();
    }

    @Override
    public boolean isUndergroundDecoAllowed() {
        return this.decorationConfig.undergroundDecorations();
    }

    @Override
    public boolean shouldAdjustToTerrain() {
        return this.decorationConfig.adjustElevation();
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getCombinedMonsterSpawnableList() {
        return this.controlledSpawningConfig.combinedMonsterSpawnableCache();
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getCombinedCreatureSpawnableList() {
        return this.controlledSpawningConfig.combinedCreatureSpawnableCache();
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getSpawnableList(MobCategory creatureType) {
        return switch (creatureType) {
            case MONSTER -> this.getSpawnableMonsterList(0);
            case AMBIENT -> this.controlledSpawningConfig.ambientCreatureList();
            case WATER_CREATURE -> this.controlledSpawningConfig.waterCreatureList();
            default -> List.of();
        };
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getSpawnableMonsterList(int index) {
        return this.controlledSpawningConfig.getForLabel("" + index);
    }
}
