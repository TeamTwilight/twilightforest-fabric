package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.minotaurmaze.MazeRuinsComponent;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.ConquerableStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LabyrinthStructure extends ConquerableStructure implements ConfigurableSpawns {
    public static final Codec<LabyrinthStructure> CODEC = RecordCodecBuilder.create(instance ->
            conquerStatusCodec(instance).apply(instance, LabyrinthStructure::new)
    );

    public LabyrinthStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new MazeRuinsComponent(0, x, y, z);
    }

    @Override
    public ControlledSpawningConfig getConfig() {
        return this.controlledSpawningConfig;
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.LABYRINTH.get();
    }

    public static LabyrinthStructure buildLabyrinthConfig(BootstapContext<Structure> context) {
        return new LabyrinthStructure(
                ControlledSpawningConfig.firstIndexMonsters(
                        new MobSpawnSettings.SpawnerData(TFEntities.MINOTAUR.get(), 20, 2, 3),
                        new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.MAZE_SLIME.get(), 10, 2, 4),
                        new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.FIRE_BEETLE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.PINCH_BEETLE.get(), 10, 1, 1)
                ),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
                new HintConfig(HintConfig.book("labyrinth", 5), TFEntities.KOBOLD.get()),
                new DecorationConfig(3, true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LABYRINTH_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }
}
