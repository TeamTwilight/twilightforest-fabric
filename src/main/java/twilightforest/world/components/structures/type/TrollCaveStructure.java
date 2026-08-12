package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFMain;
import twilightforest.TFRegistries;
import twilightforest.tags.TFBiomeTags;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.init.TFStructureTypes;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.trollcave.TrollCaveMainComponent;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrollCaveStructure extends ProgressionStructure implements ConfigurableSpawns {
	public static final MapCodec<TrollCaveStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
		.group(
			ControlledSpawns.ControlledSpawningConfig.CODEC.fieldOf(ControlledSpawns.CODEC_NAME).forGetter(ConfigurableSpawns::getConfig),
			StructureSpeleothemConfigs.CODEC.fieldOf("speleothem_config").forGetter(s -> s.speleothemConfig)
		)
		.and(progressionCodec(instance))
		.apply(instance, TrollCaveStructure::new)
	);

	private final ControlledSpawningConfig controlledSpawningConfig;
	private final Holder.Reference<StructureSpeleothemConfig> speleothemConfig;

	public TrollCaveStructure(ControlledSpawningConfig controlledSpawningConfig, Holder<StructureSpeleothemConfig> speleothemConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);

		this.controlledSpawningConfig = controlledSpawningConfig;
		this.speleothemConfig = (Holder.Reference<StructureSpeleothemConfig>) speleothemConfig;
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new TrollCaveMainComponent(TFStructurePieceTypes.TFTCMai, 0, x, y + 11, z, this.speleothemConfig);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.TROLL_CAVE;
	}

	@Override
	public ControlledSpawningConfig getConfig() {
		return this.controlledSpawningConfig;
	}

	public static TrollCaveStructure buildTrollCaveConfig(BootstrapContext<Structure> context) {
		return new TrollCaveStructure(
			ControlledSpawningConfig.create(List.of(WeightedList.<MobSpawnSettings.SpawnerData>builder()
				.add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 2), 5)
				.add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.TROLL, 1, 2), 20)
				.add(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1), 5)
				.build()
			, WeightedList.<MobSpawnSettings.SpawnerData>builder()
				.add(new MobSpawnSettings.SpawnerData(TFEntities.GIANT_MINER, 1, 1), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.ARMORED_GIANT, 1, 1), 10)
				.build()
			), WeightedList.of(), WeightedList.of()),
			context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.TROLL_CAVE),
			new AdvancementLockConfig(List.of(TFMain.prefix("progress_merge"))),
			Optional.of(new HintConfig(HintConfig.book("trollcave", 3), TFEntities.KOBOLD)),
			Optional.of(new DecorationConfig(4, true, true, false)),
			false, Optional.of(BuiltInRegistries.MAP_DECORATION_TYPE.wrapAsHolder(TFMapDecorations.TROLL_CAVES)),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_TROLL_CAVE_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
				TerrainAdjustment.BURY
			)
		);
	}
}
