package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;
import twilightforest.TFMain;
import twilightforest.entity.*;
import twilightforest.entity.boss.*;
import twilightforest.entity.monster.*;
import twilightforest.entity.passive.*;
import twilightforest.entity.projectile.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TFEntities {

	public static final Map<EntityType<?>, Supplier<AttributeSupplier.Builder>> ATTRIBUTES = new HashMap<>();
	public static final Map<EntityType<?>, SpawnPlacements.SpawnPredicate<?>> SPAWN_PREDICATES = new HashMap<>();

	public static final EntityType<Adherent> ADHERENT = registerWithAttributes("adherent", EntityType.Builder.of(Adherent::new, MobCategory.MONSTER).sized(0.8F, 2.2F).clientTrackingRange(8), Adherent::registerAttributes);
	public static final EntityType<AlphaYeti> ALPHA_YETI = registerWithEgg("alpha_yeti", EntityType.Builder.of(AlphaYeti::new, MobCategory.MONSTER).sized(3.8F, 5.0F).clientTrackingRange(16), AlphaYeti::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<ArmoredGiant> ARMORED_GIANT = registerWithEgg("armored_giant", EntityType.Builder.of(ArmoredGiant::new, MobCategory.MONSTER).sized(2.4F, 7.2F).clientTrackingRange(16), ArmoredGiant::registerAttributes, ArmoredGiant::canSpawn);
	public static final EntityType<Bighorn> BIGHORN_SHEEP = registerWithEgg("bighorn_sheep", EntityType.Builder.of(Bighorn::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(8), Bighorn::createAttributes, Animal::checkAnimalSpawnRules);
	public static final EntityType<BlockChainGoblin> BLOCKCHAIN_GOBLIN = registerWithEgg("block_and_chain_goblin", EntityType.Builder.of(BlockChainGoblin::new, MobCategory.MONSTER).sized(0.9F, 1.4F).clientTrackingRange(8), BlockChainGoblin::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<Boar> BOAR = registerWithEgg("boar", EntityType.Builder.of(Boar::new, MobCategory.CREATURE).sized(0.9F, 0.9F).clientTrackingRange(8), Boar::registerAttributes, Animal::checkAnimalSpawnRules);
	//public static final EntityType<Boggard>> BOGGARD = registerWithAttributes("boggard", EntityType.Builder.of(Boggard::new, MobCategory.MONSTER).sized(0.8F, 1.1F), Boggard::registerAttributes);
	public static final EntityType<TowerBroodling> CARMINITE_BROODLING = registerWithEgg("carminite_broodling", EntityType.Builder.of(TowerBroodling::new, MobCategory.MONSTER).sized(0.7F, 0.5F).clientTrackingRange(8), TowerBroodling::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<CarminiteGhastguard> CARMINITE_GHASTGUARD = registerWithEgg("carminite_ghastguard", EntityType.Builder.of(CarminiteGhastguard::new, MobCategory.MONSTER).sized(4.0F, 6.0F).clientTrackingRange(20).fireImmune(), CarminiteGhastguard::registerAttributes, CarminiteGhastguard::ghastSpawnHandler);
	public static final EntityType<CarminiteGhastling> CARMINITE_GHASTLING = registerWithEgg("carminite_ghastling", EntityType.Builder.of(CarminiteGhastling::new, MobCategory.MONSTER).sized(1.1F, 1.5F).eyeHeight(0.5F).clientTrackingRange(10).fireImmune(), CarminiteGhastling::registerAttributes, CarminiteGhastling::ghastSpawnHandler);
	public static final EntityType<CarminiteGolem> CARMINITE_GOLEM = registerWithEgg("carminite_golem", EntityType.Builder.of(CarminiteGolem::new, MobCategory.MONSTER).sized(1.4F, 2.9F).clientTrackingRange(8), CarminiteGolem::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<ChainBlock> CHAIN_BLOCK = registerMisc("chain_block", EntityType.Builder.<ChainBlock>of(ChainBlock::new, MobCategory.MISC).sized(0.6F, 0.6F).noSummon().clientTrackingRange(8).setUpdateInterval(1).fireImmune());
	public static final EntityType<CharmEffect> CHARM_EFFECT = registerMisc("charm_effect", EntityType.Builder.<CharmEffect>of(CharmEffect::new, MobCategory.MISC).sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).noSave().noSummon().fireImmune());
	public static final EntityType<CubeOfAnnihilation> CUBE_OF_ANNIHILATION = registerMisc("cube_of_annihilation", EntityType.Builder.<CubeOfAnnihilation>of(CubeOfAnnihilation::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().clientTrackingRange(4).setUpdateInterval(20).fireImmune());
	public static final EntityType<DeathTome> DEATH_TOME = registerWithEgg("death_tome", EntityType.Builder.of(DeathTome::new, MobCategory.MONSTER).sized(0.75F, 1.5F).clientTrackingRange(8), DeathTome::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<Deer> DEER = registerWithEgg("deer", EntityType.Builder.of(Deer::new, MobCategory.CREATURE).sized(0.7F, 1.8F).clientTrackingRange(8), Deer::registerAttributes, Animal::checkAnimalSpawnRules);
	public static final EntityType<DwarfRabbit> DWARF_RABBIT = registerWithEgg("dwarf_rabbit", EntityType.Builder.of(DwarfRabbit::new, MobCategory.CREATURE).sized(0.4F, 0.4F).clientTrackingRange(8), DwarfRabbit::registerAttributes, Animal::checkAnimalSpawnRules);
	public static final EntityType<FallingIce> FALLING_ICE = registerMisc("falling_ice", EntityType.Builder.<FallingIce>of(FallingIce::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).fireImmune());
	public static final EntityType<FireBeetle> FIRE_BEETLE = registerWithEgg("fire_beetle", EntityType.Builder.of(FireBeetle::new, MobCategory.MONSTER).sized(1.1F, 0.5F).clientTrackingRange(8).fireImmune(), FireBeetle::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<GiantMiner> GIANT_MINER = registerWithEgg("giant_miner", EntityType.Builder.of(GiantMiner::new, MobCategory.MONSTER).sized(2.4F, 7.2F).clientTrackingRange(16), GiantMiner::registerAttributes, GiantMiner::canSpawn);
	public static final EntityType<HarbingerCube> HARBINGER_CUBE = registerWithAttributes("harbinger_cube", EntityType.Builder.of(HarbingerCube::new, MobCategory.MONSTER).sized(1.9F, 2.4F).clientTrackingRange(8).fireImmune(), HarbingerCube::registerAttributes);
	public static final EntityType<HedgeSpider> HEDGE_SPIDER = registerWithEgg("hedge_spider", EntityType.Builder.of(HedgeSpider::new, MobCategory.MONSTER).sized(1.4F, 0.9F), HedgeSpider::createAttributes, HedgeSpider::canSpawn);
	public static final EntityType<HelmetCrab> HELMET_CRAB = registerWithEgg("helmet_crab", EntityType.Builder.of(HelmetCrab::new, MobCategory.MONSTER).sized(0.8F, 1.1F).eyeHeight(0.45F).clientTrackingRange(8).fireImmune(), HelmetCrab::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<HostileWolf> HOSTILE_WOLF = registerWithEgg("hostile_wolf", EntityType.Builder.of(HostileWolf::new, MobCategory.MONSTER).sized(0.6F, 0.85F).clientTrackingRange(8), HostileWolf::registerAttributes, HostileWolf::checkWolfSpawnRules);
	public static final EntityType<Hydra> HYDRA = registerWithEgg("hydra", EntityType.Builder.of(Hydra::new, MobCategory.MONSTER).sized(16.0F, 12.0F).clientTrackingRange(20).fireImmune(), Hydra::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<HydraMortar> HYDRA_MORTAR = registerMisc("hydra_mortar", EntityType.Builder.<HydraMortar>of(HydraMortar::new, MobCategory.MISC).sized(0.75F, 0.75F).clientTrackingRange(8).updateInterval(10).fireImmune());
	public static final EntityType<IceArrow> ICE_ARROW = registerMisc("ice_arrow", EntityType.Builder.<IceArrow>of(IceArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<IceBomb> THROWN_ICE = registerMisc("ice_bomb", EntityType.Builder.<IceBomb>of(IceBomb::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).updateInterval(10).fireImmune());
	public static final EntityType<IceCrystal> ICE_CRYSTAL = registerWithEgg("ice_crystal", EntityType.Builder.<IceCrystal>of(IceCrystal::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8), IceCrystal::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<IceSnowball> ICE_SNOWBALL = registerMisc("ice_snowball", EntityType.Builder.<IceSnowball>of(IceSnowball::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final EntityType<KingSpider> KING_SPIDER = registerWithEgg("king_spider", EntityType.Builder.of(KingSpider::new, MobCategory.MONSTER).sized(1.6F, 1.6F).clientTrackingRange(8), KingSpider::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<KnightPhantom> KNIGHT_PHANTOM = registerWithEgg("knight_phantom", EntityType.Builder.of(KnightPhantom::new, MobCategory.MONSTER).sized(1.25F, 2.5F).clientTrackingRange(10), KnightPhantom::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<Kobold> KOBOLD = registerWithEgg("kobold", EntityType.Builder.of(Kobold::new, MobCategory.MONSTER).sized(0.8F, 1.1F).clientTrackingRange(8), Kobold::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<Lich> LICH = registerWithEgg("lich", EntityType.Builder.<Lich>of(Lich::new, MobCategory.MONSTER).sized(0.8F, 2.5F).eyeHeight(1.95F), Lich::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<LichBolt> LICH_BOLT = registerMisc("lich_bolt", EntityType.Builder.<LichBolt>of(LichBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(8).updateInterval(10));
	public static final EntityType<LichBomb> LICH_BOMB = registerMisc("lich_bomb", EntityType.Builder.<LichBomb>of(LichBomb::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(8).updateInterval(10));
	public static final EntityType<LichMinion> LICH_MINION = registerWithAttributes("lich_minion", EntityType.Builder.<LichMinion>of(LichMinion::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8), LichMinion::createAttributes);
	public static final EntityType<LowerGoblinKnight> LOWER_GOBLIN_KNIGHT = registerWithEgg("lower_goblin_knight", EntityType.Builder.of(LowerGoblinKnight::new, MobCategory.MONSTER).sized(0.7F, 1.1F).clientTrackingRange(10), LowerGoblinKnight::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<LoyalZombie> LOYAL_ZOMBIE = registerWithAttributes("loyal_zombie", EntityType.Builder.of(LoyalZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F), LoyalZombie::registerAttributes);
	public static final EntityType<MazeSlime> MAZE_SLIME = registerWithEgg("maze_slime", EntityType.Builder.of(MazeSlime::new, MobCategory.MONSTER).sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(10), Monster::createMonsterAttributes, MazeSlime::getCanSpawnHere);
	public static final EntityType<Minoshroom> MINOSHROOM = registerWithEgg("minoshroom", EntityType.Builder.of(Minoshroom::new, MobCategory.MONSTER).sized(1.49F, 2.5F).clientTrackingRange(10), Minoshroom::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<Minotaur> MINOTAUR = registerWithEgg("minotaur", EntityType.Builder.of(Minotaur::new, MobCategory.MONSTER).sized(0.6F, 2.1F).clientTrackingRange(8), Minotaur::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<MistWolf> MIST_WOLF = registerWithEgg("mist_wolf", EntityType.Builder.of(MistWolf::new, MobCategory.MONSTER).sized(1.4F, 1.9F).clientTrackingRange(8), MistWolf::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<MoonwormShot> MOONWORM_SHOT = registerMisc("moonworm_shot", EntityType.Builder.<MoonwormShot>of(MoonwormShot::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final EntityType<MosquitoSwarm> MOSQUITO_SWARM = registerWithEgg("mosquito_swarm", EntityType.Builder.of(MosquitoSwarm::new, MobCategory.MONSTER).sized(0.7F, 1.9F).clientTrackingRange(10), MosquitoSwarm::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<Naga> NAGA = registerWithEgg("naga", EntityType.Builder.of(Naga::new, MobCategory.MONSTER).sized(2.0F, 3.0F).eyeHeight(2.0F).clientTrackingRange(10), Naga::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<NatureBolt> NATURE_BOLT = registerMisc("nature_bolt", EntityType.Builder.<NatureBolt>of(NatureBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final EntityType<MagicPainting> MAGIC_PAINTING = registerMisc("magic_painting", EntityType.Builder.of(MagicPainting::new, MobCategory.MISC).noSummon().sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));
	public static final EntityType<Penguin> PENGUIN = registerWithEgg("penguin", EntityType.Builder.of(Penguin::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(8), Penguin::registerAttributes, Penguin::canSpawn);
	public static final EntityType<PinchBeetle> PINCH_BEETLE = registerWithEgg("pinch_beetle", EntityType.Builder.of(PinchBeetle::new, MobCategory.MONSTER).sized(1.2F, 0.5F).clientTrackingRange(8), PinchBeetle::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<PlateauBoss> PLATEAU_BOSS = registerWithAttributes("plateau_boss", EntityType.Builder.of(PlateauBoss::new, MobCategory.MONSTER).noLootTable().noSave().noSummon().sized(1.0F, 1.0F).fireImmune().clientTrackingRange(10), PlateauBoss::registerAttributes);
	public static final EntityType<ProtectionBox> PROTECTION_BOX = registerMisc("protection_box", EntityType.Builder.<ProtectionBox>of(ProtectionBox::new, MobCategory.MISC).noSave().noSummon().sized(0.0F, 0.0F).clientTrackingRange(16).fireImmune());
	public static final EntityType<QuestRam> QUEST_RAM = registerWithEgg("quest_ram", EntityType.Builder.of(QuestRam::new, MobCategory.CREATURE).sized(1.25F, 2.9F).clientTrackingRange(10), QuestRam::registerAttributes, Animal::checkAnimalSpawnRules);
	public static final EntityType<Raven> RAVEN = registerWithEgg("raven", EntityType.Builder.of(Raven::new, MobCategory.CREATURE).sized(0.3F, 0.5F).clientTrackingRange(10), Raven::registerAttributes, Animal::checkAnimalSpawnRules);
	public static final EntityType<Redcap> REDCAP = registerWithEgg("redcap", EntityType.Builder.of(Redcap::new, MobCategory.MONSTER).sized(0.9F, 1.4F).ridingOffset(-0.4F).clientTrackingRange(8), Redcap::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<RedcapSapper> REDCAP_SAPPER = registerWithEgg("redcap_sapper", EntityType.Builder.of(RedcapSapper::new, MobCategory.MONSTER).sized(0.9F, 1.4F).ridingOffset(-0.4F).clientTrackingRange(8), RedcapSapper::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<RisingZombie> RISING_ZOMBIE = registerWithAttributes("rising_zombie", EntityType.Builder.of(RisingZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(16).noSummon(), Zombie::createAttributes);
	public static final EntityType<RovingCube> ROVING_CUBE = registerWithAttributes("roving_cube", EntityType.Builder.of(RovingCube::new, MobCategory.MONSTER).sized(1.2F, 2.1F).noLootTable().noSummon().clientTrackingRange(8).fireImmune(), RovingCube::registerAttributes);
	public static final EntityType<SeekerArrow> SEEKER_ARROW = registerMisc("seeker_arrow", EntityType.Builder.<SeekerArrow>of(SeekerArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<SkeletonDruid> SKELETON_DRUID = registerWithEgg("skeleton_druid", EntityType.Builder.of(SkeletonDruid::new, MobCategory.MONSTER).sized(0.6F, 1.99F).ridingOffset(-0.7F).clientTrackingRange(10), SkeletonDruid::createAttributes, SkeletonDruid::checkDruidSpawnRules);
	public static final EntityType<SlideBlock> SLIDER = registerMisc("slider", EntityType.Builder.<SlideBlock>of(SlideBlock::new, MobCategory.MISC).sized(0.98F, 0.98F).noSummon().clientTrackingRange(8));
	public static final EntityType<SlimeBeetle> SLIME_BEETLE = registerWithEgg("slime_beetle", EntityType.Builder.of(SlimeBeetle::new, MobCategory.MONSTER).sized(0.9F, 0.5F).clientTrackingRange(8), SlimeBeetle::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<SlimeProjectile> SLIME_BLOB = registerMisc("slime_projectile", EntityType.Builder.<SlimeProjectile>of(SlimeProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final EntityType<SnowGuardian> SNOW_GUARDIAN = registerWithEgg("snow_guardian", EntityType.Builder.of(SnowGuardian::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8), SnowGuardian::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<SnowQueen> SNOW_QUEEN = registerWithEgg("snow_queen", EntityType.Builder.of(SnowQueen::new, MobCategory.MONSTER).sized(0.7F, 2.5F).clientTrackingRange(10), SnowQueen::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<Squirrel> SQUIRREL = registerWithEgg("squirrel", EntityType.Builder.of(Squirrel::new, MobCategory.CREATURE).sized(0.3F, 0.5F).clientTrackingRange(8), Squirrel::registerAttributes, Animal::checkAnimalSpawnRules);
	public static final EntityType<StableIceCore> STABLE_ICE_CORE = registerWithEgg("stable_ice_core", EntityType.Builder.of(StableIceCore::new, MobCategory.MONSTER).sized(0.8F, 1.8F).eyeHeight(1.35F).clientTrackingRange(8), StableIceCore::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<SwarmSpider> SWARM_SPIDER = registerWithEgg("swarm_spider", EntityType.Builder.of(SwarmSpider::new, MobCategory.MONSTER).sized(0.8F, 0.4F).clientTrackingRange(8), SwarmSpider::registerAttributes, SwarmSpider::getCanSpawnHere);
	public static final EntityType<ThrownBlock> THROWN_BLOCK = registerMisc("thrown_block", EntityType.Builder.<ThrownBlock>of(ThrownBlock::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).fireImmune());
	public static final EntityType<ThrownWep> THROWN_WEP = registerMisc("thrown_weapon", EntityType.Builder.<ThrownWep>of(ThrownWep::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10));
	public static final EntityType<TinyBird> TINY_BIRD = registerWithEgg("tiny_bird", EntityType.Builder.of(TinyBird::new, MobCategory.CREATURE).sized(0.3F, 0.3F).clientTrackingRange(8), TinyBird::registerAttributes, Animal::checkAnimalSpawnRules);
	public static final EntityType<TomeBolt> TOME_BOLT = registerMisc("tome_bolt", EntityType.Builder.<TomeBolt>of(TomeBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final EntityType<TowerwoodBorer> TOWERWOOD_BORER = registerWithEgg("towerwood_borer", EntityType.Builder.of(TowerwoodBorer::new, MobCategory.MONSTER).sized(0.4F, 0.3F).eyeHeight(0.13F).clientTrackingRange(8), TowerwoodBorer::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<Troll> TROLL = registerWithEgg("troll", EntityType.Builder.of(Troll::new, MobCategory.MONSTER).sized(1.4F, 2.4F).clientTrackingRange(8), Troll::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<TwilightWandBolt> WAND_BOLT = registerMisc("wand_bolt", EntityType.Builder.<TwilightWandBolt>of(TwilightWandBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final EntityType<UnstableIceCore> UNSTABLE_ICE_CORE = registerWithEgg("unstable_ice_core", EntityType.Builder.of(UnstableIceCore::new, MobCategory.MONSTER).sized(0.8F, 1.8F).eyeHeight(1.35F).clientTrackingRange(8), UnstableIceCore::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<UpperGoblinKnight> UPPER_GOBLIN_KNIGHT = registerWithPlacement("upper_goblin_knight", EntityType.Builder.of(UpperGoblinKnight::new, MobCategory.MONSTER).sized(1.1F, 1.3F).clientTrackingRange(8), UpperGoblinKnight::registerAttributes, Monster::checkMonsterSpawnRules);
	public static final EntityType<UrGhast> UR_GHAST = registerWithEgg("ur_ghast", EntityType.Builder.of(UrGhast::new, MobCategory.MONSTER).sized(14.0F, 18.0F).clientTrackingRange(24).fireImmune(), UrGhast::registerAttributes, Monster::checkAnyLightMonsterSpawnRules);
	public static final EntityType<WinterWolf> WINTER_WOLF = registerWithEgg("winter_wolf", EntityType.Builder.of(WinterWolf::new, MobCategory.MONSTER).sized(1.4F, 1.9F).eyeHeight(1.45F).clientTrackingRange(8), WinterWolf::registerAttributes, WinterWolf::canSpawnHere);
	public static final EntityType<Wraith> WRAITH = registerWithEgg("wraith", EntityType.Builder.of(Wraith::new, MobCategory.MONSTER).sized(0.6F, 2.1F).clientTrackingRange(8).fireImmune(), Wraith::registerAttributes, Wraith::checkMonsterSpawnRules);
	public static final EntityType<Yeti> YETI = registerWithEgg("yeti", EntityType.Builder.of(Yeti::new, MobCategory.MONSTER).sized(1.4F, 2.4F).clientTrackingRange(8), Yeti::registerAttributes, Yeti::normalYetiSpawnHandler);

	public static final EntityType<Boat> TWILIGHT_OAK_BOAT = registerMisc("twilight_oak_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.TWILIGHT_OAK_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> TWILIGHT_OAK_CHEST_BOAT = registerMisc("twilight_oak_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.TWILIGHT_OAK_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<Boat> CANOPY_BOAT = registerMisc("canopy_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.CANOPY_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> CANOPY_CHEST_BOAT = registerMisc("canopy_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.CANOPY_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<Boat> MANGROVE_BOAT = registerMisc("mangrove_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.MANGROVE_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> MANGROVE_CHEST_BOAT = registerMisc("mangrove_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.MANGROVE_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<Boat> DARK_BOAT = registerMisc("dark_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.DARK_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> DARK_CHEST_BOAT = registerMisc("dark_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.DARK_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<Boat> TIME_BOAT = registerMisc("time_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.TIME_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> TIME_CHEST_BOAT = registerMisc("time_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.TIME_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<Boat> TRANSFORMATION_BOAT = registerMisc("transformation_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.TRANSFORMATION_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> TRANSFORMATION_CHEST_BOAT = registerMisc("transformation_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.TRANSFORMATION_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<Boat> MINING_BOAT = registerMisc("mining_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.MINING_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> MINING_CHEST_BOAT = registerMisc("mining_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.MINING_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<Boat> SORTING_BOAT = registerMisc("sorting_boat", EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, TFItems.SORTING_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final EntityType<ChestBoat> SORTING_CHEST_BOAT = registerMisc("sorting_chest_boat", EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, TFItems.SORTING_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));

	public static <E extends Entity> EntityType<E> registerMisc(String name, EntityType.Builder<E> builder) {
		return Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			TFMain.prefix(name),
			builder.noLootTable().build(createIDFor(name))
		);
	}

	public static <E extends LivingEntity> EntityType<E> registerWithAttributes(String name, EntityType.Builder<E> builder, Supplier<AttributeSupplier.Builder> attributes) {
		EntityType<E> ret = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			TFMain.prefix(name),
			builder.build(createIDFor(name))
		);
		ATTRIBUTES.put(ret, attributes);
		return ret;
	}

	public static <E extends LivingEntity> EntityType<E> registerWithPlacement(String name, EntityType.Builder<E> builder, Supplier<AttributeSupplier.Builder> attributes, SpawnPlacements.@Nullable SpawnPredicate<E> predicate) {
		EntityType<E> ret = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			TFMain.prefix(name),
			builder.build(createIDFor(name))
		);
		ATTRIBUTES.put(ret, attributes);

		if (predicate != null) {
			SPAWN_PREDICATES.put(ret, predicate);
		}

		return ret;
	}

	public static <E extends Mob> EntityType<E> registerWithEgg(String name, EntityType.Builder<E> builder, Supplier<AttributeSupplier.Builder> attributes, SpawnPlacements.@Nullable SpawnPredicate<E> predicate) {
		EntityType<E> ret = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			TFMain.prefix(name),
			builder.build(createIDFor(name))
		);
		TFItems.register(name + "_spawn_egg", Item::new, () -> new Item.Properties().spawnEgg(ret));
		ATTRIBUTES.put(ret, attributes);
		if (predicate != null) {
			SPAWN_PREDICATES.put(ret, predicate);
		}
		return ret;
	}

	private static ResourceKey<EntityType<?>> createIDFor(String name) {
		return ResourceKey.create(Registries.ENTITY_TYPE, TFMain.prefix(name));
	}
}