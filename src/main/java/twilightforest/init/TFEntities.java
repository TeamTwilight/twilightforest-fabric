package twilightforest.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Zombie;
import twilightforest.TwilightForestMod;
import twilightforest.entity.SlideBlock;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.entity.boss.Hydra;
import twilightforest.entity.boss.HydraMortar;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.boss.Minoshroom;
import twilightforest.entity.boss.Naga;
import twilightforest.entity.boss.PlateauBoss;
import twilightforest.entity.boss.SnowQueen;
import twilightforest.entity.boss.UrGhast;
import twilightforest.entity.monster.Adherent;
import twilightforest.entity.monster.ArmoredGiant;
import twilightforest.entity.monster.BlockChainGoblin;
import twilightforest.entity.monster.CarminiteGhastguard;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.entity.monster.CarminiteGolem;
import twilightforest.entity.monster.DeathTome;
import twilightforest.entity.monster.FireBeetle;
import twilightforest.entity.monster.GiantMiner;
import twilightforest.entity.monster.HarbingerCube;
import twilightforest.entity.monster.HedgeSpider;
import twilightforest.entity.monster.HelmetCrab;
import twilightforest.entity.monster.HostileWolf;
import twilightforest.entity.monster.KingSpider;
import twilightforest.entity.monster.Kobold;
import twilightforest.entity.monster.LichMinion;
import twilightforest.entity.monster.IceCrystal;
import twilightforest.entity.monster.LowerGoblinKnight;
import twilightforest.entity.monster.LoyalZombie;
import twilightforest.entity.monster.MazeSlime;
import twilightforest.entity.monster.Minotaur;
import twilightforest.entity.monster.MistWolf;
import twilightforest.entity.monster.MosquitoSwarm;
import twilightforest.entity.monster.PinchBeetle;
import twilightforest.entity.monster.Redcap;
import twilightforest.entity.monster.RedcapSapper;
import twilightforest.entity.monster.RisingZombie;
import twilightforest.entity.RovingCube;
import twilightforest.entity.monster.SkeletonDruid;
import twilightforest.entity.monster.SlimeBeetle;
import twilightforest.entity.monster.SnowGuardian;
import twilightforest.entity.monster.StableIceCore;
import twilightforest.entity.monster.SwarmSpider;
import twilightforest.entity.monster.TowerwoodBorer;
import twilightforest.entity.monster.TowerBroodling;
import twilightforest.entity.monster.Troll;
import twilightforest.entity.monster.UnstableIceCore;
import twilightforest.entity.monster.UpperGoblinKnight;
import twilightforest.entity.monster.WinterWolf;
import twilightforest.entity.monster.Wraith;
import twilightforest.entity.monster.Yeti;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.Boar;
import twilightforest.entity.passive.Deer;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Penguin;
import twilightforest.entity.passive.QuestRam;
import twilightforest.entity.passive.Raven;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.entity.projectile.IceSnowball;
import twilightforest.entity.projectile.ChainBlock;
import twilightforest.entity.projectile.CubeOfAnnihilation;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.entity.projectile.FallingIce;
import twilightforest.entity.projectile.LichBolt;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.entity.projectile.NatureBolt;
import twilightforest.entity.projectile.SlimeProjectile;
import twilightforest.entity.projectile.ThrownBlock;
import twilightforest.entity.projectile.TomeBolt;
import twilightforest.entity.projectile.UrGhastFireball;

import java.lang.reflect.Method;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class TFEntities {
    public static final TFRegistryObject<EntityType<Adherent>> ADHERENT = entity("adherent", EntityType.Builder.of(Adherent::new, MobCategory.MONSTER).sized(0.8F, 2.2F));
    public static final TFRegistryObject<EntityType<AlphaYeti>> ALPHA_YETI = entity("alpha_yeti", EntityType.Builder.<AlphaYeti>of((type, level) -> new AlphaYeti(type, level), MobCategory.MONSTER).sized(3.8F, 5.0F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<ArmoredGiant>> ARMORED_GIANT = entity("armored_giant", EntityType.Builder.of(ArmoredGiant::new, MobCategory.MONSTER).sized(2.4F, 7.2F));
    public static final TFRegistryObject<EntityType<Bighorn>> BIGHORN_SHEEP = entity("bighorn_sheep", EntityType.Builder.of(Bighorn::new, MobCategory.CREATURE).sized(0.9F, 1.3F));
    public static final TFRegistryObject<EntityType<Boar>> BOAR = entity("boar", EntityType.Builder.of(Boar::new, MobCategory.CREATURE).sized(0.9F, 0.9F));
    public static final TFRegistryObject<EntityType<BlockChainGoblin>> BLOCKCHAIN_GOBLIN = entity("blockchain_goblin", EntityType.Builder.<BlockChainGoblin>of((type, level) -> new BlockChainGoblin(type, level), MobCategory.MONSTER).sized(0.9F, 1.4F));
    public static final TFRegistryObject<EntityType<TowerBroodling>> CARMINITE_BROODLING = entity("carminite_broodling", EntityType.Builder.<TowerBroodling>of((type, level) -> new TowerBroodling(type, level), MobCategory.MONSTER).sized(0.7F, 0.5F));
    public static final TFRegistryObject<EntityType<CarminiteGhastguard>> CARMINITE_GHASTGUARD = entity("carminite_ghastguard", EntityType.Builder.<CarminiteGhastguard>of((type, level) -> new CarminiteGhastguard(type, level), MobCategory.MONSTER).sized(4.0F, 6.0F));
    public static final TFRegistryObject<EntityType<CarminiteGhastling>> CARMINITE_GHASTLING = entity("carminite_ghastling", EntityType.Builder.<CarminiteGhastling>of((type, level) -> new CarminiteGhastling(type, level), MobCategory.MONSTER).sized(1.1F, 1.5F));
    public static final TFRegistryObject<EntityType<CarminiteGolem>> CARMINITE_GOLEM = entity("carminite_golem", EntityType.Builder.<CarminiteGolem>of((type, level) -> new CarminiteGolem(type, level), MobCategory.MONSTER).sized(1.4F, 2.9F));
    public static final TFRegistryObject<EntityType<CubeOfAnnihilation>> CUBE_OF_ANNIHILATION = projectile("cube_of_annihilation", EntityType.Builder.<CubeOfAnnihilation>of(CubeOfAnnihilation::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).updateInterval(1));
    public static final TFRegistryObject<EntityType<DeathTome>> DEATH_TOME = entity("death_tome", EntityType.Builder.of(DeathTome::new, MobCategory.MONSTER).sized(0.6F, 1.1F));
    public static final TFRegistryObject<EntityType<Deer>> DEER = entity("deer", EntityType.Builder.of(Deer::new, MobCategory.CREATURE).sized(0.9F, 1.4F));
    public static final TFRegistryObject<EntityType<DwarfRabbit>> DWARF_RABBIT = entity("dwarf_rabbit", EntityType.Builder.of(DwarfRabbit::new, MobCategory.CREATURE).sized(0.4F, 0.5F));
    public static final TFRegistryObject<EntityType<FireBeetle>> FIRE_BEETLE = entity("fire_beetle", EntityType.Builder.<FireBeetle>of((type, level) -> new FireBeetle(type, level), MobCategory.MONSTER).sized(1.1F, 0.5F));
    public static final TFRegistryObject<EntityType<GiantMiner>> GIANT_MINER = entity("giant_miner", EntityType.Builder.of(GiantMiner::new, MobCategory.MONSTER).sized(2.4F, 7.2F));
    public static final TFRegistryObject<EntityType<HarbingerCube>> HARBINGER_CUBE = entity("harbinger_cube", EntityType.Builder.<HarbingerCube>of((type, level) -> new HarbingerCube(type, level), MobCategory.MONSTER).sized(1.9F, 2.4F));
    public static final TFRegistryObject<EntityType<HedgeSpider>> HEDGE_SPIDER = entity("hedge_spider", EntityType.Builder.<HedgeSpider>of((type, level) -> new HedgeSpider(type, level), MobCategory.MONSTER).sized(1.4F, 0.9F));
    public static final TFRegistryObject<EntityType<HelmetCrab>> HELMET_CRAB = entity("helmet_crab", EntityType.Builder.<HelmetCrab>of((type, level) -> new HelmetCrab(type, level), MobCategory.MONSTER).sized(0.8F, 1.1F));
    public static final TFRegistryObject<EntityType<HostileWolf>> HOSTILE_WOLF = entity("hostile_wolf", EntityType.Builder.<HostileWolf>of((type, level) -> new HostileWolf(type, level), MobCategory.MONSTER).sized(0.6F, 0.85F));
    public static final TFRegistryObject<EntityType<Hydra>> HYDRA = entity("hydra", EntityType.Builder.<Hydra>of((type, level) -> new Hydra(type, level), MobCategory.MONSTER).sized(16.0F, 12.0F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<HydraMortar>> HYDRA_MORTAR = projectile("hydra_mortar", EntityType.Builder.<HydraMortar>of(HydraMortar::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).updateInterval(3));
    public static final TFRegistryObject<EntityType<IceCrystal>> ICE_CRYSTAL = entity("ice_crystal", EntityType.Builder.<IceCrystal>of((type, level) -> new IceCrystal(type, level), MobCategory.MONSTER).sized(0.6F, 1.8F));
    public static final TFRegistryObject<EntityType<KingSpider>> KING_SPIDER = entity("king_spider", EntityType.Builder.<KingSpider>of((type, level) -> new KingSpider(type, level), MobCategory.MONSTER).sized(1.6F, 1.6F));
    public static final TFRegistryObject<EntityType<KnightPhantom>> KNIGHT_PHANTOM = entity("knight_phantom", EntityType.Builder.<KnightPhantom>of((type, level) -> new KnightPhantom(type, level), MobCategory.MONSTER).sized(1.25F, 2.5F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<Kobold>> KOBOLD = entity("kobold", EntityType.Builder.of(Kobold::new, MobCategory.MONSTER).sized(0.8F, 1.1F));
    public static final TFRegistryObject<EntityType<Lich>> LICH = entity("lich", EntityType.Builder.<Lich>of((type, level) -> new Lich(type, level), MobCategory.MONSTER).sized(1.1F, 2.1F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<LichBolt>> LICH_BOLT = projectile("lich_bolt", EntityType.Builder.<LichBolt>of(LichBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(2).updateInterval(2));
    public static final TFRegistryObject<EntityType<LichBomb>> LICH_BOMB = projectile("lich_bomb", EntityType.Builder.<LichBomb>of(LichBomb::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(2).updateInterval(2));
    public static final TFRegistryObject<EntityType<LichMinion>> LICH_MINION = entity("lich_minion", EntityType.Builder.<LichMinion>of((type, level) -> new LichMinion(type, level), MobCategory.MONSTER).sized(0.6F, 1.95F));
    public static final TFRegistryObject<EntityType<LowerGoblinKnight>> LOWER_GOBLIN_KNIGHT = entity("lower_goblin_knight", EntityType.Builder.<LowerGoblinKnight>of((type, level) -> new LowerGoblinKnight(type, level), MobCategory.MONSTER).sized(0.7F, 1.1F));
    public static final TFRegistryObject<EntityType<LoyalZombie>> LOYAL_ZOMBIE = entity("loyal_zombie", EntityType.Builder.<LoyalZombie>of((type, level) -> new LoyalZombie(type, level), MobCategory.MONSTER).sized(0.6F, 1.8F));
    public static final TFRegistryObject<EntityType<MazeSlime>> MAZE_SLIME = entity("maze_slime", EntityType.Builder.<MazeSlime>of((type, level) -> new MazeSlime(type, level), MobCategory.MONSTER).sized(2.04F, 2.04F));
    public static final TFRegistryObject<EntityType<Minoshroom>> MINOSHROOM = entity("minoshroom", EntityType.Builder.<Minoshroom>of((type, level) -> new Minoshroom(type, level), MobCategory.MONSTER).sized(1.49F, 2.5F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<Minotaur>> MINOTAUR = entity("minotaur", EntityType.Builder.<Minotaur>of((type, level) -> new Minotaur(type, level), MobCategory.MONSTER).sized(0.6F, 2.1F));
    public static final TFRegistryObject<EntityType<MistWolf>> MIST_WOLF = entity("mist_wolf", EntityType.Builder.<MistWolf>of((type, level) -> new MistWolf(type, level), MobCategory.MONSTER).sized(1.4F, 1.9F));
    public static final TFRegistryObject<EntityType<MosquitoSwarm>> MOSQUITO_SWARM = entity("mosquito_swarm", EntityType.Builder.<MosquitoSwarm>of((type, level) -> new MosquitoSwarm(type, level), MobCategory.MONSTER).sized(0.7F, 1.9F));
    public static final TFRegistryObject<EntityType<Naga>> NAGA = entity("naga", EntityType.Builder.<Naga>of((type, level) -> new Naga(type, level), MobCategory.MONSTER).sized(2.0F, 3.0F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<NatureBolt>> NATURE_BOLT = projectile("nature_bolt", EntityType.Builder.<NatureBolt>of(NatureBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(5).updateInterval(5));
    public static final TFRegistryObject<EntityType<Penguin>> PENGUIN = entity("penguin", EntityType.Builder.of(Penguin::new, MobCategory.CREATURE).sized(0.6F, 1.1F));
    public static final TFRegistryObject<EntityType<PinchBeetle>> PINCH_BEETLE = entity("pinch_beetle", EntityType.Builder.<PinchBeetle>of((type, level) -> new PinchBeetle(type, level), MobCategory.MONSTER).sized(1.2F, 0.5F));
    public static final TFRegistryObject<EntityType<QuestRam>> QUEST_RAM = entity("quest_ram", EntityType.Builder.<QuestRam>of((type, level) -> new QuestRam(type, level), MobCategory.CREATURE).sized(1.25F, 2.9F));
    public static final TFRegistryObject<EntityType<Raven>> RAVEN = entity("raven", EntityType.Builder.of(Raven::new, MobCategory.CREATURE).sized(0.4F, 0.7F));
    public static final TFRegistryObject<EntityType<Redcap>> REDCAP = entity("redcap", EntityType.Builder.<Redcap>of((type, level) -> new Redcap(type, level), MobCategory.MONSTER).sized(0.9F, 1.4F));
    public static final TFRegistryObject<EntityType<RedcapSapper>> REDCAP_SAPPER = entity("redcap_sapper", EntityType.Builder.<RedcapSapper>of((type, level) -> new RedcapSapper(type, level), MobCategory.MONSTER).sized(0.9F, 1.4F));
    public static final TFRegistryObject<EntityType<RisingZombie>> RISING_ZOMBIE = entity("rising_zombie", EntityType.Builder.<RisingZombie>of((type, level) -> new RisingZombie(type, level), MobCategory.MONSTER).sized(0.6F, 1.95F));
    public static final TFRegistryObject<EntityType<RovingCube>> ROVING_CUBE = entity("roving_cube", EntityType.Builder.<RovingCube>of((type, level) -> new RovingCube(type, level), MobCategory.MONSTER).sized(1.2F, 2.1F));
    public static final TFRegistryObject<EntityType<SkeletonDruid>> SKELETON_DRUID = entity("skeleton_druid", EntityType.Builder.of(SkeletonDruid::new, MobCategory.MONSTER).sized(0.6F, 1.99F));
    public static final TFRegistryObject<EntityType<SlideBlock>> SLIDER = projectile("slider", EntityType.Builder.<SlideBlock>of(SlideBlock::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(8).updateInterval(1));
    public static final TFRegistryObject<EntityType<SlimeBeetle>> SLIME_BEETLE = entity("slime_beetle", EntityType.Builder.<SlimeBeetle>of((type, level) -> new SlimeBeetle(type, level), MobCategory.MONSTER).sized(0.9F, 0.5F));
    public static final TFRegistryObject<EntityType<SnowGuardian>> SNOW_GUARDIAN = entity("snow_guardian", EntityType.Builder.<SnowGuardian>of((type, level) -> new SnowGuardian(type, level), MobCategory.MONSTER).sized(0.6F, 1.8F));
    public static final TFRegistryObject<EntityType<SnowQueen>> SNOW_QUEEN = entity("snow_queen", EntityType.Builder.<SnowQueen>of((type, level) -> new SnowQueen(type, level), MobCategory.MONSTER).sized(0.7F, 2.2F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<StableIceCore>> STABLE_ICE_CORE = entity("stable_ice_core", EntityType.Builder.<StableIceCore>of((type, level) -> new StableIceCore(type, level), MobCategory.MONSTER).sized(0.8F, 1.8F));
    public static final TFRegistryObject<EntityType<Squirrel>> SQUIRREL = entity("squirrel", EntityType.Builder.of(Squirrel::new, MobCategory.CREATURE).sized(0.4F, 0.4F));
    public static final TFRegistryObject<EntityType<SwarmSpider>> SWARM_SPIDER = entity("swarm_spider", EntityType.Builder.<SwarmSpider>of((type, level) -> new SwarmSpider(type, level), MobCategory.MONSTER).sized(0.8F, 0.4F));
    public static final TFRegistryObject<EntityType<TowerwoodBorer>> TOWERWOOD_BORER = entity("towerwood_borer", EntityType.Builder.<TowerwoodBorer>of((type, level) -> new TowerwoodBorer(type, level), MobCategory.MONSTER).sized(0.4F, 0.3F));
    public static final TFRegistryObject<EntityType<TomeBolt>> TOME_BOLT = projectile("tome_bolt", EntityType.Builder.<TomeBolt>of(TomeBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(5).updateInterval(2));
    public static final TFRegistryObject<EntityType<Troll>> TROLL = entity("troll", EntityType.Builder.<Troll>of((type, level) -> new Troll(type, level), MobCategory.MONSTER).sized(1.4F, 2.4F));
    public static final TFRegistryObject<EntityType<UnstableIceCore>> UNSTABLE_ICE_CORE = entity("unstable_ice_core", EntityType.Builder.<UnstableIceCore>of((type, level) -> new UnstableIceCore(type, level), MobCategory.MONSTER).sized(0.8F, 1.8F));
    public static final TFRegistryObject<EntityType<UpperGoblinKnight>> UPPER_GOBLIN_KNIGHT = entity("upper_goblin_knight", EntityType.Builder.<UpperGoblinKnight>of((type, level) -> new UpperGoblinKnight(type, level), MobCategory.MONSTER).sized(1.1F, 1.3F));
    public static final TFRegistryObject<EntityType<UrGhast>> UR_GHAST = entity("ur_ghast", EntityType.Builder.<UrGhast>of((type, level) -> new UrGhast(type, level), MobCategory.MONSTER).sized(14.0F, 18.0F).clientTrackingRange(10));
    public static final TFRegistryObject<EntityType<UrGhastFireball>> UR_GHAST_FIREBALL = projectile("ur_ghast_fireball", EntityType.Builder.<UrGhastFireball>of(UrGhastFireball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).updateInterval(10));
    public static final TFRegistryObject<EntityType<WinterWolf>> WINTER_WOLF = entity("winter_wolf", EntityType.Builder.<WinterWolf>of((type, level) -> new WinterWolf(type, level), MobCategory.MONSTER).sized(1.4F, 1.9F));
    public static final TFRegistryObject<EntityType<Wraith>> WRAITH = entity("wraith", EntityType.Builder.<Wraith>of((type, level) -> new Wraith(type, level), MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final TFRegistryObject<EntityType<Yeti>> YETI = entity("yeti", EntityType.Builder.of(Yeti::new, MobCategory.MONSTER).sized(1.4F, 2.4F));
    public static final TFRegistryObject<EntityType<IceSnowball>> ICE_SNOWBALL = projectile("ice_snowball", EntityType.Builder.<IceSnowball>of(IceSnowball::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(5).updateInterval(2));
    public static final TFRegistryObject<EntityType<IceBomb>> THROWN_ICE = projectile("thrown_ice", EntityType.Builder.<IceBomb>of(IceBomb::new, MobCategory.MISC).sized(0.75F, 0.75F).clientTrackingRange(5).updateInterval(2));
    public static final TFRegistryObject<EntityType<FallingIce>> FALLING_ICE = projectile("falling_ice", EntityType.Builder.<FallingIce>of(FallingIce::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(8).updateInterval(3));
    public static final TFRegistryObject<EntityType<SlimeProjectile>> SLIME_BLOB = projectile("slime_blob", EntityType.Builder.<SlimeProjectile>of(SlimeProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(5).updateInterval(2));
    public static final TFRegistryObject<EntityType<ChainBlock>> CHAIN_BLOCK = projectile("chain_block", EntityType.Builder.<ChainBlock>of(ChainBlock::new, MobCategory.MISC).sized(0.6F, 0.6F).clientTrackingRange(8).updateInterval(1));
    public static final TFRegistryObject<EntityType<twilightforest.entity.projectile.IceArrow>> ICE_ARROW = projectile("ice_arrow", EntityType.Builder.<twilightforest.entity.projectile.IceArrow>of(twilightforest.entity.projectile.IceArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    public static final TFRegistryObject<EntityType<twilightforest.entity.projectile.SeekerArrow>> SEEKER_ARROW = projectile("seeker_arrow", EntityType.Builder.<twilightforest.entity.projectile.SeekerArrow>of(twilightforest.entity.projectile.SeekerArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    public static final TFRegistryObject<EntityType<twilightforest.entity.projectile.ThrownWep>> THROWN_WEP = projectile("thrown_wep", EntityType.Builder.<twilightforest.entity.projectile.ThrownWep>of(twilightforest.entity.projectile.ThrownWep::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    public static final TFRegistryObject<EntityType<twilightforest.entity.CharmEffect>> CHARM_EFFECT = projectile("charm_effect", EntityType.Builder.<twilightforest.entity.CharmEffect>of(twilightforest.entity.CharmEffect::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(8).updateInterval(20));
    public static final TFRegistryObject<EntityType<twilightforest.entity.ProtectionBox>> PROTECTION_BOX = projectile("protection_box", EntityType.Builder.<twilightforest.entity.ProtectionBox>of(twilightforest.entity.ProtectionBox::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(16).updateInterval(20));
    public static final TFRegistryObject<EntityType<twilightforest.entity.monster.Boggard>> BOGGARD = entity("boggard", EntityType.Builder.<twilightforest.entity.monster.Boggard>of((type, level) -> new twilightforest.entity.monster.Boggard(type, level), MobCategory.MONSTER).sized(0.6F, 1.8F));
    public static final TFRegistryObject<EntityType<twilightforest.entity.MagicPainting>> MAGIC_PAINTING = projectile("magic_painting", EntityType.Builder.<twilightforest.entity.MagicPainting>of(twilightforest.entity.MagicPainting::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));
    // Q24 cross-lane addition (Lane A authored with permission, lives under Lane-B-owned TFEntities for projectile registration unity)
    public static final TFRegistryObject<EntityType<twilightforest.entity.projectile.TwilightWandBolt>> WAND_BOLT = projectile("wand_bolt", EntityType.Builder.<twilightforest.entity.projectile.TwilightWandBolt>of(twilightforest.entity.projectile.TwilightWandBolt::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(5).updateInterval(2));
    public static final TFRegistryObject<EntityType<twilightforest.entity.projectile.MoonwormShot>> MOONWORM_SHOT = projectile("moonworm_shot", EntityType.Builder.<twilightforest.entity.projectile.MoonwormShot>of(twilightforest.entity.projectile.MoonwormShot::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(5).updateInterval(2));
    public static final TFRegistryObject<EntityType<ThrownBlock>> THROWN_BLOCK = projectile("thrown_block", EntityType.Builder.<ThrownBlock>of(ThrownBlock::new, MobCategory.MISC).sized(0.75F, 0.75F).clientTrackingRange(8).updateInterval(3));
    public static final TFRegistryObject<EntityType<TinyBird>> TINY_BIRD = entity("tiny_bird", EntityType.Builder.of(TinyBird::new, MobCategory.CREATURE).sized(0.4F, 0.7F));
    public static final TFRegistryObject<EntityType<PlateauBoss>> PLATEAU_BOSS = entity("plateau_boss", EntityType.Builder.<PlateauBoss>of((type, level) -> new PlateauBoss(type, level), MobCategory.MONSTER).sized(1.0F, 1.0F).clientTrackingRange(10));

    private TFEntities() {
    }

    public static void addEntityAttributes() {
        register(Zombie.createAttributes(), LICH_MINION, ARMORED_GIANT);
        FabricDefaultAttributeRegistry.register(GIANT_MINER.get(), GiantMiner.registerAttributes().build());
        register(Spider.createAttributes());
        register(Ghast.createAttributes());
        register(Skeleton.createAttributes(), SKELETON_DRUID);
        register(PolarBear.createAttributes(), YETI);
        register(Blaze.createAttributes());
        register(Vex.createAttributes());
        register(Sheep.createAttributes(), BIGHORN_SHEEP);
        FabricDefaultAttributeRegistry.register(BOAR.get(), Boar.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(DEER.get(), Deer.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(DWARF_RABBIT.get(), DwarfRabbit.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(SQUIRREL.get(), Squirrel.registerAttributes().build());
        register(Chicken.createAttributes(), RAVEN, TINY_BIRD);
        FabricDefaultAttributeRegistry.register(PENGUIN.get(), Penguin.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(BLOCKCHAIN_GOBLIN.get(), BlockChainGoblin.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(CARMINITE_BROODLING.get(), TowerBroodling.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(CARMINITE_GHASTGUARD.get(), CarminiteGhastguard.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(CARMINITE_GHASTLING.get(), CarminiteGhastling.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(CARMINITE_GOLEM.get(), CarminiteGolem.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(KOBOLD.get(), Kobold.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(LOWER_GOBLIN_KNIGHT.get(), LowerGoblinKnight.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(DEATH_TOME.get(), DeathTome.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(FIRE_BEETLE.get(), FireBeetle.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(HEDGE_SPIDER.get(), Spider.createAttributes().build());
        FabricDefaultAttributeRegistry.register(HELMET_CRAB.get(), HelmetCrab.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(YETI.get(), Yeti.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(ADHERENT.get(), Adherent.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(ALPHA_YETI.get(), AlphaYeti.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(HYDRA.get(), Hydra.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(HOSTILE_WOLF.get(), HostileWolf.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(HARBINGER_CUBE.get(), HarbingerCube.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(ICE_CRYSTAL.get(), IceCrystal.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(KING_SPIDER.get(), KingSpider.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(KNIGHT_PHANTOM.get(), KnightPhantom.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(LICH.get(), Lich.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(MAZE_SLIME.get(), MazeSlime.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(LOYAL_ZOMBIE.get(), LoyalZombie.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(MINOSHROOM.get(), Minoshroom.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(MINOTAUR.get(), Minotaur.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(MIST_WOLF.get(), MistWolf.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(MOSQUITO_SWARM.get(), MosquitoSwarm.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(NAGA.get(), Naga.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(PINCH_BEETLE.get(), PinchBeetle.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(PLATEAU_BOSS.get(), PlateauBoss.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(QUEST_RAM.get(), QuestRam.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(REDCAP.get(), Redcap.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(REDCAP_SAPPER.get(), RedcapSapper.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(RISING_ZOMBIE.get(), Monster.createMonsterAttributes().build());
        FabricDefaultAttributeRegistry.register(ROVING_CUBE.get(), RovingCube.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(SLIME_BEETLE.get(), SlimeBeetle.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(SNOW_GUARDIAN.get(), SnowGuardian.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(SNOW_QUEEN.get(), SnowQueen.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(STABLE_ICE_CORE.get(), StableIceCore.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(SWARM_SPIDER.get(), SwarmSpider.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(TOWERWOOD_BORER.get(), TowerwoodBorer.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(TROLL.get(), Troll.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(UNSTABLE_ICE_CORE.get(), UnstableIceCore.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(UPPER_GOBLIN_KNIGHT.get(), UpperGoblinKnight.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(UR_GHAST.get(), UrGhast.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(WINTER_WOLF.get(), WinterWolf.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(WRAITH.get(), Wraith.registerAttributes().build());
        FabricDefaultAttributeRegistry.register(BOGGARD.get(), twilightforest.entity.monster.Boggard.registerAttributes().build());
    }

    private static <T extends Mob> TFRegistryObject<EntityType<T>> entity(String path, EntityType.Builder<T> builder) {
        EntityType<T> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, TwilightForestMod.prefix(path), builder.build(TwilightForestMod.ID + ":" + path));
        markServerOnly(type);
        return new TFRegistryObject<>(type);
    }

    private static <T extends net.minecraft.world.entity.Entity> TFRegistryObject<EntityType<T>> projectile(String path, EntityType.Builder<T> builder) {
        EntityType<T> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, TwilightForestMod.prefix(path), builder.build(TwilightForestMod.ID + ":" + path));
        markServerOnly(type);
        return new TFRegistryObject<>(type);
    }

    private static void markServerOnly(EntityType<?> type) {
    }

    @SafeVarargs
    private static <T extends LivingEntity> void register(AttributeSupplier.Builder attributes, TFRegistryObject<? extends EntityType<? extends T>>... entityTypes) {
        for (TFRegistryObject<? extends EntityType<? extends T>> entityType : entityTypes) {
            FabricDefaultAttributeRegistry.register(entityType.get(), attributes.build());
        }
    }
}
