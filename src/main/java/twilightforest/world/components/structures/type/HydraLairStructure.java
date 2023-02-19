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
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.HydraLairComponent;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.List;
import java.util.Map;

public class HydraLairStructure extends ProgressionStructure {
    public static final Codec<HydraLairStructure> CODEC = RecordCodecBuilder.create(instance ->
            progressionCodec(instance).apply(instance, HydraLairStructure::new)
    );

    public HydraLairStructure(AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(advancementLockConfig, hintConfig, decorationConfig, structureSettings);
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return new HydraLairComponent(0, x - 7, y, z - 7);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.HYDRA_LAIR.get();
    }

    public static HydraLairStructure buildHydraLairConfig(BootstapContext<Structure> context) {
        return new HydraLairStructure(
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_labyrinth"))),
                new HintConfig(HintConfig.book("hydralair", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(false, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HYDRA_LAIR_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                )
        );
    }
}
