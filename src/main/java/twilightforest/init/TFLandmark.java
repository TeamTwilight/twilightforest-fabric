package twilightforest.init;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.ArrayList;
import java.util.List;

public class TFLandmark implements DecorationClearance, ControlledSpawns {
	public static final TFLandmark NOTHING = new TFLandmark( 0, "no_feature") { { this.enableDecorations().disableStructure(); } };
	public static final TFLandmark SMALL_HILL = new TFLandmark( 1, "small_hollow_hill", true) {
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
			return new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), 0, size, x - 3, y - 2, z - 3);
		}
	};
	public static final TFLandmark MEDIUM_HILL = new TFLandmark( 2, "medium_hollow_hill", true ) {
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
			return new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), 0, size, x - 7, y - 5, z - 7);
		}
	};
	public static final TFLandmark LARGE_HILL = new TFLandmark( 3, "large_hollow_hill", true ) {
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
			return new HollowHillComponent(TFStructurePieceTypes.TFHill.get(), 0, size, x - 11, y - 5, z - 11);
		}
	};
	public static final TFLandmark HEDGE_MAZE = new TFLandmark( 2, "hedge_maze") {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}
		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HedgeMazeComponent(0, x + 1, chunkGenerator.getSeaLevel() + 8, z + 1);
		}
	};
	public static final TFLandmark QUEST_GROVE = new TFLandmark( 1, "quest_grove") {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new QuestGrove(structureManager, new BlockPos(x - 12, chunkGenerator.getSeaLevel() + 5, z - 12));
		}
	};
	public static final TFLandmark NAGA_COURTYARD = new TFLandmark( 3, "naga_courtyard") {
		{
			this.enableTerrainAlterations();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new CourtyardMain(rand, 0, x + 1, chunkGenerator.getSeaLevel() + 5, z + 1, structureManager);
		}
	};
	public static final TFLandmark LICH_TOWER = new TFLandmark( 1, "lich_tower" ) {
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
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new TowerMainComponent(rand, 0, x, y, z);
		}
	};
	public static final TFLandmark HYDRA_LAIR = new TFLandmark( 2, "hydra_lair" ) {
		{
			this.enableTerrainAlterations();
			this.undergroundDecoAllowed = false;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new HydraLairComponent(0, x - 7, y, z - 7);
		}
	};
	public static final TFLandmark LABYRINTH = new TFLandmark( 3, "labyrinth" ) {
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
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new MazeRuinsComponent(0, x, y, z);
		}

	};
	public static final TFLandmark DARK_TOWER = new TFLandmark( 1, "dark_tower" ) {
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
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new DarkTowerMainComponent(rand, 0, x, y, z);
		}
	};
	public static final TFLandmark KNIGHT_STRONGHOLD = new TFLandmark( 3, "knight_stronghold" ) {
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
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new StrongholdEntranceComponent(0, x, y + 5, z);
		}

	};
	public static final TFLandmark YETI_CAVE = new TFLandmark( 2, "yeti_lairs" ) {
		{
			this.enableDecorations().enableTerrainAlterations();
			this.undergroundDecoAllowed = false;

			this.addMonster(TFEntities.YETI.get(), 5, 1, 2);
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new YetiCaveComponent(0, x, y, z);
		}
	};
	public static final TFLandmark ICE_TOWER = new TFLandmark( 2, "ice_tower" ) {
		{
			this.addMonster(TFEntities.SNOW_GUARDIAN.get(), 10, 1, 2)
					.addMonster(TFEntities.STABLE_ICE_CORE.get(), 10, 1, 2)
					.addMonster(TFEntities.UNSTABLE_ICE_CORE.get(), 5, 1, 2);
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new IceTowerMainComponent(rand, 0, x, y, z);
		}
	};
	// TODO split cloud giants from this
	public static final TFLandmark TROLL_CAVE = new TFLandmark( 4, "troll_lairs" ) {
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
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new TrollCaveMainComponent(TFStructurePieceTypes.TFTCMai.get(), 0, x, y, z);
		}
	};
	public static final TFLandmark FINAL_CASTLE = new TFLandmark( 4, "final_castle" ) {
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
			return new FinalCastleMainComponent(0, x, y, z);
		}
	};
	public static final TFLandmark MUSHROOM_TOWER = new TFLandmark( 2, "mushroom_tower") {
		{
			// FIXME Incomplete
			this.disableStructure();

			this.adjustToTerrainHeight = true;
		}

		@Override
		public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
			return new MushroomTowerMainComponent(rand, 0, x, y, z);
		}
	};
	public static final TFLandmark QUEST_ISLAND = new TFLandmark( 1, "quest_island") { { this.disableStructure(); } };
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
	public boolean hasProtectionAura = true;
	protected boolean adjustToTerrainHeight = false;

	private static int maxPossibleSize;

	private final List<List<MobSpawnSettings.SpawnerData>> spawnableMonsterLists = new ArrayList<>();
	private final List<MobSpawnSettings.SpawnerData> ambientCreatureList = new ArrayList<>();
	private final List<MobSpawnSettings.SpawnerData> waterCreatureList = new ArrayList<>();

	private TFLandmark(int size, String name) {
		this(size, name, false);
	}

	private TFLandmark(int size, String name, boolean centerBounds) {
		this.size = size;
		this.name = name;

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

	@Deprecated // TODO Deleting this method will break maps - best to wait until new MC version before committing to it.
	@Nullable
	public StructurePiece provideFirstPiece(StructureTemplateManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, int x, int y, int z) {
		return null;
	}

	@Deprecated
	public final BoundingBox getComponentToAddBoundingBoxDELETE(int x, int y, int z, int minX, int minY, int minZ, int spanX, int spanY, int spanZ, @Nullable Direction dir) {
		return getComponentToAddBoundingBox(x, y, z, minX, minY, minZ, spanX, spanY, spanZ, dir, this.centerBounds);
	}

	@NotNull
	public static BoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int spanX, int spanY, int spanZ, @Nullable Direction dir, boolean centerBounds) {
		// CenterBounds is true for ONLY Hollow Hills, Hydra Lair, & Yeti Caves
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
