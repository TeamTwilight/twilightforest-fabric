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
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.courtyard.CourtyardMain;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Map;

public class NagaCourtyardStructure extends LandmarkStructure {
    public static final Codec<NagaCourtyardStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, NagaCourtyardStructure::new));

    public NagaCourtyardStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(decorationConfig, structureSettings);
    }

    @Override
    protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new CourtyardMain(random, 0, x + 1, context.chunkGenerator().getSeaLevel() + 5, z + 1, context.structureTemplateManager());
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.NAGA_COURTYARD.get();
    }

    public static NagaCourtyardStructure buildStructureConfig(BootstapContext<Structure> context) {
        return new NagaCourtyardStructure(
                new DecorationConfig(3, false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_NAGA_COURTYARD_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
