package twilightforest.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.biome.Biome;
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
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
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
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

			this.addMonster(TFEntities.REDCAP.get(), 10, 1, 2)
					.addMonster(TFEntities.REDCAP_SAPPER.get(), 1, 1, 2)
					.addMonster(TFEntities.KOBOLD.get(), 10, 2, 4)
					.addMonster(EntityType.SKELETON, 10, 2, 3)
					.addMonster(TFEntities.SWARM_SPIDER.get(), 10, 2, 4)
					.addMonster(EntityType.SPIDER, 10, 1, 3)
					.addMonster(EntityType.CREEPER, 10, 1, 2)
					.addMonster(TFEntities.FIRE_BEETLE.get(), 5, 1, 1)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 5, 1, 1)
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

			this.addMonster(TFEntities.REDCAP.get(), 10, 2, 4)
					.addMonster(TFEntities.REDCAP_SAPPER.get(), 2, 1, 2)
					.addMonster(EntityType.SKELETON, 10, 2, 3)
					.addMonster(EntityType.CAVE_SPIDER, 10, 1, 2)
					.addMonster(EntityType.CREEPER, 10, 1, 1)
					.addMonster(EntityType.ENDERMAN, 1, 1, 1)
					.addMonster(TFEntities.WRAITH.get(), 2, 1, 2)
					.addMonster(TFEntities.FIRE_BEETLE.get(), 10, 1, 2)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 10, 1, 2)
					.addMonster(TFEntities.PINCH_BEETLE.get(), 10, 1, 2)
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
			return new HedgeMazeComponent(this, 0, x + 1, chunkGenerator.getSeaLevel() + 8, z + 1);
		}
	};
	public static final TFLandmark QUEST_GROVE = new TFLandmark( 1, "quest_grove" , true, BiomeTagGenerator.VALID_QUEST_GROVE_BIOMES, TerrainAdjustment.BEARD_THIN) {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new QuestGrove(structureManager, new BlockPos(x - 12, chunkGenerator.getSeaLevel() + 5, z - 12));
		}
	};
	public static final TFLandmark NAGA_COURTYARD = new TFLandmark( 3, "naga_courtyard", true, BiomeTagGenerator.VALID_NAGA_COURTYARD_BIOMES, TerrainAdjustment.BEARD_THIN) {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new CourtyardMain(this, rand, 0, x + 1, chunkGenerator.getSeaLevel() + 5, z + 1, structureManager);
		}
	};
	public static final TFLandmark LICH_TOWER = new TFLandmark( 1, "lich_tower", true, BiomeTagGenerator.VALID_LICH_TOWER_BIOMES, TerrainAdjustment.BEARD_THIN, TwilightForestMod.prefix("progress_naga") ) {
		{
			this.addMonster(EntityType.ZOMBIE, 10, 1, 2)
					.addMonster(EntityType.SKELETON, 10, 1, 2)
					.addMonster(EntityType.CREEPER, 1, 1, 1)
					.addMonster(EntityType.ENDERMAN, 1, 1, 2)
					.addMonster(TFEntities.DEATH_TOME.get(), 10, 2, 3)
					.addMonster(EntityType.WITCH, 1, 1, 1);

			this.adjustToTerrainHeight = true;
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.lichtower", 4);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.lichtower"));
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
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.hydralair"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HydraLairComponent(this, 0, x - 7, y, z - 7);
		}
	};
	public static final TFLandmark LABYRINTH = new TFLandmark( 3, "labyrinth", true, BiomeTagGenerator.VALID_LABYRINTH_BIOMES, TerrainAdjustment.BURY, TwilightForestMod.prefix("progress_lich") ) {
		{
			this.enableDecorations();
			this.undergroundDecoAllowed = false;

			this.addMonster(TFEntities.MINOTAUR.get(), 20, 2, 3)
					.addMonster(EntityType.CAVE_SPIDER, 10, 1, 2)
					.addMonster(EntityType.CREEPER, 10, 1, 2)
					.addMonster(TFEntities.MAZE_SLIME.get(), 10, 2, 4)
					.addMonster(EntityType.ENDERMAN, 1, 1, 2)
					.addMonster(TFEntities.FIRE_BEETLE.get(), 10, 1, 2)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 10, 1, 2)
					.addMonster(TFEntities.PINCH_BEETLE.get(), 10, 1, 1);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.labyrinth", 5);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.labyrinth"));
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
			this.addMonster(TFEntities.CARMINITE_GOLEM.get(), 10, 1, 2)
					.addMonster(EntityType.SKELETON, 10, 1, 2)
					.addMonster(EntityType.CREEPER, 5, 1, 1)
					.addMonster(EntityType.ENDERMAN, 2, 1, 2)
					.addMonster(EntityType.WITCH, 1, 1, 1)
					.addMonster(TFEntities.CARMINITE_GHASTLING.get(), 10, 1, 2)
					.addMonster(TFEntities.CARMINITE_BROODLING.get(), 10, 4, 4)
					.addMonster(TFEntities.PINCH_BEETLE.get(), 10, 1, 1)
					// roof ghasts
					.addMonster(1, TFEntities.CARMINITE_GHASTGUARD.get(), 10, 1, 2)
					// aquarium squids (only in aquariums between y = 35 and y = 64. :/
					.addWaterCreature(EntityType.SQUID, 10, 4, 4);

			this.adjustToTerrainHeight = true;
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.darktower", 3);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.darktower"));
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

			this.addMonster(TFEntities.BLOCKCHAIN_GOBLIN.get(), 10, 1, 2)
					.addMonster(TFEntities.LOWER_GOBLIN_KNIGHT.get(), 5, 1, 2)
					.addMonster(TFEntities.HELMET_CRAB.get(), 10, 2, 4)
					.addMonster(TFEntities.SLIME_BEETLE.get(), 10, 2, 3)
					.addMonster(TFEntities.REDCAP_SAPPER.get(), 2, 1, 2)
					.addMonster(TFEntities.KOBOLD.get(), 10, 2, 4)
					.addMonster(EntityType.CREEPER, 5, 1, 2)
					.addMonster(EntityType.SLIME, 5, 4, 4);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.tfstronghold", 5);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.tfstronghold"));
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

			this.addMonster(TFEntities.YETI.get(), 5, 1, 2);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.yeticave", 3);

			book.addTagElement("pages" , bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.yeticave"));
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new YetiCaveComponent(this, 0, x, y, z);
		}
	};
	public static final TFLandmark ICE_TOWER = new TFLandmark( 2, "ice_tower", true, BiomeTagGenerator.VALID_AURORA_PALACE_BIOMES, TerrainAdjustment.NONE, TwilightForestMod.prefix("progress_yeti") ) {
		{
			this.addMonster(TFEntities.SNOW_GUARDIAN.get(), 10, 1, 2)
					.addMonster(TFEntities.STABLE_ICE_CORE.get(), 10, 1, 2)
					.addMonster(TFEntities.UNSTABLE_ICE_CORE.get(), 5, 1, 2);
		}

		@Override
		public void addBookInformation(ItemStack book, ListTag bookPages) {

			StructureHints.addTranslatedPages(bookPages, TwilightForestMod.ID + ".book.icetower", 3);

			book.addTagElement("pages", bookPages);
			book.addTagElement("author", StringTag.valueOf(BOOK_AUTHOR));
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.icetower"));
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

			this.addMonster(EntityType.CREEPER, 5, 1, 2)
					.addMonster(EntityType.SKELETON, 10, 1, 2)
					.addMonster(TFEntities.TROLL.get(), 20, 1, 2)
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
			book.addTagElement("title", StringTag.valueOf(TwilightForestMod.ID + ".book.trollcave"));
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
//		{
//			// plain parts of the castle, like the tower maze
//			this.addMonster(TFEntities.KOBOLD.get(), 10, 1, 2)
//					.addMonster(TFEntities.ADHERENT.get(), 10, 1, 1)
//					.addMonster(TFEntities.HARBINGER_CUBE.get(), 10, 1, 1)
//					.addMonster(EntityType.ENDERMAN, 10, 1, 1)
//					// internal castle
//					.addMonster(1, TFEntities.KOBOLD.get(), 10, 1, 2)
//					.addMonster(1, TFEntities.ADHERENT.get(), 10, 1, 1)
//					.addMonster(1, TFEntities.HARBINGER_CUBE.get(), 10, 1, 1)
//					.addMonster(1, TFEntities.ARMORED_GIANT.get(), 10, 1, 1)
//					// dungeons
//					.addMonster(2, TFEntities.ADHERENT.get(), 10, 1, 1)
//					// forge
//					.addMonster(3, EntityType.BLAZE, 10, 1, 1);
//		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new FinalCastleMainComponent(this, 0, x, y, z);
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

	private TFLandmark(int size, String name, boolean featureGenerator, @Nullable TagKey<Biome> biomeTag, TerrainAdjustment beardifierContribution, ResourceLocation... requiredAdvancements) {
		this(size, name, featureGenerator, biomeTag, beardifierContribution, false, requiredAdvancements);
	}

	private TFLandmark(int size, String name, boolean featureGenerator, TagKey<Biome> biomeTag, TerrainAdjustment beardifierContribution, boolean centerBounds, ResourceLocation... requiredAdvancements) {
		this.size = size;
		this.name = name;
		this.biomeTag = biomeTag;
		this.beardifierContribution = beardifierContribution;

		this.requiredAdvancements = ImmutableList.copyOf(requiredAdvancements);

		this.centerBounds = centerBounds;

		maxPossibleSize = Math.max(this.size, maxPossibleSize);
	}

	@Deprecated // Not good practice - TODO The root need for this method can be fixed
	public static int getMaxSearchSize() {
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

	@Nullable
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

		boolean dontCenter = this == TFLandmark.LICH_TOWER || this == TFLandmark.TROLL_CAVE || this == TFLandmark.YETI_CAVE;
		int x = (chunkPos.x << 4) + (dontCenter ? 0 : 7);
		int z = (chunkPos.z << 4) + (dontCenter ? 0 : 7);
		int y = this.shouldAdjustToTerrain()
				? Mth.clamp(context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()), context.chunkGenerator().getSeaLevel() + 1, context.chunkGenerator().getSeaLevel() + 7)
				: context.chunkGenerator().getSeaLevel();

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

	private static final ImmutableMap<String, TFLandmark> NAME_2_TYPE = Util.make(() -> ImmutableMap.<String, TFLandmark>builder()
			.put("mushroom_tower", TFLandmark.MUSHROOM_TOWER)
			.put("small_hollow_hill", TFLandmark.SMALL_HILL)
			.put("medium_hollow_hill", TFLandmark.MEDIUM_HILL)
			.put("large_hollow_hill", TFLandmark.LARGE_HILL)
			.put("hedge_maze", TFLandmark.HEDGE_MAZE)
			.put("quest_grove", TFLandmark.QUEST_GROVE)
			.put("quest_island", TFLandmark.QUEST_ISLAND)
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
			.build());

	public static final Codec<TFLandmark> CODEC = Codec.STRING.comapFlatMap(
			name -> TFLandmark.NAME_2_TYPE.containsKey(name) ? DataResult.success(TFLandmark.NAME_2_TYPE.get(name)) : DataResult.error("Landmark " + name + " not recognized!"),
			tfFeature -> tfFeature.name
	);

	@Override
	public String toString() {
		return "TFLandmark{" +
				"name='" + name + '\'' +
				//", centerBounds=" + centerBounds +
				//", surfaceDecorationsAllowed=" + surfaceDecorationsAllowed +
				//", undergroundDecoAllowed=" + undergroundDecoAllowed +
				//", isStructureEnabled=" + isStructureEnabled +
				//", requiresTerraforming=" + requiresTerraforming +
				//", biomeTag=" + biomeTag +
				//", beardifierContribution=" + beardifierContribution +
				//", requiredAdvancements=" + requiredAdvancements +
				//", hasProtectionAura=" + hasProtectionAura +
				//", adjustToTerrainHeight=" + adjustToTerrainHeight +
				//", spawnableMonsterLists=" + spawnableMonsterLists +
				//", ambientCreatureList=" + ambientCreatureList +
				//", waterCreatureList=" + waterCreatureList +
				//", lastSpawnedHintMonsterTime=" + lastSpawnedHintMonsterTime +
				'}';
	}
}
