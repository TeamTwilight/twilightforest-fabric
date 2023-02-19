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
import twilightforest.world.components.structures.lichtower.TowerMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawns;

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

    public static LichTowerStructure buildLichTowerConfig(BootstapContext<Structure> context) {
        return new LichTowerStructure(
                ControlledSpawns.ControlledSpawningConfig.create(TFLandmark.LICH_TOWER.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
                new HintConfig(HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LICH_TOWER_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
