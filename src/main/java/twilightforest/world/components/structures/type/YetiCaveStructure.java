package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.YetiCaveComponent;

import java.util.List;
import java.util.Map;

public class YetiCaveStructure extends ConquerableStructure {
    public static final Codec<YetiCaveStructure> CODEC = RecordCodecBuilder.create(instance ->
            conquerStatusCodec(instance).apply(instance, YetiCaveStructure::new)
    );

    public YetiCaveStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new YetiCaveComponent(0, x, y, z);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.YETI_CAVE.get();
    }

    public static YetiCaveStructure buildYetiCaveConfig(BootstapContext<Structure> context) {
        return new YetiCaveStructure(
                ControlledSpawningConfig.create(TFLandmark.YETI_CAVE.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
                new HintConfig(HintConfig.book("yeticave", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_YETI_CAVE_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }
}
