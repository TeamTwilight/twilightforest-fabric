package twilightforest.world.components.structures.start;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import twilightforest.init.TFEntities;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.LegacyLandmarkGetter;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.Map;
import java.util.Optional;

@Deprecated
public class LegacyLandmark extends ProgressionStructure implements LegacyLandmarkGetter {
    public final TFLandmark feature;

    public static final Codec<LegacyLandmark> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(TFLandmark.CODEC.fieldOf("legacy_landmark_start").forGetter(LegacyLandmark::getFeatureType))
            .and(progressionCodec(instance))
            .apply(instance, LegacyLandmark::new)
    );

    public LegacyLandmark(TFLandmark landmark, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, ControlledSpawningConfig controlledSpawningConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(advancementLockConfig, hintConfig, controlledSpawningConfig, decorationConfig, structureSettings);
        this.feature = landmark;
    }

    public static LegacyLandmark extractLandmark(TFLandmark landmark) {
        return new LegacyLandmark(
                landmark,
                new AdvancementLockConfig(landmark.getRequiredAdvancements()),
                new HintConfig(landmark.createHintBook(), TFEntities.KOBOLD.get()),
                ControlledSpawns.ControlledSpawningConfig.create(landmark.getSpawnableMonsterLists(), landmark.getAmbientCreatureList(), landmark.getWaterCreatureList()),
                new DecorationClearance.DecorationConfig(landmark.isSurfaceDecorationsAllowed(), landmark.isUndergroundDecoAllowed(), landmark.shouldAdjustToTerrain()),
                new Structure.StructureSettings(
                        BuiltinRegistries.BIOME.getOrCreateTag(landmark.getBiomeTag()),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        landmark.getDecorationStage(),
                        landmark.getBeardifierContribution()
                )
        );
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return this.feature.generateStub(context);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.LEGACY_LANDMARK.get();
    }

    @Override
    public TFLandmark getFeatureType() {
        return this.feature;
    }
}
