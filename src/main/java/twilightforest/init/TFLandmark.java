package twilightforest.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.util.IntPair;
import twilightforest.world.components.structures.*;
import twilightforest.world.components.structures.courtyard.CourtyardMain;
import twilightforest.world.components.structures.darktower.DarkTowerMainComponent;
import twilightforest.world.components.structures.finalcastle.FinalCastleMainComponent;
import twilightforest.world.components.structures.icetower.IceTowerMainComponent;
import twilightforest.world.components.structures.lichtower.TowerMainComponent;
import twilightforest.world.components.structures.minotaurmaze.MazeRuinsComponent;
import twilightforest.world.components.structures.mushroomtower.MushroomTowerMainComponent;
import twilightforest.world.components.structures.stronghold.StrongholdEntranceComponent;
import twilightforest.world.components.structures.trollcave.TrollCaveMainComponent;
import twilightforest.world.components.structures.util.*;
import twilightforest.world.registration.TFGenerationSettings;

import org.jetbrains.annotations.Nullable;
import java.util.*;

/**
 * Arbiting class that decides what feature goes where in the world, in terms of the major features in the world
 */
@Deprecated
public class TFLandmark implements StructureHints, AdvancementLockedStructure, DecorationClearance, ControlledSpawns {
	public static final TFLandmark NOTHING = new TFLandmark( 0, "no_feature"       , false, null, TerrainAdjustment.NONE) { { this.enableDecorations().disableStructure(); } };
	public static final TFLandmark SMALL_HILL = new TFLandmark( 1, "small_hollow_hill", true, BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES, TerrainAdjustment.NONE, true) {
		{
			this.enableDecorations().enableTerrainAlterations();
			this.undergroundDecoAllowed = false;

			this.addMonster(EntityType.SPIDER, 10, 4, 4)
					.addMonster(EntityType.ZOMBIE, 10, 4, 4)
					.addMonster(TFEntities.REDCAP.get(), 10, 4, 4)
					.addMonster(TFEntities.SWARM_SPIDER.get(), 10, 4, 4)
					.addMonster(TFEntities.KOBOLD.get(), 10, 4, 8);
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), this, 0, size, x - 3, y - 2, z - 3);
		}
	};
	public static final TFLandmark MEDIUM_HILL = new TFLandmark( 2, "medium_hollow_hill", true, BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES, TerrainAdjustment.NONE, true ) {
		{
			this.enableDecorations().enableTerrainAlterations();
			this.undergroundDecoAllowed = false;

			this.addMonster(TFEntities.REDCAP.get(), 10, 4, 4)
					.addMonster(TFEntities.REDCAP_SAPPER.get(), 1, 1, 4)
					.addMonster(TFEntities.KOBOLD.get(), 10, 4, 8)
					.addMonster(EntityType.SKELETON, 10, 4, 4)
					.addMonster(TFEntities.SWARM_SPIDER.get(), 10, 4, 4)
					.addMonster(EntityType.SPIDER, 10, 4, 4)
					.addMonster(EntityType.CREEPER, 10, 4, 4)
					.addMonster(TFEntities.FIRE_BEETLE.get(), 5, 4, 4)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 5, 4, 4)
					.addMonster(EntityType.WITCH, 1, 1, 1);
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), this, 0, size, x - 7, y - 5, z - 7);
		}
	};
	public static final TFLandmark LARGE_HILL = new TFLandmark( 3, "large_hollow_hill", true, BiomeTagGenerator.VALID_HOLLOW_HILL_BIOMES, TerrainAdjustment.NONE, true ) {
		{
			this.enableDecorations().enableTerrainAlterations();
			this.undergroundDecoAllowed = false;

			this.addMonster(TFEntities.REDCAP.get(), 10, 4, 4)
					.addMonster(TFEntities.REDCAP_SAPPER.get(), 2, 1, 4)
					.addMonster(EntityType.SKELETON, 10, 4, 4)
					.addMonster(EntityType.CAVE_SPIDER, 10, 4, 4)
					.addMonster(EntityType.CREEPER, 10, 4, 4)
					.addMonster(EntityType.ENDERMAN, 1, 1, 4)
					.addMonster(TFEntities.WRAITH.get(), 2, 1, 4)
					.addMonster(TFEntities.FIRE_BEETLE.get(), 10, 4, 4)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 10, 4, 4)
					.addMonster(TFEntities.PINCH_BEETLE.get(), 10, 2, 4)
					.addMonster(EntityType.WITCH, 1, 1, 1);
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), this, 0, size, x - 11, y - 5, z - 11);
		}
	};
	public static final TFLandmark HEDGE_MAZE = new TFLandmark( 2, "hedge_maze", true, BiomeTagGenerator.VALID_HEDGE_MAZE_BIOMES, TerrainAdjustment.BEARD_THIN) {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}
		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HedgeMazeComponent(this, 0, x + 1, y + 4, z + 1);
		}
	};
	public static final TFLandmark QUEST_GROVE = new TFLandmark( 1, "quest_grove" , true, BiomeTagGenerator.VALID_QUEST_GROVE_BIOMES, TerrainAdjustment.BEARD_THIN) {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new QuestGrove(structureManager, new BlockPos(x - 12, y, z - 12));
		}
	};
	public static final TFLandmark NAGA_COURTYARD = new TFLandmark( 3, "naga_courtyard", true, BiomeTagGenerator.VALID_NAGA_COURTYARD_BIOMES, TerrainAdjustment.BEARD_THIN) {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new CourtyardMain(this, rand, 0, x + 1, y + 1, z + 1, structureManager);
		}
	};
	public static final TFLandmark LICH_TOWER = new TFLandmark( 1, "lich_tower", true, BiomeTagGenerator.VALID_LICH_TOWER_BIOMES, TerrainAdjustment.BEARD_THIN, TwilightForestMod.prefix("progress_naga") ) {
		{
			this.addMonster(EntityType.ZOMBIE, 10, 4, 4)
					.addMonster(EntityType.SKELETON, 10, 4, 4)
					.addMonster(EntityType.CREEPER, 1, 4, 4)
					.addMonster(EntityType.ENDERMAN, 1, 1, 4)
					.addMonster(TFEntities.DEATH_TOME.get(), 10, 4, 4)
					.addMonster(EntityType.WITCH, 1, 1, 1);

			this.adjustToTerrainHeight = true;
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.lichtower", 4);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf("Notes on a Pointy Tower"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new TowerMainComponent(this, rand, 0, x, y, z);
		}
	};
	public static final TFLandmark HYDRA_LAIR = new TFLandmark( 2, "hydra_lair"    , true, BiomeTagGenerator.VALID_HYDRA_LAIR_BIOMES, TerrainAdjustment.NONE, true, TwilightForestMod.prefix("progress_labyrinth") ) {
		{
			this.enableTerrainAlterations();
			this.undergroundDecoAllowed = false;
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.hydralair", 4);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf("Notes on the Fire Swamp"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HydraLairComponent(this, rand, 0, x - 7, y, z - 7);
		}
	};
	public static final TFLandmark LABYRINTH = new TFLandmark( 3, "labyrinth", true, BiomeTagGenerator.VALID_LABYRINTH_BIOMES, TerrainAdjustment.BURY, TwilightForestMod.prefix("progress_lich") ) {
		{
			this.enableDecorations();
			this.undergroundDecoAllowed = false;

			this.addMonster(TFEntities.MINOTAUR.get(), 20, 2, 4)
					.addMonster(EntityType.CAVE_SPIDER, 10, 4, 4)
					.addMonster(EntityType.CREEPER, 10, 4, 4)
					.addMonster(TFEntities.MAZE_SLIME.get(), 10, 4, 4)
					.addMonster(EntityType.ENDERMAN, 1, 1, 4)
					.addMonster(TFEntities.FIRE_BEETLE.get(), 10, 4, 4)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 10, 4, 4)
					.addMonster(TFEntities.PINCH_BEETLE.get(), 10, 2, 4);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.labyrinth", 5);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf("Notes on a Swampy Labyrinth"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new MazeRuinsComponent(this, 0, x, y, z);
		}

		@Override
		public GenerationStep.Decoration getDecorationStage() {
			return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
		}
	};
	public static final TFLandmark DARK_TOWER = new TFLandmark( 1, "dark_tower", true, BiomeTagGenerator.VALID_DARK_TOWER_BIOMES, TerrainAdjustment.BEARD_THIN, TwilightForestMod.prefix("progress_knights") ) {
		{
			this.addMonster(TFEntities.CARMINITE_GOLEM.get(), 10, 4, 4)
					.addMonster(EntityType.SKELETON, 10, 4, 4)
					.addMonster(EntityType.CREEPER, 10, 4, 4)
					.addMonster(EntityType.ENDERMAN, 2, 1, 4)
					.addMonster(EntityType.WITCH, 1, 1, 1)
					.addMonster(TFEntities.CARMINITE_GHASTLING.get(), 10, 1, 4)
					.addMonster(TFEntities.CARMINITE_BROODLING.get(), 10, 8, 8)
					.addMonster(TFEntities.PINCH_BEETLE.get(), 10, 2, 4)
					// roof ghasts
					.addMonster(1, TFEntities.CARMINITE_GHASTGUARD.get(), 10, 1, 4)
					// aquarium squids (only in aquariums between y = 35 and y = 64. :/
					.addWaterCreature(EntityType.SQUID, 10, 4, 4);

			this.adjustToTerrainHeight = true;
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.darktower", 3);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf("Notes on a Wooden Tower"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new DarkTowerMainComponent(this, rand, 0, x, y, z);
		}
	};
	public static final TFLandmark KNIGHT_STRONGHOLD = new TFLandmark( 3, "knight_stronghold", true, BiomeTagGenerator.VALID_KNIGHT_STRONGHOLD_BIOMES, TerrainAdjustment.BURY, TwilightForestMod.prefix("progress_trophy_pedestal") ) {
		{
			this.enableDecorations().disableProtectionAura();
			this.undergroundDecoAllowed = false;

			this.addMonster(TFEntities.BLOCKCHAIN_GOBLIN.get(), 10, 4, 4)
					.addMonster(TFEntities.LOWER_GOBLIN_KNIGHT.get(), 5, 1, 2)
					.addMonster(TFEntities.HELMET_CRAB.get(), 10, 4, 4)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 10, 4, 4)
					.addMonster(TFEntities.REDCAP_SAPPER.get(), 2, 1, 4)
					.addMonster(TFEntities.KOBOLD.get(), 10, 4, 8)
					.addMonster(EntityType.CREEPER, 10, 4, 4)
					.addMonster(EntityType.SLIME, 5, 4, 4);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.tfstronghold", 5);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf("Notes on a Stronghold"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new StrongholdEntranceComponent(this, 0, x, y + 5, z);
		}

		@Override
		public GenerationStep.Decoration getDecorationStage() {
			return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
		}
	};
	public static final TFLandmark YETI_CAVE = new TFLandmark( 2, "yeti_lairs", true, BiomeTagGenerator.VALID_YETI_CAVE_BIOMES, TerrainAdjustment.BURY, true, TwilightForestMod.prefix("progress_lich") ) {
		{
			this.enableDecorations().enableTerrainAlterations();
			this.undergroundDecoAllowed = false;

			this.addMonster(TFEntities.YETI.get(), 10, 4, 4);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.yeticave", 3);

			book.addTagElement("pages" , bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title" , StringTag.valueOf("Notes on an Icy Cave"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new YetiCaveComponent(this, rand, 0, x, y, z);
		}
	};
	public static final TFLandmark ICE_TOWER = new TFLandmark( 2, "ice_tower", true, BiomeTagGenerator.VALID_AURORA_PALACE_BIOMES, TerrainAdjustment.NONE, TwilightForestMod.prefix("progress_yeti") ) {
		{
			this.addMonster(TFEntities.SNOW_GUARDIAN.get(), 10, 4, 4)
					.addMonster(TFEntities.STABLE_ICE_CORE.get(), 10, 4, 4)
					.addMonster(TFEntities.UNSTABLE_ICE_CORE.get(), 5, 4, 4);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.icetower", 3);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf("Notes on Auroral Fortification"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new IceTowerMainComponent(this, rand, 0, x, y, z);
		}
	};
	// TODO split cloud giants from this
	public static final TFLandmark TROLL_CAVE = new TFLandmark( 4, "troll_lairs", true, BiomeTagGenerator.VALID_TROLL_CAVE_BIOMES, TerrainAdjustment.BURY, TwilightForestMod.prefix("progress_merge") ) {
		{
			this.enableDecorations().enableTerrainAlterations().disableProtectionAura();

			this.addMonster(EntityType.CREEPER, 5, 4, 4)
					.addMonster(EntityType.SKELETON, 10, 4, 4)
					.addMonster(TFEntities.TROLL.get(), 20, 4, 4)
					.addMonster(EntityType.WITCH, 5, 1, 1)
					// cloud monsters
					.addMonster(1, TFEntities.GIANT_MINER.get(), 10, 1, 1)
					.addMonster(1, TFEntities.ARMORED_GIANT.get(), 10, 1, 1);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.trollcave", 3);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf("Notes on the Highlands"));
		}

		@Override
		public GenerationStep.Decoration getDecorationStage() {
			return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new TrollCaveMainComponent(TFStructurePieceTypes.TFTCMai.get(), this, 0, x, y, z);
		}
	};
	public static final TFLandmark FINAL_CASTLE = new TFLandmark( 4, "final_castle", true, BiomeTagGenerator.VALID_FINAL_CASTLE_BIOMES, TerrainAdjustment.BEARD_THIN, TwilightForestMod.prefix("progress_troll") ) {
		{
			// plain parts of the castle, like the tower maze
			this.addMonster(TFEntities.KOBOLD.get(), 10, 4, 4)
					.addMonster(TFEntities.ADHERENT.get(), 10, 1, 1)
					.addMonster(TFEntities.HARBINGER_CUBE.get(), 10, 1, 1)
					.addMonster(EntityType.ENDERMAN, 10, 1, 1)
					// internal castle
					.addMonster(1, TFEntities.KOBOLD.get(), 10, 4, 4)
					.addMonster(1, TFEntities.ADHERENT.get(), 10, 1, 1)
					.addMonster(1, TFEntities.HARBINGER_CUBE.get(), 10, 1, 1)
					.addMonster(1, TFEntities.ARMORED_GIANT.get(), 10, 1, 1)
					// dungeons
					.addMonster(2, TFEntities.ADHERENT.get(), 10, 1, 1)
					// forge
					.addMonster(3, EntityType.BLAZE, 10, 1, 1);
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new FinalCastleMainComponent(this, rand, 0, x, y, z);
		}
	};
	public static final TFLandmark MUSHROOM_TOWER = new TFLandmark( 2, "mushroom_tower", true, BiomeTagGenerator.VALID_MUSHROOM_TOWER_BIOMES, TerrainAdjustment.NONE ) {
		{
			// FIXME Incomplete
			this.disableStructure();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new MushroomTowerMainComponent(this, rand, 0, x, y, z);
		}
	};
	public static final TFLandmark QUEST_ISLAND = new TFLandmark( 1, "quest_island", false, BiomeTagGenerator.VALID_QUEST_GROVE_BIOMES, TerrainAdjustment.NONE ) { { this.disableStructure(); } };
	//public static final TFFeature DRUID_GROVE    = new TFFeature( 1, "druid_grove"   , false ) { { this.disableStructure(); } };
	//public static final TFFeature FLOATING_RUINS = new TFFeature( 3, "floating_ruins", false ) { { this.disableStructure(); } };
	//public static final TFFeature WORLD_TREE = new TFFeature( 3, "world_tree", false ) { { this.disableStructure(); } };

	private static final Map<ResourceLocation, TFLandmark> BIOME_FEATURES = new ImmutableMap.Builder<ResourceLocation, TFLandmark>()
		.put(BiomeKeys.DARK_FOREST.location(), KNIGHT_STRONGHOLD)
		.put(BiomeKeys.DARK_FOREST_CENTER.location(), DARK_TOWER)
		//.put(BiomeKeys.DENSE_MUSHROOM_FOREST.location(), MUSHROOM_TOWER)
		.put(BiomeKeys.ENCHANTED_FOREST.location(), QUEST_GROVE)
		.put(BiomeKeys.FINAL_PLATEAU.location(), FINAL_CASTLE)
		.put(BiomeKeys.FIRE_SWAMP.location(), HYDRA_LAIR)
		.put(BiomeKeys.GLACIER.location(), ICE_TOWER)
		.put(BiomeKeys.HIGHLANDS.location(), TROLL_CAVE)
		.put(BiomeKeys.SNOWY_FOREST.location(), YETI_CAVE)
		.put(BiomeKeys.SWAMP.location(), LABYRINTH)
		.put(BiomeKeys.LAKE.location(), QUEST_ISLAND)
		.build();

	public final int size;
	public final String name;
	public final boolean centerBounds;
	protected boolean surfaceDecorationsAllowed = false;
	protected boolean undergroundDecoAllowed = true;
	public boolean isStructureEnabled = true;
	public boolean requiresTerraforming = false; // TODO Terraforming Type? Envelopment vs Flattening maybe?
	private final TagKey<Biome> biomeTag;
	private final TerrainAdjustment beardifierContribution;
	private final ImmutableList<ResourceLocation> requiredAdvancements;
	public boolean hasProtectionAura = true;
	protected boolean adjustToTerrainHeight = false;

	private static int maxPossibleSize;

	private final List<List<MobSpawnSettings.SpawnerData>> spawnableMonsterLists = new ArrayList<>();
	private final List<MobSpawnSettings.SpawnerData> ambientCreatureList = new ArrayList<>();
	private final List<MobSpawnSettings.SpawnerData> waterCreatureList = new ArrayList<>();

	private long lastSpawnedHintMonsterTime;

	TFLandmark(int size, String name, boolean featureGenerator, TagKey<Biome> biomeTag, TerrainAdjustment beardifierContribution, ResourceLocation... requiredAdvancements) {
		this(size, name, featureGenerator, biomeTag, beardifierContribution, false, requiredAdvancements);
	}

	TFLandmark(int size, String name, boolean featureGenerator, TagKey<Biome> biomeTag, TerrainAdjustment beardifierContribution, boolean centerBounds, ResourceLocation... requiredAdvancements) {
		this.size = size;
		this.name = name;
		this.biomeTag = biomeTag;
		this.beardifierContribution = beardifierContribution;

		this.requiredAdvancements = ImmutableList.copyOf(requiredAdvancements);

		this.centerBounds = centerBounds;

		maxPossibleSize = Math.max(this.size, maxPossibleSize);
	}

	public static int getMaxSize() {
		return maxPossibleSize;
	}

	@Override
	public boolean isSurfaceDecorationsAllowed() {
		return this.surfaceDecorationsAllowed;
	}

	@Override
	public boolean isUndergroundDecoAllowed() {
		return this.undergroundDecoAllowed;
	}

	@Override
	public boolean shouldAdjustToTerrain() {
		return this.adjustToTerrainHeight;
	}

	public static TFLandmark getFeatureAt(int regionX, int regionZ, WorldGenLevel world) {
		return generateFeature(regionX >> 4, regionZ >> 4, world);
	}

	public static boolean isInFeatureChunk(int regionX, int regionZ) {
		int chunkX = regionX >> 4;
		int chunkZ = regionZ >> 4;
		BlockPos cc = getNearestCenterXYZ(chunkX, chunkZ);

		return chunkX == (cc.getX() >> 4) && chunkZ == (cc.getZ() >> 4);
	}

	/**
	 * Turns on biome-specific decorations like grass and trees near this feature.
	 */
	public TFLandmark enableDecorations() {
		this.surfaceDecorationsAllowed = true;
		return this;
	}

	/**
	 * Tell the chunkgenerator that we don't have an associated structure.
	 */
	public TFLandmark disableStructure() {
		this.enableDecorations();
		this.isStructureEnabled = false;
		return this;
	}

	/**
	 * Tell the chunkgenerator that we want the terrain changed nearby.
	 */
	public TFLandmark enableTerrainAlterations() {
		this.requiresTerraforming = true;
		return this;
	}

	public TFLandmark disableProtectionAura() {
		this.hasProtectionAura = false;
		return this;
	}

	/**
	 * Add a monster to spawn list 0
	 */
	public TFLandmark addMonster(EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
		this.addMonster(0, monsterClass, weight, minGroup, maxGroup);
		return this;
	}

	/**
	 * Add a monster to a specific spawn list
	 */
	public TFLandmark addMonster(int listIndex, EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
		List<MobSpawnSettings.SpawnerData> monsterList;
		if (this.spawnableMonsterLists.size() > listIndex) {
			monsterList = this.spawnableMonsterLists.get(listIndex);
		} else {
			monsterList = new ArrayList<>();
			this.spawnableMonsterLists.add(listIndex, monsterList);
		}

		monsterList.add(new MobSpawnSettings.SpawnerData(monsterClass, weight, minGroup, maxGroup));
		return this;
	}

	/**
	 * Add a water creature
	 */
	public TFLandmark addWaterCreature(EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
		this.waterCreatureList.add(new MobSpawnSettings.SpawnerData(monsterClass, weight, minGroup, maxGroup));
		return this;
	}

	/**
	 * @return The type of feature directly at the specified Chunk coordinates
	 */
	public static TFLandmark getFeatureDirectlyAt(int chunkX, int chunkZ, WorldGenLevel world) {
		if (isInFeatureChunk(chunkX << 4, chunkZ << 4)) {
			return getFeatureAt(chunkX << 4, chunkZ << 4, world);
		}
		return NOTHING;
	}

	/**
	 * What feature would go in this chunk.  Called when we know there is a feature, but there is no cache data,
	 * either generating this chunk for the first time, or using the magic map to forecast beyond the edge of the world.
	 */
	public static TFLandmark generateFeature(int chunkX, int chunkZ, WorldGenLevel world) {
		// set the chunkX and chunkZ to the center of the biome
		chunkX = Math.round(chunkX / 16F) * 16;
		chunkZ = Math.round(chunkZ / 16F) * 16;

		// what biome is at the center of the chunk?
		Biome biomeAt = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8)).value();
		return generateFeature(world.registryAccess(), chunkX, chunkZ, biomeAt, world.getSeed(), world.registryAccess());
	}

	public static TFLandmark generateFeature(RegistryAccess access, int chunkX, int chunkZ, Biome biome, long seed, RegistryAccess registryAccess) {
		// Remove block comment start-marker to enable debug
		/*if (true) {
			return LICH_TOWER;
		}*/

		// set the chunkX and chunkZ to the center of the biome in case they arent already
		chunkX = Math.round(chunkX / 16F) * 16;
		chunkZ = Math.round(chunkZ / 16F) * 16;

		// does the biome have a feature?
		TFLandmark biomeFeature = BIOME_FEATURES.get(access.ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getKey(biome));

		if(biomeFeature != null)
			return biomeFeature;

		int regionOffsetX = Math.abs((chunkX + 64 >> 4) % 8);
		int regionOffsetZ = Math.abs((chunkZ + 64 >> 4) % 8);

		// plant two lich towers near the center of each 2048x2048 map area
		if ((regionOffsetX == 4 && regionOffsetZ == 5) || (regionOffsetX == 4 && regionOffsetZ == 3)) {
			return LICH_TOWER;
		}

		// also two nagas
		if ((regionOffsetX == 5 && regionOffsetZ == 4) || (regionOffsetX == 3 && regionOffsetZ == 4)) {
			return NAGA_COURTYARD;
		}

		// get random value
		// okay, well that takes care of most special cases
		return switch (new Random(seed + chunkX * 25117L + chunkZ * 151121L).nextInt(16)) {
			case 6, 7, 8 -> MEDIUM_HILL;
			case 9 -> LARGE_HILL;
			case 10, 11 -> HEDGE_MAZE;
			case 12, 13 -> NAGA_COURTYARD;
			case 14, 15 -> LICH_TOWER;
			default -> SMALL_HILL;
		};
	}

	/**
	 * Returns the feature nearest to the specified chunk coordinates.
	 */
	public static TFLandmark getNearestFeature(int cx, int cz, WorldGenLevel world) {
		return getNearestFeature(cx, cz, world, null);
	}

	/**
	 * Returns the feature nearest to the specified chunk coordinates.
	 *
	 * If a non-null {@code center} is provided and a valid feature is found,
	 * it will be set to relative block coordinates indicating the center of
	 * that feature relative to the current chunk block coordinate system.
	 */
	public static TFLandmark getNearestFeature(int cx, int cz, WorldGenLevel world, @Nullable IntPair center) {

		int maxSize = getMaxSize();
		int diam = maxSize * 2 + 1;
		TFLandmark[] features = new TFLandmark[diam * diam];

		for (int rad = 1; rad <= maxSize; rad++) {
			for (int x = -rad; x <= rad; x++) {
				for (int z = -rad; z <= rad; z++) {

					int idx = (x + maxSize) * diam + (z + maxSize);
					TFLandmark directlyAt = features[idx];
					if (directlyAt == null) {
						features[idx] = directlyAt = getFeatureDirectlyAt(x + cx, z + cz, world);
					}

					if (directlyAt.size == rad) {
						if (center != null) {
							center.x = (x << 4) + 8;
							center.z = (z << 4) + 8;
						}
						return directlyAt;
					}
				}
			}
		}

		return NOTHING;
	}

	// [Vanilla Copy] from MapGenStructure#findNearestStructurePosBySpacing; changed 2nd param to be TFFeature instead of MapGenStructure
	//TODO: Second parameter doesn't exist in Structure.findNearest
	@Nullable
	public static BlockPos findNearestFeaturePosBySpacing(WorldGenLevel worldIn, TFLandmark feature, BlockPos blockPos, int p_191069_3_, int p_191069_4_, int p_191069_5_, boolean p_191069_6_, int p_191069_7_, boolean findUnexplored) {
		int i = blockPos.getX() >> 4;
		int j = blockPos.getZ() >> 4;
		int k = 0;

		for (Random random = new Random(); k <= p_191069_7_; ++k) {
			for (int l = -k; l <= k; ++l) {
				boolean flag = l == -k || l == k;

				for (int i1 = -k; i1 <= k; ++i1) {
					boolean flag1 = i1 == -k || i1 == k;

					if (flag || flag1) {
						int j1 = i + p_191069_3_ * l;
						int k1 = j + p_191069_3_ * i1;

						if (j1 < 0) {
							j1 -= p_191069_3_ - 1;
						}

						if (k1 < 0) {
							k1 -= p_191069_3_ - 1;
						}

						int l1 = j1 / p_191069_3_;
						int i2 = k1 / p_191069_3_;
//						Random random1 = worldIn.setRandomSeed(l1, i2, p_191069_5_);
						Random random1 = new Random();
						l1 = l1 * p_191069_3_;
						i2 = i2 * p_191069_3_;

						if (p_191069_6_) {
							l1 = l1 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
							i2 = i2 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
						} else {
							l1 = l1 + random1.nextInt(p_191069_3_ - p_191069_4_);
							i2 = i2 + random1.nextInt(p_191069_3_ - p_191069_4_);
						}

						//MapGenBase.setupChunkSeed(worldIn.getSeed(), random, l1, i2);
						random.nextInt();

						// Check changed for TFFeature
						if (getFeatureAt(l1 << 4, i2 << 4, worldIn.getLevel()) == feature) {
							if (!findUnexplored || !worldIn.hasChunk(l1, i2)) {
								return new BlockPos((l1 << 4) + 8, 64, (i2 << 4) + 8);
							}
						} else if (k == 0) {
							break;
						}
					}
				}

				if (k == 0) {
					break;
				}
			}
		}

		return null;
	}

	/**
	 * @return The feature in the chunk "region"
	 */
	public static TFLandmark getFeatureForRegion(int chunkX, int chunkZ, WorldGenLevel world) {
		//just round to the nearest multiple of 16 chunks?
		int featureX = Math.round(chunkX / 16F) * 16;
		int featureZ = Math.round(chunkZ / 16F) * 16;

		return generateFeature(featureX, featureZ, world);
	}

	/**
	 * @return The feature in the chunk "region"
	 */
	public static TFLandmark getFeatureForRegionPos(int posX, int posZ, WorldGenLevel world) {
		return getFeatureForRegion(posX >> 4, posZ >> 4, world);
	}

	/**
	 * Given some coordinates, return the center of the nearest feature.
	 * <p>
	 * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
	 * <p>
	 * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
	 */
	public static BlockPos getNearestCenterXYZ(int chunkX, int chunkZ) {
		// generate random number for the whole biome area
		int regionX = (chunkX + 8) >> 4;
		int regionZ = (chunkZ + 8) >> 4;

		long seed = regionX * 3129871 ^ regionZ * 116129781L;
		seed = seed * seed * 42317861L + seed * 7L;

		int num0 = (int) (seed >> 12 & 3L);
		int num1 = (int) (seed >> 15 & 3L);
		int num2 = (int) (seed >> 18 & 3L);
		int num3 = (int) (seed >> 21 & 3L);

		// slightly randomize center of biome (+/- 3)
		int centerX = 8 + num0 - num1;
		int centerZ = 8 + num2 - num3;

		// centers are offset strangely depending on +/-
		int ccz;
		if (regionZ >= 0) {
			ccz = (regionZ * 16 + centerZ - 8) * 16 + 8;
		} else {
			ccz = (regionZ * 16 + (16 - centerZ) - 8) * 16 + 9;
		}

		int ccx;
		if (regionX >= 0) {
			ccx = (regionX * 16 + centerX - 8) * 16 + 8;
		} else {
			ccx = (regionX * 16 + (16 - centerX) - 8) * 16 + 9;
		}

		return new BlockPos(ccx, TFGenerationSettings.SEALEVEL, ccz);//  Math.abs(chunkX % 16) == centerX && Math.abs(chunkZ % 16) == centerZ; FIXME (set sea level hard)
	}

	public TagKey<Biome> getBiomeTag() {
		return this.biomeTag;
	}

	public TerrainAdjustment getBeardifierContribution() {
		return this.beardifierContribution;
	}

	@Override
	public List<MobSpawnSettings.SpawnerData> getCombinedMonsterSpawnableList() {
		List<MobSpawnSettings.SpawnerData> list = new ArrayList<>();
		spawnableMonsterLists.forEach(l -> {
			if(l != null)
				list.addAll(l);
		});
		return list;
	}

	@Override
	public List<MobSpawnSettings.SpawnerData> getCombinedCreatureSpawnableList() {
		List<MobSpawnSettings.SpawnerData> list = new ArrayList<>();
		list.addAll(ambientCreatureList);
		list.addAll(waterCreatureList);
		return list;
	}

	/**
	 * Returns a list of hostile monsters.  Are we ever going to need passive or water creatures?
	 */
	@Override
	public List<MobSpawnSettings.SpawnerData> getSpawnableList(MobCategory creatureType) {
		return switch (creatureType) {
			case MONSTER -> this.getSpawnableMonsterList(0);
			case AMBIENT -> this.ambientCreatureList;
			case WATER_CREATURE -> this.waterCreatureList;
			default -> List.of();
		};
	}

	/**
	 * Returns a list of hostile monsters in the specified indexed category
	 */
	@Override
	public List<MobSpawnSettings.SpawnerData> getSpawnableMonsterList(int index) {
		if (index >= 0 && index < this.spawnableMonsterLists.size()) {
			return this.spawnableMonsterLists.get(index);
		}
		return new ArrayList<>();
	}

	public List<List<MobSpawnSettings.SpawnerData>> getSpawnableMonsterLists() {
		return this.spawnableMonsterLists;
	}

	public List<MobSpawnSettings.SpawnerData> getAmbientCreatureList() {
		return this.ambientCreatureList;
	}

	public List<MobSpawnSettings.SpawnerData> getWaterCreatureList() {
		return this.waterCreatureList;
	}

	@Override
	public List<ResourceLocation> getRequiredAdvancements() {
		return this.requiredAdvancements;
	}

	/**
	 * Try several times to spawn a hint monster
	 */
	@Override
	public void trySpawnHintMonster(Level world, Player player, BlockPos pos) {
		// check if the timer is valid
		long currentTime = world.getGameTime();

		// if someone set the time backwards, fix the spawn timer
		if (currentTime < this.lastSpawnedHintMonsterTime) {
			this.lastSpawnedHintMonsterTime = 0;
		}

		if (currentTime - this.lastSpawnedHintMonsterTime > 1200) {
			// okay, time is good, try several times to spawn one
			for (int i = 0; i < 20; i++) {
				if (didSpawnHintMonster(world, player, pos)) {
					this.lastSpawnedHintMonsterTime = currentTime;
					break;
				}
			}
		}
	}

	@Nullable
	public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
		return null;
	}

	public Optional<Structure.GenerationStub> generateStub(Structure.GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		if (!TFLandmark.isInFeatureChunk(chunkPos.x << 4, chunkPos.z << 4))
			return Optional.empty();
		boolean dontCenter = this == TFLandmark.LICH_TOWER || this == TFLandmark.TROLL_CAVE || this == TFLandmark.YETI_CAVE;
		int x = (chunkPos.x << 4) + (dontCenter ? 0 : 7);
		int z = (chunkPos.z << 4) + (dontCenter ? 0 : 7);
		int y = shouldAdjustToTerrain() ? Mth.clamp(context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()), context.chunkGenerator().getSeaLevel() + 1, context.chunkGenerator().getSeaLevel() + 7) : context.chunkGenerator().getSeaLevel();
		Holder<Biome> holder = context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), Climate.empty());
		if (this != generateFeature(context.registryAccess(), chunkPos.x, chunkPos.z, holder.value(), context.seed(), context.registryAccess()))
			return Optional.empty();
		return Optional.ofNullable(this.provideFirstPiece(context.structureTemplateManager(), context.chunkGenerator(), RandomSource.create(context.seed() + chunkPos.x * 25117L + chunkPos.z * 151121L), x, y, z)).map(piece -> this.getStructurePieceGenerationStubFunction(piece, context, x, y, z));
	}

	@NotNull
	private Structure.GenerationStub getStructurePieceGenerationStubFunction(StructurePiece startingPiece, Structure.GenerationContext context, int x, int y, int z) {
		return new Structure.GenerationStub(new BlockPos(x, y, z), structurePiecesBuilder -> {
			structurePiecesBuilder.addPiece(startingPiece);
			startingPiece.addChildren(startingPiece, structurePiecesBuilder, context.random());

			structurePiecesBuilder.pieces.stream()
					.filter(TFStructureComponentTemplate.class::isInstance)
					.map(TFStructureComponentTemplate.class::cast)
					.forEach(t -> t.LAZY_TEMPLATE_LOADER.run());
		});
	}

	public GenerationStep.Decoration getDecorationStage() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	public final BoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int spanX, int spanY, int spanZ, @Nullable Direction dir) {
		if(centerBounds) {
			x += (spanX + minX) / 4;
			y += (spanY + minY) / 4;
			z += (spanZ + minZ) / 4;
		}
		return switch (dir) {
			case WEST -> // '\001'
					new BoundingBox(x - spanZ + minZ, y + minY, z + minX, x + minZ, y + spanY + minY, z + spanX + minX);
			case NORTH -> // '\002'
					new BoundingBox(x - spanX - minX, y + minY, z - spanZ - minZ, x - minX, y + spanY + minY, z - minZ);
			case EAST -> // '\003'
					new BoundingBox(x + minZ, y + minY, z - spanX, x + spanZ + minZ, y + spanY + minY, z + minX);
			default -> // '\0'
					new BoundingBox(x + minX, y + minY, z + minZ, x + spanX + minX, y + spanY + minY, z + spanZ + minZ);
		};
	}

	public static boolean isTheseFeatures(TFLandmark feature, TFLandmark... predicates) {
		for (TFLandmark predicate : predicates)
			if (feature == predicate)
				return true;
		return false;
	}

	private static final ImmutableMap<String, TFLandmark> NAME_2_TYPE = Util.make(() -> ImmutableMap.<String, TFLandmark>builder()
			.put("small_hollow_hill", TFLandmark.SMALL_HILL)
			.put("medium_hollow_hill", TFLandmark.MEDIUM_HILL)
			.put("large_hollow_hill", TFLandmark.LARGE_HILL)
			.put("hedge_maze", TFLandmark.HEDGE_MAZE)
			.put("quest_grove", TFLandmark.QUEST_GROVE)
			.put("naga_courtyard", TFLandmark.NAGA_COURTYARD)
			.put("lich_tower", TFLandmark.LICH_TOWER)
			.put("hydra_lair", TFLandmark.HYDRA_LAIR)
			.put("labyrinth", TFLandmark.LABYRINTH)
			.put("dark_tower", TFLandmark.DARK_TOWER)
			.put("knight_stronghold", TFLandmark.KNIGHT_STRONGHOLD)
			.put("yeti_lairs", TFLandmark.YETI_CAVE)
			.put("ice_tower", TFLandmark.ICE_TOWER)
			.put("troll_lairs", TFLandmark.TROLL_CAVE)
			.put("final_castle", TFLandmark.FINAL_CASTLE)
			.put("mushroom_tower", TFLandmark.MUSHROOM_TOWER)
			.build());

	public static final Codec<TFLandmark> CODEC = Codec.STRING.comapFlatMap(
			name -> TFLandmark.NAME_2_TYPE.containsKey(name) ? DataResult.success(TFLandmark.NAME_2_TYPE.get(name)) : DataResult.error("Landmark " + name + " not recognized!"),
			tfFeature -> tfFeature.name
	);
}
