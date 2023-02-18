package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructureTypes;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.CustomStructureData;
import twilightforest.world.components.structures.util.ProgressionStructure;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Deprecated
public class LegacyStructure extends ProgressionStructure implements CustomStructureData, ConfigurableSpawns {
    public final TFLandmark feature;
    private final ControlledSpawningConfig controlledSpawningConfig;

    public static final Codec<LegacyStructure> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    TFLandmark.CODEC.fieldOf("legacy_landmark_start").forGetter(LegacyStructure::getFeatureType),
                    ControlledSpawningConfig.FLAT_CODEC.forGetter(s -> s.controlledSpawningConfig)
            )
            .and(progressionCodec(instance))
            .apply(instance, LegacyStructure::new)
    );

    public LegacyStructure(TFLandmark landmark, ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(advancementLockConfig, hintConfig, decorationConfig, structureSettings);
        this.feature = landmark;
        this.controlledSpawningConfig = controlledSpawningConfig;
    }

    @Override
    public StructureStart generate(RegistryAccess registryAccess, ChunkGenerator chunkGen, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkPos chunkPos, int references, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> biomeAt) {
        return LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkPos) ? generateCustom(registryAccess, chunkGen, biomeSource, randomState, structureTemplateManager, seed, chunkPos, references, heightAccessor, biomeAt) : StructureStart.INVALID_START;
    }

    // [VANILLA COPY] Structure.generate
    //  StructureStart construction swapped for TFStructureStart construction
    public StructureStart generateCustom(RegistryAccess pRegistryAccess, ChunkGenerator pChunkGenerator, BiomeSource pBiomeSource, RandomState pRandomState, StructureTemplateManager pStructureTemplateManager, long pSeed, ChunkPos pChunkPos, int p_226604_, LevelHeightAccessor pHeightAccessor, Predicate<Holder<Biome>> pValidBiome) {
        Structure.GenerationContext structure$generationcontext = new Structure.GenerationContext(pRegistryAccess, pChunkGenerator, pBiomeSource, pRandomState, pStructureTemplateManager, pSeed, pChunkPos, pHeightAccessor, pValidBiome);
        Optional<Structure.GenerationStub> optional = this.findValidGenerationPoint(structure$generationcontext);
        if (optional.isPresent()) {
            StructurePiecesBuilder structurepiecesbuilder = optional.get().getPiecesBuilder();
            StructureStart structurestart = new TFStructureStart(this, pChunkPos, p_226604_, structurepiecesbuilder.build());
            if (structurestart.isValid()) {
                return structurestart;
            }
        }

        return StructureStart.INVALID_START;
    }

    @Override
    protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
        return this.feature.provideFirstPiece(context.structureTemplateManager(), context.chunkGenerator(), random, x, y, z);
    }

    @Override
    @Deprecated
    protected boolean dontCenter() {
        return this.feature == TFLandmark.LICH_TOWER || this.feature == TFLandmark.TROLL_CAVE || this.feature == TFLandmark.YETI_CAVE;
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.LEGACY_LANDMARK.get();
    }

    public TFLandmark getFeatureType() {
        return this.feature;
    }

    @Override
    public ControlledSpawningConfig getConfig() {
        return this.controlledSpawningConfig;
    }

    @Deprecated
    public static LegacyStructure buildLichTowerConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.LICH_TOWER,
                ControlledSpawningConfig.create(TFLandmark.LICH_TOWER.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
                new HintConfig(book("lichtower", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LICH_TOWER_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildLabyrinthConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.LABYRINTH,
                ControlledSpawningConfig.create(TFLandmark.LABYRINTH.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
                new HintConfig(book("labyrinth", 5), TFEntities.KOBOLD.get()),
                new DecorationConfig(true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LABYRINTH_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildHydraLairConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.HYDRA_LAIR,
                ControlledSpawningConfig.create(List.of(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_labyrinth"))),
                new HintConfig(book("hydralair", 4), TFEntities.KOBOLD.get()),
                new DecorationConfig(false, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HYDRA_LAIR_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildKnightStrongholdConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.KNIGHT_STRONGHOLD,
                ControlledSpawningConfig.create(TFLandmark.KNIGHT_STRONGHOLD.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_trophy_pedestal"))),
                new HintConfig(book("tfstronghold", 5), TFEntities.KOBOLD.get()),
                new DecorationConfig(true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_KNIGHT_STRONGHOLD_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildDarkTowerConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.DARK_TOWER,
                ControlledSpawningConfig.create(TFLandmark.DARK_TOWER.getSpawnableMonsterLists(), List.of(), TFLandmark.DARK_TOWER.getSpawnableList(MobCategory.WATER_CREATURE)),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_knights"))),
                new HintConfig(book("darktower", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_DARK_TOWER_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildYetiCaveConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.YETI_CAVE,
                ControlledSpawningConfig.create(TFLandmark.YETI_CAVE.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
                new HintConfig(book("yeticave", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(true, false, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_YETI_CAVE_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildAuroraPalaceConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.ICE_TOWER,
                ControlledSpawningConfig.create(TFLandmark.ICE_TOWER.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_yeti"))),
                new HintConfig(book("icetower", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(false, true, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_AURORA_PALACE_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildTrollCaveConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.TROLL_CAVE,
                ControlledSpawningConfig.create(TFLandmark.TROLL_CAVE.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_merge"))),
                new HintConfig(book("trollcave", 3), TFEntities.KOBOLD.get()),
                new DecorationConfig(true, true, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_TROLL_CAVE_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.BURY
                )
        );
    }

    @Deprecated
    public static LegacyStructure buildFinalCastleConfig(BootstapContext<Structure> context) {
        return new LegacyStructure(
                TFLandmark.FINAL_CASTLE,
                ControlledSpawningConfig.create(TFLandmark.FINAL_CASTLE.getSpawnableMonsterLists(), List.of(), List.of()),
                new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_troll"))),
                new HintConfig(defaultBook(), TFEntities.KOBOLD.get()),
                new DecorationConfig(false, true, false),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_FINAL_CASTLE_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }

    private static ItemStack defaultBook() {
        return book("unknown", 2);
    }

    private static ItemStack book(String name, int pageCount) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        book.getOrCreateTag().putBoolean(TwilightForestMod.ID + ":book", true);
        StructureHints.addBookInformationStatic(book, new ListTag(), name, pageCount);
        return book;
    }
}
