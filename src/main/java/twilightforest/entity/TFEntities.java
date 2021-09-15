package twilightforest.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import twilightforest.client.renderer.entity.*;
import twilightforest.entity.boss.*;
import twilightforest.entity.passive.*;
import twilightforest.entity.projectile.*;
import twilightforest.item.TFItems;
import twilightforest.item.TransformPowderItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.levelgen.Heightmap;

public class TFEntities {

	public static final SpawnPlacements.Type ON_ICE = SpawnPlacements.Type.ON_GROUND;//SpawnPlacements.Type.valueOf("TF_ON_ICE");

	public static final SpawnPlacements.Type CLOUDS = SpawnPlacements.Type.ON_GROUND;//SpawnPlacements.Type.valueOf("CLOUD_DWELLERS");

	private static final List<EntityType<?>> ALL = new ArrayList<>();
	public static final EntityType<BoarEntity> wild_boar = make(TFEntityNames.WILD_BOAR, BoarEntity::new, MobCategory.CREATURE, 0.9F, 0.9F);
	public static final EntityType<BighornEntity> bighorn_sheep = make(TFEntityNames.BIGHORN_SHEEP, BighornEntity::new, MobCategory.CREATURE, 0.9F, 1.3F);
	public static final EntityType<DeerEntity> deer = make(TFEntityNames.DEER, DeerEntity::new, MobCategory.CREATURE, 0.7F, 1.8F);
	public static final EntityType<RedcapEntity> redcap = make(TFEntityNames.REDCAP, RedcapEntity::new, MobCategory.MONSTER, 0.9F, 1.4F);
	public static final EntityType<SwarmSpiderEntity> swarm_spider = make(TFEntityNames.SWARM_SPIDER, SwarmSpiderEntity::new, MobCategory.MONSTER, 0.8F, 0.4F);
	public static final EntityType<NagaEntity> naga = make(TFEntityNames.NAGA, NagaEntity::new, MobCategory.MONSTER, 1.75f, 3.0f);
	public static final EntityType<SkeletonDruidEntity> skeleton_druid = make(TFEntityNames.SKELETON_DRUID, SkeletonDruidEntity::new, MobCategory.MONSTER, 0.6F, 1.99F);
	public static final EntityType<HostileWolfEntity> hostile_wolf = make(TFEntityNames.HOSTILE_WOLF, HostileWolfEntity::new, MobCategory.MONSTER, 0.6F, 0.85F);
	public static final EntityType<WraithEntity> wraith = make(TFEntityNames.WRAITH, WraithEntity::new, MobCategory.MONSTER);
	public static final EntityType<HedgeSpiderEntity> hedge_spider = make(TFEntityNames.HEDGE_SPIDER, HedgeSpiderEntity::new, MobCategory.MONSTER, 1.4F, 0.9F);
	public static final EntityType<HydraEntity> hydra = make(TFEntityNames.HYDRA, HydraEntity::new, MobCategory.MONSTER, 16F, 12F);
	public static final EntityType<LichEntity> lich = make(TFEntityNames.LICH, LichEntity::new, MobCategory.MONSTER, 1.1F, 2.1F);
	public static final EntityType<PenguinEntity> penguin = make(TFEntityNames.PENGUIN, PenguinEntity::new, MobCategory.CREATURE, 0.5F, 0.9F);
	public static final EntityType<LichMinionEntity> lich_minion = make(TFEntityNames.LICH_MINION, LichMinionEntity::new, MobCategory.MONSTER, 0.6F, 1.95F);
	public static final EntityType<LoyalZombieEntity> loyal_zombie = make(TFEntityNames.LOYAL_ZOMBIE, LoyalZombieEntity::new, MobCategory.MONSTER);
	public static final EntityType<TinyBirdEntity> tiny_bird = make(TFEntityNames.TINY_BIRD, TinyBirdEntity::new, MobCategory.CREATURE, 0.3F, 0.3F);
	public static final EntityType<SquirrelEntity> squirrel = make(TFEntityNames.SQUIRREL, SquirrelEntity::new, MobCategory.CREATURE, 0.3F, 0.5F);
	public static final EntityType<BunnyEntity> bunny = make(TFEntityNames.BUNNY, BunnyEntity::new, MobCategory.CREATURE, 0.3F, 0.6F);
	public static final EntityType<RavenEntity> raven = make(TFEntityNames.RAVEN, RavenEntity::new, MobCategory.CREATURE, 0.3F, 0.5F);
	public static final EntityType<QuestRamEntity> quest_ram = make(TFEntityNames.QUEST_RAM, QuestRamEntity::new, MobCategory.CREATURE, 1.25F, 2.9F);
	public static final EntityType<KoboldEntity> kobold = make(TFEntityNames.KOBOLD, KoboldEntity::new, MobCategory.MONSTER, 0.8F, 1.1F);
	public static final EntityType<MosquitoSwarmEntity> mosquito_swarm = make(TFEntityNames.MOSQUITO_SWARM, MosquitoSwarmEntity::new, MobCategory.MONSTER, 0.7F, 1.9F);
	public static final EntityType<DeathTomeEntity> death_tome = make(TFEntityNames.DEATH_TOME, DeathTomeEntity::new, MobCategory.MONSTER);
	public static final EntityType<MinotaurEntity> minotaur = make(TFEntityNames.MINOTAUR, MinotaurEntity::new, MobCategory.MONSTER, 0.6F, 2.1F);
	public static final EntityType<MinoshroomEntity> minoshroom = make(TFEntityNames.MINOSHROOM, MinoshroomEntity::new, MobCategory.MONSTER, 1.49F, 2.5F);
	public static final EntityType<FireBeetleEntity> fire_beetle = make(TFEntityNames.FIRE_BEETLE, FireBeetleEntity::new, MobCategory.MONSTER, 1.1F, 0.5F);
	public static final EntityType<SlimeBeetleEntity> slime_beetle = make(TFEntityNames.SLIME_BEETLE, SlimeBeetleEntity::new, MobCategory.MONSTER, 0.9F, 0.5F);
	public static final EntityType<PinchBeetleEntity> pinch_beetle = make(TFEntityNames.PINCH_BEETLE, PinchBeetleEntity::new, MobCategory.MONSTER, 1.2F, 0.5F);
	public static final EntityType<MazeSlimeEntity> maze_slime = make(TFEntityNames.MAZE_SLIME, MazeSlimeEntity::new, MobCategory.MONSTER, 2.04F, 2.04F);
	public static final EntityType<RedcapSapperEntity> redcap_sapper = make(TFEntityNames.REDCAP_SAPPER, RedcapSapperEntity::new, MobCategory.MONSTER, 0.9F, 1.4F);
	public static final EntityType<MistWolfEntity> mist_wolf = make(TFEntityNames.MIST_WOLF, MistWolfEntity::new, MobCategory.MONSTER, 1.4F, 1.9F);
	public static final EntityType<KingSpiderEntity> king_spider = make(TFEntityNames.KING_SPIDER, KingSpiderEntity::new, MobCategory.MONSTER, 1.6F, 1.6F);
	public static final EntityType<CarminiteGhastlingEntity> mini_ghast = make(TFEntityNames.MINI_GHAST, CarminiteGhastlingEntity::new, MobCategory.MONSTER, 1.1F, 1.5F);
	public static final EntityType<CarminiteGhastguardEntity> tower_ghast = make(TFEntityNames.TOWER_GHAST, CarminiteGhastguardEntity::new, MobCategory.MONSTER, 4.0F, 6.0F);
	public static final EntityType<CarminiteGolemEntity> tower_golem = make(TFEntityNames.TOWER_GOLEM, CarminiteGolemEntity::new, MobCategory.MONSTER, 1.4F, 2.9F);
	public static final EntityType<TowerwoodBorerEntity> tower_termite = make(TFEntityNames.TOWER_TERMITE, TowerwoodBorerEntity::new, MobCategory.MONSTER, 0.4F, 0.3F);
	public static final EntityType<TowerBroodlingEntity> tower_broodling = make(TFEntityNames.TOWER_BROODLING, TowerBroodlingEntity::new, MobCategory.MONSTER, 0.7F, 0.5F);
	public static final EntityType<UrGhastEntity> ur_ghast = make(TFEntityNames.UR_GHAST, UrGhastEntity::new, MobCategory.MONSTER, 14.0F, 18.0F);
	public static final EntityType<BlockChainGoblinEntity> blockchain_goblin = make(TFEntityNames.BLOCKCHAIN_GOBLIN, BlockChainGoblinEntity::new, MobCategory.MONSTER, 0.9F, 1.4F);
	public static final EntityType<UpperGoblinKnightEntity> goblin_knight_upper = make(TFEntityNames.GOBLIN_KNIGHT_UPPER, UpperGoblinKnightEntity::new, MobCategory.MONSTER, 1.1F, 1.3F);
	public static final EntityType<LowerGoblinKnightEntity> goblin_knight_lower = make(TFEntityNames.GOBLIN_KNIGHT_LOWER, LowerGoblinKnightEntity::new, MobCategory.MONSTER, 0.7F, 1.1F);
	public static final EntityType<HelmetCrabEntity> helmet_crab = make(TFEntityNames.HELMET_CRAB, HelmetCrabEntity::new, MobCategory.MONSTER, 0.8F, 1.1F);
	public static final EntityType<KnightPhantomEntity> knight_phantom = make(TFEntityNames.KNIGHT_PHANTOM, KnightPhantomEntity::new, MobCategory.MONSTER, 1.5F, 3.0F);
	public static final EntityType<YetiEntity> yeti = make(TFEntityNames.YETI, YetiEntity::new, MobCategory.MONSTER, 1.4F, 2.4F);
	public static final EntityType<AlphaYetiEntity> yeti_alpha = make(TFEntityNames.YETI_ALPHA, AlphaYetiEntity::new, MobCategory.MONSTER, 3.8F, 5.0F);
	public static final EntityType<WinterWolfEntity> winter_wolf = make(TFEntityNames.WINTER_WOLF, WinterWolfEntity::new, MobCategory.MONSTER, 1.4F, 1.9F);
	public static final EntityType<SnowGuardianEntity> snow_guardian = make(TFEntityNames.SNOW_GUARDIAN, SnowGuardianEntity::new, MobCategory.MONSTER, 0.6F, 1.8F);
	public static final EntityType<StableIceCoreEntity> stable_ice_core = make(TFEntityNames.STABLE_ICE_CORE, StableIceCoreEntity::new, MobCategory.MONSTER, 0.8F, 1.8F);
	public static final EntityType<UnstableIceCoreEntity> unstable_ice_core = make(TFEntityNames.UNSTABLE_ICE_CORE, UnstableIceCoreEntity::new, MobCategory.MONSTER, 0.8F, 1.8F);
	public static final EntityType<SnowQueenEntity> snow_queen = make(TFEntityNames.SNOW_QUEEN, SnowQueenEntity::new, MobCategory.MONSTER, 0.7F, 2.2F);
	public static final EntityType<TrollEntity> troll = make(TFEntityNames.TROLL, TrollEntity::new, MobCategory.MONSTER, 1.4F, 2.4F);
	public static final EntityType<GiantMinerEntity> giant_miner = make(TFEntityNames.GIANT_MINER, GiantMinerEntity::new, MobCategory.MONSTER, 2.4F, 7.2F);
	public static final EntityType<ArmoredGiantEntity> armored_giant = make(TFEntityNames.ARMORED_GIANT, ArmoredGiantEntity::new, MobCategory.MONSTER, 2.4F, 7.2F);
	public static final EntityType<IceCrystalEntity> ice_crystal = make(TFEntityNames.ICE_CRYSTAL, IceCrystalEntity::new, MobCategory.MONSTER, 0.6F, 1.8F);
	public static final EntityType<HarbingerCubeEntity> harbinger_cube = make(TFEntityNames.HARBINGER_CUBE, HarbingerCubeEntity::new, MobCategory.MONSTER, 1.9F, 2.4F);
	public static final EntityType<AdherentEntity> adherent = make(TFEntityNames.ADHERENT, AdherentEntity::new, MobCategory.MONSTER, 0.8F, 2.2F);
	public static final EntityType<RovingCubeEntity> roving_cube = make(TFEntityNames.ROVING_CUBE, RovingCubeEntity::new, MobCategory.MONSTER, 1.2F, 2.1F);
	//public static final EntityType<EntityTFCastleGuardian> castle_guardian = make(TFEntityNames.CASTLE_GUARDIAN, EntityTFCastleGuardian::new, EntityClassification.MONSTER, 1.8F, 2.4F);
	public static final EntityType<PlateauBossEntity> plateau_boss = make(TFEntityNames.PLATEAU_BOSS, PlateauBossEntity::new, MobCategory.MONSTER, 1F, 1F);

	public static final EntityType<NatureBoltEntity> nature_bolt = build(TFEntityNames.NATURE_BOLT, makeCastedBuilder(NatureBoltEntity.class, NatureBoltEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*//*.setUpdateInterval(5)*/);
	public static final EntityType<LichBoltEntity> lich_bolt = build(TFEntityNames.LICH_BOLT, makeCastedBuilder(LichBoltEntity.class, LichBoltEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*/*.setTrackingRange(150)*//*/*.setUpdateInterval(2)*/);
	public static final EntityType<TwilightWandBoltEntity> wand_bolt = build(TFEntityNames.WAND_BOLT, makeCastedBuilder(TwilightWandBoltEntity.class, TwilightWandBoltEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*//*.setUpdateInterval(5)*/);
	public static final EntityType<TomeBoltEntity> tome_bolt = build(TFEntityNames.TOME_BOLT, makeCastedBuilder(TomeBoltEntity.class, TomeBoltEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*//*.setUpdateInterval(5)*/);
	public static final EntityType<HydraMortarHead> hydra_mortar = build(TFEntityNames.HYDRA_MORTAR, makeCastedBuilder(HydraMortarHead.class, HydraMortarHead::new, MobCategory.MISC).sized(0.75F, 0.75F)/*.setTrackingRange(150)*/);
	public static final EntityType<LichBombEntity> lich_bomb = build(TFEntityNames.LICH_BOMB, makeCastedBuilder(LichBombEntity.class, LichBombEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*/);
	public static final EntityType<MoonwormShotEntity> moonworm_shot = build(TFEntityNames.MOONWORM_SHOT, makeCastedBuilder(MoonwormShotEntity.class, MoonwormShotEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*/);
	public static final EntityType<CicadaShotEntity> cicada_shot = build(TFEntityNames.CICADA_SHOT, makeCastedBuilder(CicadaShotEntity.class, CicadaShotEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*/);
	public static final EntityType<SlimeProjectileEntity> slime_blob = build(TFEntityNames.SLIME_BLOB, makeCastedBuilder(SlimeProjectileEntity.class, SlimeProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*/);
	public static final EntityType<CharmEffectEntity> charm_effect = make(TFEntityNames.CHARM_EFFECT, CharmEffectEntity::new, MobCategory.MISC, 0.25F, 0.25F);
	public static final EntityType<ThrownWepEntity> thrown_wep = make(TFEntityNames.THROWN_WEP, ThrownWepEntity::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final EntityType<FallingIceEntity> falling_ice = make(TFEntityNames.FALLING_ICE, FallingIceEntity::new, MobCategory.MISC, 2.98F, 2.98F);
	public static final EntityType<IceBombEntity> thrown_ice = build(TFEntityNames.THROWN_ICE, makeCastedBuilder(IceBombEntity.class, IceBombEntity::new, MobCategory.MISC).sized(1.0F, 1.0F)/*.setUpdateInterval(2)*/);
	public static final EntityType<SeekerArrowEntity> seeker_arrow = build(TFEntityNames.SEEKER_ARROW, makeCastedBuilder(SeekerArrowEntity.class, SeekerArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)/*.setTrackingRange(150)*//*.setUpdateInterval(1)*/);
	public static final EntityType<IceArrowEntity> ice_arrow = build(TFEntityNames.ICE_ARROW, makeCastedBuilder(IceArrowEntity.class, IceArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)/*.setTrackingRange(150)*//*.setUpdateInterval(1)*/);
	public static final EntityType<IceSnowballEntity> ice_snowball = build(TFEntityNames.ICE_SNOWBALL, makeCastedBuilder(IceSnowballEntity.class, IceSnowballEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)/*.setTrackingRange(150)*/);
	public static final EntityType<ChainBlockEntity> chain_block = build(TFEntityNames.CHAIN_BLOCK, makeCastedBuilder(ChainBlockEntity.class, ChainBlockEntity::new, MobCategory.MISC).sized(0.6F, 0.6F)/*.setUpdateInterval(1)*/);
	public static final EntityType<CubeOfAnnihilationEntity> cube_of_annihilation = build(TFEntityNames.CUBE_OF_ANNIHILATION, makeCastedBuilder(CubeOfAnnihilationEntity.class, CubeOfAnnihilationEntity::new, MobCategory.MISC).sized(1F, 1F)/*.setUpdateInterval(1)*/);
	public static final EntityType<SlideBlockEntity> slider = build(TFEntityNames.SLIDER, makeCastedBuilder(SlideBlockEntity.class, SlideBlockEntity::new, MobCategory.MISC).sized(0.98F, 0.98F)/*.setUpdateInterval(1)*/);
	//public static final EntityType<EntityTFBoggard> boggard = make(TFEntityNames.BOGGARD, EntityTFBoggard::new, EntityClassification.MONSTER, 0.8F, 1.1F);
	public static final EntityType<RisingZombieEntity> rising_zombie = make(TFEntityNames.RISING_ZOMBIE, RisingZombieEntity::new, MobCategory.MONSTER, 0.6F, 1.95F);
	public static final EntityType<ProtectionBoxEntity> protection_box = build(TFEntityNames.PROTECTION_BOX, makeCastedBuilder(ProtectionBoxEntity.class, ProtectionBoxEntity::new, MobCategory.MISC).noSave().noSummon().sized(0, 0));

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height) {
		return build(id, makeBuilder(factory, classification).sized(width, height));
	}

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification) {
		return make(id, factory, classification, 0.6F, 1.8F);
	}

	@SuppressWarnings("unchecked")
	private static <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
		boolean cache = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
		SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;
		EntityType<E> ret = Registry.register(Registry.ENTITY_TYPE, id,builder.build(id.toString()));
		SharedConstants.CHECK_DATA_FIXER_SCHEMA = cache;
		ALL.add(ret);
		return ret;
	}

	private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(@SuppressWarnings("unused") Class<E> cast, EntityType.EntityFactory<E> factory, MobCategory classification) {
		return makeBuilder(factory, classification);
	}

	private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory, MobCategory classification) {
		return EntityType.Builder.of(factory, classification).
				sized(0.6F, 1.8F);
				/*setTrackingRange(80).
				setUpdateInterval(3).
				setShouldReceiveVelocityUpdates(true);*/
	}

	private static Item spawnEgg(EntityType<? extends Mob> type, int color, int color2) {
		ResourceLocation eggId = new ResourceLocation(Registry.ENTITY_TYPE.getKey(type).getNamespace(), Registry.ENTITY_TYPE.getKey(type).getPath() + "_spawn_egg");
		return Registry.register(Registry.ITEM, eggId, new SpawnEggItem(type, color, color2, TFItems.defaultBuilder()));
	}

	public static void registerSpawnEggs() {
		spawnEgg(adherent, 0x0a0000, 0x00008b);
		spawnEgg(yeti_alpha, 0xcdcdcd, 0x29486e);
		spawnEgg(armored_giant, 0x239391, 0x9a9a9a);
		spawnEgg(bighorn_sheep, 0xdbceaf, 0xd7c771);
		spawnEgg(blockchain_goblin, 0xd3e7bc, 0x1f3fff);
		spawnEgg(tower_broodling, 0x343c14, 0xbaee02);
		spawnEgg(tower_ghast, 0xbcbcbc, 0xb77878);
		spawnEgg(mini_ghast, 0xbcbcbc, 0xa74343);
		spawnEgg(tower_golem, 0x6b3d20, 0xe2ddda);
		spawnEgg(troll, 0x9ea98f, 0xb0948e);
		spawnEgg(death_tome, 0x774e22, 0xdbcdbe);
		spawnEgg(deer, 0x7b4d2e, 0x4b241d);
		spawnEgg(bunny, 0xfefeee, 0xccaa99);
		spawnEgg(fire_beetle, 0x1d0b00, 0xcb6f25);
		spawnEgg(squirrel, 0x904f12, 0xeeeeee);
		spawnEgg(giant_miner, 0x211b52, 0x9a9a9a);
		spawnEgg(goblin_knight_lower, 0x566055, 0xd3e7bc);
		spawnEgg(harbinger_cube, 0x00000a, 0x8b0000);
		spawnEgg(hedge_spider, 0x235f13, 0x562653);
		spawnEgg(helmet_crab, 0xfb904b, 0xd3e7bc);
		spawnEgg(hostile_wolf, 0xd7d3d3, 0xab1e14);
		spawnEgg(hydra, 0x142940, 0x29806b);
		spawnEgg(ice_crystal, 0xdce9fe, 0xadcafb);
		spawnEgg(king_spider, 0x2c1a0e, 0xffc017);
		spawnEgg(knight_phantom, 0xa6673b, 0xd3e7bc);
		spawnEgg(kobold, 0x372096, 0x895d1b);
		spawnEgg(maze_slime, 0xa3a3a3, 0x2a3b17);
		spawnEgg(minoshroom, 0xa81012, 0xaa7d66);
		spawnEgg(minotaur, 0x3f3024, 0xaa7d66);
		spawnEgg(mist_wolf, 0x3a1411, 0xe2c88a);
		spawnEgg(mosquito_swarm, 0x080904, 0x2d2f21);
		spawnEgg(naga, 0xa4d316, 0x1b380b);
		spawnEgg(penguin, 0x12151b, 0xf9edd2);
		spawnEgg(pinch_beetle, 0xbc9327, 0x241609);
		spawnEgg(quest_ram, 0xfefeee, 0x33aadd);
		spawnEgg(raven, 0x000011, 0x222233);
		spawnEgg(redcap, 0x3b3a6c, 0xab1e14);
		spawnEgg(redcap_sapper, 0x575d21, 0xab1e14);
		spawnEgg(roving_cube, 0x0a0000, 0x00009b);
		spawnEgg(skeleton_druid, 0xa3a3a3, 0x2a3b17);
		spawnEgg(slime_beetle, 0x0c1606, 0x60a74c);
		spawnEgg(snow_guardian, 0xd3e7bc, 0xfefefe);
		spawnEgg(snow_queen, 0xb1b2d4, 0x87006e);
		spawnEgg(stable_ice_core, 0xa1bff3, 0x7000f8);
		spawnEgg(swarm_spider, 0x32022e, 0x17251e);
		spawnEgg(tiny_bird, 0x33aadd, 0x1188ee);
		spawnEgg(tower_termite, 0x5d2b21, 0xaca03a);
		spawnEgg(lich, 0xaca489, 0x360472);
		spawnEgg(unstable_ice_core, 0x9aacf5, 0x9b0fa5);
		spawnEgg(ur_ghast, 0xbcbcbc, 0xb77878);
		spawnEgg(wild_boar, 0x83653b, 0xffefca);
		spawnEgg(winter_wolf, 0xdfe3e5, 0xb2bcca);
		spawnEgg(wraith, 0x505050, 0x838383);
		spawnEgg(yeti, 0xdedede, 0x4675bb);
	}

	public static void registerEntities() {
		((TransformPowderItem) TFItems.transformation_powder).initTransformations();

		SpawnPlacements.register(wild_boar, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(bighorn_sheep, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(deer, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(redcap, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(skeleton_druid, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkeletonDruidEntity::skeletonDruidSpawnHandler);
		SpawnPlacements.register(wraith, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WraithEntity::getCanSpawnHere);
		SpawnPlacements.register(hydra, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(lich, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(penguin, ON_ICE, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PenguinEntity::canSpawn);
		SpawnPlacements.register(lich_minion, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(loyal_zombie, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(tiny_bird, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(squirrel, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(bunny, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(raven, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(quest_ram, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(kobold, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(mosquito_swarm, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MosquitoSwarmEntity::canSpawn);
		SpawnPlacements.register(death_tome, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(minotaur, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(minoshroom, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(fire_beetle, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(slime_beetle, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(pinch_beetle, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(mist_wolf, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HostileWolfEntity::getCanSpawnHere);
		SpawnPlacements.register(mini_ghast, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastlingEntity::canSpawnHere);
		SpawnPlacements.register(tower_golem, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(tower_termite, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(tower_ghast, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastguardEntity::ghastSpawnHandler);
		SpawnPlacements.register(ur_ghast, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(blockchain_goblin, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(goblin_knight_upper, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(goblin_knight_lower, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(helmet_crab, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(knight_phantom, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(naga, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(swarm_spider, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SwarmSpiderEntity::getCanSpawnHere);
		SpawnPlacements.register(king_spider, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(tower_broodling, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SwarmSpiderEntity::getCanSpawnHere);
		SpawnPlacements.register(hedge_spider, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HedgeSpiderEntity::canSpawn);
		SpawnPlacements.register(redcap_sapper, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(maze_slime, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MazeSlimeEntity::getCanSpawnHere);
		SpawnPlacements.register(yeti, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YetiEntity::yetiSnowyForestSpawnHandler);
		SpawnPlacements.register(yeti_alpha, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(winter_wolf, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WinterWolfEntity::canSpawnHere);
		SpawnPlacements.register(snow_guardian, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(stable_ice_core, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(unstable_ice_core, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(snow_queen, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(troll, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TrollEntity::canSpawn);
		SpawnPlacements.register(giant_miner, CLOUDS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMinerEntity::canSpawn);
		SpawnPlacements.register(armored_giant, CLOUDS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMinerEntity::canSpawn);
		SpawnPlacements.register(ice_crystal, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(harbinger_cube, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
		SpawnPlacements.register(adherent, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(roving_cube, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
		SpawnPlacements.register(rising_zombie, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);

		//EntitySpawnPlacementRegistry.register(castle_guardian, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
	}

	public static void addEntityAttributes() {
		FabricDefaultAttributeRegistry.register(wild_boar, Pig.createAttributes());
		FabricDefaultAttributeRegistry.register(bighorn_sheep, Sheep.createAttributes());
		FabricDefaultAttributeRegistry.register(deer, DeerEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(redcap, RedcapEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(swarm_spider, SwarmSpiderEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(naga, NagaEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(skeleton_druid, AbstractSkeleton.createAttributes());
		FabricDefaultAttributeRegistry.register(hostile_wolf, HostileWolfEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(wraith, WraithEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(hedge_spider, Spider.createAttributes());
		FabricDefaultAttributeRegistry.register(hydra, HydraEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(lich, LichEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(penguin, PenguinEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(lich_minion, Zombie.createAttributes());
		FabricDefaultAttributeRegistry.register(loyal_zombie, LoyalZombieEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(tiny_bird, TinyBirdEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(squirrel, SquirrelEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(bunny, BunnyEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(raven, RavenEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(quest_ram, QuestRamEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(kobold, KoboldEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(mosquito_swarm, MosquitoSwarmEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(death_tome, DeathTomeEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(minotaur, MinotaurEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(minoshroom, MinoshroomEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(fire_beetle, FireBeetleEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(slime_beetle, SlimeBeetleEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(pinch_beetle, PinchBeetleEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(maze_slime, MazeSlimeEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(redcap_sapper, RedcapSapperEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(mist_wolf, MistWolfEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(king_spider, KingSpiderEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(mini_ghast, CarminiteGhastlingEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(tower_ghast, CarminiteGhastguardEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(tower_golem, CarminiteGolemEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(tower_termite, TowerwoodBorerEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(tower_broodling, TowerBroodlingEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(ur_ghast, UrGhastEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(blockchain_goblin, BlockChainGoblinEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(goblin_knight_upper, UpperGoblinKnightEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(goblin_knight_lower, LowerGoblinKnightEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(helmet_crab, HelmetCrabEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(knight_phantom, KnightPhantomEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(yeti, YetiEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(yeti_alpha, AlphaYetiEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(winter_wolf, WinterWolfEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(snow_guardian, SnowGuardianEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(stable_ice_core, StableIceCoreEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(unstable_ice_core, UnstableIceCoreEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(snow_queen, SnowQueenEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(troll, TrollEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(giant_miner, GiantMinerEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(armored_giant, GiantMinerEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(ice_crystal, IceCrystalEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(harbinger_cube, HarbingerCubeEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(adherent, AdherentEntity.registerAttributes());
		FabricDefaultAttributeRegistry.register(roving_cube, RovingCubeEntity.registerAttributes());
		//FabricDefaultAttributeRegistry.register(castle_guardian, MobEntity.createMobAttributes().create());
		FabricDefaultAttributeRegistry.register(plateau_boss, PlateauBossEntity.registerAttributes());

		//FabricDefaultAttributeRegistry.register(boggard, EntityTFBoggard.registerAttributes().create());
		FabricDefaultAttributeRegistry.register(rising_zombie, Zombie.createAttributes());
	}

	@Environment(EnvType.CLIENT)
	public static class BakedMultiPartRenderers {

		private static final Map<ResourceLocation, EntityRenderer<?>> renderers = new HashMap<>();

		public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
			renderers.put(TFPartEntity.RENDERER, new NoopRenderer<>(context));
			renderers.put(HydraHeadEntity.RENDERER, new HydraHeadRenderer(context));
			renderers.put(HydraNeckEntity.RENDERER, new HydraNeckRenderer(context));
			renderers.put(SnowQueenIceShieldEntity.RENDERER, new SnowQueenIceShieldLayer<>(context));
			renderers.put(NagaSegmentEntity.RENDERER, new NagaSegmentRenderer<>(context));
		}

		public static EntityRenderer<?> lookup(ResourceLocation location) {
			return renderers.get(location);
		}

	}
}
