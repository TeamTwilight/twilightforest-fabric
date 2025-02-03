package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructureTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.world.components.chunkgenerators.BoxDensityFunction;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.lichtower.TowerMainComponent;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerBaseTrim;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerFoyer;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerWingBeard;
import twilightforest.world.components.structures.lichtowerrevamp.LichYardBox;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LichTowerStructure extends ControlledSpawningStructure implements CustomDensitySource {
	public static final MapCodec<LichTowerStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, LichTowerStructure::new)
	);
	public static final boolean REVAMP = true;

	public LichTowerStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return REVAMP ? makeFoyer(context, random, x, y, z) : new TowerMainComponent(random, 0, x, y, z);
	}

	@Nullable
	private static LichTowerFoyer makeFoyer(GenerationContext context, RandomSource random, int x, int y, int z) {
		Direction direction = Rotation.getRandom(random).rotate(Direction.SOUTH);
		BlockPos placePos = new BlockPos(x, y, z).relative(direction, -7); // Shift to re-align yard with grass-clearing zone
		FrontAndTop oriented = FrontAndTop.fromFrontAndTop(Direction.UP, direction);

		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(placePos, BlockPos.ZERO, oriented, context.structureTemplateManager(), TwilightForestMod.prefix("lich_tower/tower_foyer"), "twilightforest:lich_tower/vestibule", random);

		return placeContext == null ? null : new LichTowerFoyer(context.structureTemplateManager(), placeContext, true, random.nextBoolean());
	}

	@Override
	protected void generateFromStartingPiece(StructurePiece startingPiece, GenerationContext context, StructurePiecesBuilder structurePiecesBuilder) {
		super.generateFromStartingPiece(startingPiece, context, structurePiecesBuilder);

		if (startingPiece instanceof LichTowerFoyer foyerPiece) {
			LichYardBox.beginYard(foyerPiece, context, structurePiecesBuilder);
		}
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.LICH_TOWER.get();
	}

	@SuppressWarnings("unchecked")
	public static LichTowerStructure buildLichTowerConfig(BootstrapContext<Structure> context) {
		final ControlledSpawningConfig monsters;
		if (REVAMP) { // For the new Lich Tower
			List<MobSpawnSettings.SpawnerData> yardSpawns = List.of(
				new MobSpawnSettings.SpawnerData(TFEntities.RISING_ZOMBIE.value(), 2, 1, 2)
			);
			List<MobSpawnSettings.SpawnerData> interiorSpawns = List.of(
				new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 1, 2),
				new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 1, 2),
				new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1, 1),
				new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2),
				new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.value(), 10, 2, 3),
				new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1)
			);
			monsters = ControlledSpawningConfig.justMonsters(
				yardSpawns,
				interiorSpawns
			);
		} else { // For the current Lich Tower
			monsters = ControlledSpawningConfig.firstIndexMonsters(
				new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 1, 2),
				new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 1, 2),
				new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1, 1),
				new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2),
				new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.value(), 10, 2, 3),
				new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1)
			);
		}
		return new LichTowerStructure(
			monsters,
			new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
			new HintConfig(HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get()),
			new DecorationConfig(2.5f, false, true, false, true),
			true, Optional.of(TFMapDecorations.LICH_TOWER),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LICH_TOWER_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.BEARD_THIN
			)
		);
	}

	@Override
	public DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource) {
		if (!REVAMP) return DensityFunctions.constant(0);

		List<BoundingBox> trimBoxes = new ArrayList<>();

		int yBase = structurePieceSource.getPieces().getFirst().getBoundingBox().minY();

		DensityFunction activator = DensityFunctions.yClampedGradient(yBase - 2, yBase - 1, 1, 0);

		for (var piece : structurePieceSource.getPieces()) {
			if (piece instanceof LichTowerFoyer || piece instanceof LichTowerBaseTrim || (piece instanceof LichTowerWingBeard beard && beard.isTrim())) {
				trimBoxes.add(piece.getBoundingBox());
			} else if (piece instanceof LichYardBox yard && yard.getTerrainAdjustment() != TerrainAdjustment.NONE) {
				trimBoxes.add(piece.getBoundingBox().moved(0, -5, 0));
			}
		}

		return DensityFunctions.mul(activator, BoxDensityFunction.combine(trimBoxes, -5, -5, TerrainAdjustment.BURY));
	}

	@Override
	public int adjustForTerrain(GenerationContext context, int x, int z) {
		int chunkOriginX = x & ~0b1111;
		int chunkOriginZ = z & ~0b1111;

		// Here we use the geometric mean of the corners and the center

		IntList heights = new IntArrayList(IntList.of(
			context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()),
			context.chunkGenerator().getFirstOccupiedHeight(chunkOriginX, chunkOriginZ, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()),
			context.chunkGenerator().getFirstOccupiedHeight(chunkOriginX + 15, chunkOriginZ, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()),
			context.chunkGenerator().getFirstOccupiedHeight(chunkOriginX, chunkOriginZ + 15, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()),
			context.chunkGenerator().getFirstOccupiedHeight(chunkOriginX + 15, chunkOriginZ + 15, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState())
		));

		int minimumHeight = heights.intStream().min().orElse(0);
		int multiplied = 1;
		for (int index = 0; index < heights.size(); index++) {
			multiplied *= heights.getInt(index) - minimumHeight;
		}

		// Exponent of 1/5 is the same as 5th root
		return Mth.floor(Math.pow(multiplied, 0.2)) + minimumHeight;
	}
}
