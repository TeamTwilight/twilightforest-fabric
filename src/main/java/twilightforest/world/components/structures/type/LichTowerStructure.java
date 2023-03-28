package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.lichtower.TowerMainComponent;
import twilightforest.world.components.structures.util.ConquerableStructure;

import java.util.List;
import java.util.Map;

public class LichTowerStructure extends ConquerableStructure {
    public static final Codec<LichTowerStructure> CODEC = RecordCodecBuilder.create(instance ->
            conquerStatusCodec(instance).apply(instance, LichTowerStructure::new)
    );

    public LichTowerStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new TowerMainComponent(random, 0, x, y, z);
        //return new TowerFoyer(context.structureTemplateManager(), new BlockPos(x, y + 1, z));
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.LICH_TOWER.get();
    }

    @Override
    protected boolean dontCenter() {
        return true;
    }

    public static LichTowerStructure buildLichTowerConfig(BootstapContext<Structure> context) {
        return new LichTowerStructure(
                ControlledSpawningConfig.firstIndexMonsters(
                        new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1, 1),
                        new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.get(), 10, 2, 3),
                        new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1)
                ),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
                new HintConfig(HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(1, false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LICH_TOWER_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
