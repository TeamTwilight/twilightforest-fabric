package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.QuestGrove;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Map;

public class QuestGroveStructure extends LandmarkStructure {
    public static final Codec<QuestGroveStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, QuestGroveStructure::new));

    public QuestGroveStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(decorationConfig, structureSettings);
    }

    @Override
    protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new QuestGrove(context.structureTemplateManager(), new BlockPos(x - 12, context.chunkGenerator().getSeaLevel() + 5, z - 12));
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.QUEST_GROVE.get();
    }

    public static QuestGroveStructure buildStructureConfig(BootstapContext<Structure> context) {
        return new QuestGroveStructure(
                new DecorationConfig(false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_QUEST_GROVE_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
