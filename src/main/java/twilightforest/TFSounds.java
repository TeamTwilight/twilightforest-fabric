package twilightforest;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import twilightforest.entity.TFEntities;

public final class TFSounds {
	
	public static final SoundEvent ALPHAYETI_ALERT = createEvent("entity.twilightforest.alphayeti.alert");
	public static final SoundEvent ALPHAYETI_DEATH = createEvent("entity.twilightforest.alphayeti.death");
	public static final SoundEvent ALPHAYETI_GRAB = createEvent("entity.twilightforest.alphayeti.grab");
	public static final SoundEvent ALPHAYETI_GROWL = createEvent("entity.twilightforest.alphayeti.growl");
	public static final SoundEvent ALPHAYETI_HURT = createEvent("entity.twilightforest.alphayeti.hurt");
	public static final SoundEvent ALPHAYETI_ICE = createEvent("entity.twilightforest.alphayeti.ice");
	public static final SoundEvent ALPHAYETI_PANT = createEvent("entity.twilightforest.alphayeti.pant");
	public static final SoundEvent ALPHAYETI_ROAR = createEvent("entity.twilightforest.alphayeti.roar");
	public static final SoundEvent ALPHAYETI_THROW = createEvent("entity.twilightforest.alphayeti.throw");
	public static final SoundEvent BIGHORN_AMBIENT = createEvent("entity.twilightforest.bighorn.ambient");
	public static final SoundEvent BIGHORN_DEATH = createEvent("entity.twilightforest.bighorn.death");
	public static final SoundEvent BIGHORN_HURT = createEvent("entity.twilightforest.bighorn.hurt");
	public static final SoundEvent BIGHORN_STEP = createEvent("entity.twilightforest.bighorn.step");
	public static final SoundEvent BLOCKCHAIN_AMBIENT = createEvent("entity.twilightforest.blockchain.ambient");
	public static final SoundEvent BLOCKCHAIN_COLLIDE = createEvent("item.twilightforest.blockchain.collide");
	public static final SoundEvent BLOCKCHAIN_DEATH = createEvent("entity.twilightforest.blockchain.death");
	public static final SoundEvent BLOCKCHAIN_FIRED = createEvent("item.twilightforest.blockchain.fire");
	public static final SoundEvent BLOCKCHAIN_HIT = createEvent("item.twilightforest.blockchain.hit");
	public static final SoundEvent BLOCKCHAIN_HURT = createEvent("entity.twilightforest.blockchain.hurt");
	public static final SoundEvent BOAR_AMBIENT = createEvent("entity.twilightforest.boar.ambient");
	public static final SoundEvent BOAR_DEATH = createEvent("entity.twilightforest.boar.death");
	public static final SoundEvent BOAR_HURT = createEvent("entity.twilightforest.boar.hurt");
	public static final SoundEvent BOAR_STEP = createEvent("entity.twilightforest.boar.step");
	public static final SoundEvent BRITTLE_FLASK_BREAK = createEvent("item.twilightforest.flask.break");
	public static final SoundEvent BRITTLE_FLASK_CRACK = createEvent("item.twilightforest.flask.crack");
	public static final SoundEvent BROODLING_AMBIENT = createEvent("entity.twilightforest.broodling.ambient");
	public static final SoundEvent BROODLING_DEATH = createEvent("entity.twilightforest.broodling.death");
	public static final SoundEvent BROODLING_HURT = createEvent("entity.twilightforest.broodling.hurt");
	public static final SoundEvent BROODLING_STEP = createEvent("entity.twilightforest.broodling.step");
	public static final SoundEvent BUILDER_OFF = createEvent("block.twilightforest.builder.off");
	public static final SoundEvent BUILDER_ON = createEvent("block.twilightforest.builder.on");
	public static final SoundEvent BUILDER_REPLACE = createEvent("block.twilightforest.builder.replace");
	public static final SoundEvent BUILDER_CREATE = createEvent("block.twilightforest.builder.create");
	public static final SoundEvent BUG_SQUISH = createEvent("block.twilightforest.bug.squish");
	public static final SoundEvent CARMINITE_GOLEM_ATTACK = createEvent("entity.twilightforest.carminitegolem.attack");
	public static final SoundEvent CARMINITE_GOLEM_DEATH = createEvent("entity.twilightforest.carminitegolem.death");
	public static final SoundEvent CARMINITE_GOLEM_HURT = createEvent("entity.twilightforest.carminitegolem.hurt");
	public static final SoundEvent CARMINITE_GOLEM_STEP = createEvent("entity.twilightforest.carminitegolem.step");
	public static final SoundEvent CHARM_KEEP = createEvent("item.twilightforest.charm.keep");
	public static final SoundEvent CHARM_LIFE = createEvent("item.twilightforest.charm.life");
	public static final SoundEvent CICADA = createEvent("entity.twilightforest.cicada");
	public static final SoundEvent DEER_DEATH = createEvent("entity.twilightforest.deer.death");
	public static final SoundEvent DEER_HURT = createEvent("entity.twilightforest.deer.hurt");
	public static final SoundEvent DEER_IDLE = createEvent("entity.twilightforest.deer.idle");
	public static final SoundEvent DOOR_ACTIVATED = createEvent("block.twilightforest.door.activate");
	public static final SoundEvent DOOR_REAPPEAR = createEvent("block.twilightforest.door.reappear");
	public static final SoundEvent DOOR_VANISH = createEvent("block.twilightforest.door.vanish");
	public static final SoundEvent FAN_WOOSH = createEvent("item.twilightforest.fan.woosh");
	public static final SoundEvent FIRE_BEETLE_DEATH = createEvent("entity.twilightforest.firebeetle.death");
	public static final SoundEvent FIRE_BEETLE_HURT = createEvent("entity.twilightforest.firebeetle.hurt");
	public static final SoundEvent FIRE_BEETLE_SHOOT = createEvent("entity.twilightforest.firebeetle.shoot");
	public static final SoundEvent FIRE_BEETLE_STEP = createEvent("entity.twilightforest.firebeetle.step");
	public static final SoundEvent FLASK_FILL = createEvent("item.twilightforest.flask.fill");
	public static final SoundEvent GHASTGUARD_AMBIENT = createEvent("entity.twilightforest.ghastguard.ambient");
	public static final SoundEvent GHASTGUARD_DEATH = createEvent("entity.twilightforest.ghastguard.death");
	public static final SoundEvent GHASTGUARD_HURT = createEvent("entity.twilightforest.ghastguard.hurt");
	public static final SoundEvent GHASTLING_AMBIENT = createEvent("entity.twilightforest.ghastling.ambient");
	public static final SoundEvent GHASTLING_DEATH = createEvent("entity.twilightforest.ghastling.death");
	public static final SoundEvent GHASTLING_HURT = createEvent("entity.twilightforest.ghastling.hurt");
	public static final SoundEvent GLASS_SWORD_BREAK = createEvent("item.twilightforest.glasssword.break");
	public static final SoundEvent GOBLIN_KNIGHT_AMBIENT = createEvent("entity.twilightforest.goblinKnight.ambient");
	public static final SoundEvent GOBLIN_KNIGHT_DEATH = createEvent("entity.twilightforest.goblinKnight.death");
	public static final SoundEvent GOBLIN_KNIGHT_HURT = createEvent("entity.twilightforest.goblinKnight.hurt");
	public static final SoundEvent GOBLIN_KNIGHT_MUFFLED_AMBIENT = createEvent("entity.twilightforest.goblinknight.muffled.ambient");
	public static final SoundEvent GOBLIN_KNIGHT_MUFFLED_DEATH = createEvent("entity.twilightforest.goblinknight.muffled.death");
	public static final SoundEvent GOBLIN_KNIGHT_MUFFLED_HURT = createEvent("entity.twilightforest.goblinknight.muffled.hurt");
	public static final SoundEvent HEDGE_SPIDER_AMBIENT = createEvent("entity.twilightforest.hedgespider.ambient");
	public static final SoundEvent HEDGE_SPIDER_DEATH = createEvent("entity.twilightforest.hedgespider.death");
	public static final SoundEvent HEDGE_SPIDER_HURT = createEvent("entity.twilightforest.hedgespider.hurt");
	public static final SoundEvent HEDGE_SPIDER_STEP = createEvent("entity.twilightforest.hedgespider.step");
	public static final SoundEvent HELMET_CRAB_DEATH = createEvent("entity.twilightforest.helmetcrab.death");
	public static final SoundEvent HELMET_CRAB_HURT = createEvent("entity.twilightforest.helmetcrab.hurt");
	public static final SoundEvent HELMET_CRAB_STEP = createEvent("entity.twilightforest.helmetcrab.step");
	public static final SoundEvent HOSTILE_WOLF_DEATH = createEvent("entity.twilightforest.hostilewolf.death");
	public static final SoundEvent HOSTILE_WOLF_HURT = createEvent("entity.twilightforest.hostilewolf.hurt");
	public static final SoundEvent HOSTILE_WOLF_IDLE = createEvent("entity.twilightforest.hostilewolf.idle");
	public static final SoundEvent HOSTILE_WOLF_TARGET = createEvent("entity.twilightforest.hostilewolf.target");
	public static final SoundEvent HYDRA_DEATH = createEvent("entity.twilightforest.hydra.death");
	public static final SoundEvent HYDRA_GROWL = createEvent("entity.twilightforest.hydra.growl");
	public static final SoundEvent HYDRA_HURT = createEvent("entity.twilightforest.hydra.hurt");
	public static final SoundEvent HYDRA_ROAR = createEvent("entity.twilightforest.hydra.roar");
	public static final SoundEvent HYDRA_SHOOT = createEvent("entity.twilightforest.hydra.shoot");
	public static final SoundEvent HYDRA_WARN = createEvent("entity.twilightforest.hydra.warn");
	public static final SoundEvent ICEBOMB_FIRED = createEvent("item.twilightforest.icebomb.fired");
	public static final SoundEvent ICE_CORE_AMBIENT = createEvent("entity.twilightforest.ice.noise");
	public static final SoundEvent ICE_CORE_DEATH = createEvent("entity.twilightforest.ice.death");
	public static final SoundEvent ICE_CORE_HURT = createEvent("entity.twilightforest.ice.hurt");
	public static final SoundEvent ICE_CORE_SHOOT = createEvent("entity.twilightforest.ice.shoot");
	public static final SoundEvent ICE_GUARDIAN_AMBIENT = createEvent("entity.twilightforest.iceguardian.ambient");
	public static final SoundEvent ICE_GUARDIAN_DEATH = createEvent("entity.twilightforest.iceguardian.death");
	public static final SoundEvent ICE_GUARDIAN_HURT = createEvent("entity.twilightforest.iceguardian.hurt");
	public static final SoundEvent JET_ACTIVE = createEvent("block.twilightforest.jet.active");
	public static final SoundEvent JET_POP = createEvent("block.twilightforest.jet.pop");
	public static final SoundEvent JET_START = createEvent("block.twilightforest.jet.start");
	public static final SoundEvent KING_SPIDER_AMBIENT = createEvent("entity.twilightforest.kingspider.ambient");
	public static final SoundEvent KING_SPIDER_DEATH = createEvent("entity.twilightforest.kingspider.death");
	public static final SoundEvent KING_SPIDER_HURT = createEvent("entity.twilightforest.kingspider.hurt");
	public static final SoundEvent KING_SPIDER_STEP = createEvent("entity.twilightforest.kingspider.step");
	public static final SoundEvent KNIGHTMETAL_EQUIP = createEvent("item.twilightforest.armor.equip_knightmetal");
	public static final SoundEvent KOBOLD_AMBIENT = createEvent("entity.twilightforest.kobold.ambient");
	public static final SoundEvent KOBOLD_DEATH = createEvent("entity.twilightforest.kobold.death");
	public static final SoundEvent KOBOLD_HURT = createEvent("entity.twilightforest.kobold.hurt");
	public static final SoundEvent KOBOLD_MUNCH = createEvent("entity.twilightforest.kobold.munch");
	public static final SoundEvent LAMP_BURN = createEvent("item.twilightforest.lamp.burn");
	public static final SoundEvent LICH_AMBIENT = createEvent("entity.twilightforest.lich.ambient");
	public static final SoundEvent LICH_CLONE_HURT = createEvent("entity.twilightforest.lichclone.hurt");
	public static final SoundEvent LICH_DEATH = createEvent("entity.twilightforest.lich.death");
	public static final SoundEvent LICH_HURT = createEvent("entity.twilightforest.lich.hurt");
	public static final SoundEvent LICH_SHOOT = createEvent("entity.twilightforest.lich.shoot");
	public static final SoundEvent LICH_TELEPORT = createEvent("entity.twilightforest.lich.teleport");
	public static final SoundEvent LOCKED_VANISHING_BLOCK = createEvent("block.twilightforest.vanish.locked");
	public static final SoundEvent LOYAL_ZOMBIE_AMBIENT = createEvent("entity.twilightforest.loyalzombie.ambient");
	public static final SoundEvent LOYAL_ZOMBIE_DEATH = createEvent("entity.twilightforest.loyalzombie.death");
	public static final SoundEvent LOYAL_ZOMBIE_HURT = createEvent("entity.twilightforest.loyalzombie.hurt");
	public static final SoundEvent LOYAL_ZOMBIE_STEP = createEvent("entity.twilightforest.loyalzombie.step");
	public static final SoundEvent MAGNET_GRAB = createEvent("item.twilightforest.magnet.grab");
	public static final SoundEvent MAZE_SLIME_DEATH = createEvent("entity.twilightforest.mazeslime.death");
	public static final SoundEvent MAZE_SLIME_DEATH_SMALL = createEvent("entity.twilightforest.mazeslimesmall.death");
	public static final SoundEvent MAZE_SLIME_HURT = createEvent("entity.twilightforest.mazeslime.hurt");
	public static final SoundEvent MAZE_SLIME_HURT_SMALL = createEvent("entity.twilightforest.mazeslimesmall.hurt");
	public static final SoundEvent MAZE_SLIME_SQUISH = createEvent("entity.twilightforest.mazeslime.squish");
	public static final SoundEvent MAZE_SLIME_SQUISH_SMALL = createEvent("entity.twilightforest.mazeslimesmall.squish");
	public static final SoundEvent MINION_AMBIENT = createEvent("entity.twilightforest.minion.ambient");
	public static final SoundEvent MINION_DEATH = createEvent("entity.twilightforest.minion.death");
	public static final SoundEvent MINION_HURT = createEvent("entity.twilightforest.minion.hurt");
	public static final SoundEvent MINION_STEP = createEvent("entity.twilightforest.minion.step");
	public static final SoundEvent MINION_SUMMON = createEvent("entity.twilightforest.minion.summon");
	public static final SoundEvent MINOSHROOM_AMBIENT = createEvent("entity.twilightforest.minoshroom.ambient");
	public static final SoundEvent MINOSHROOM_ATTACK = createEvent("entity.twilightforest.minoshroom.attack");
	public static final SoundEvent MINOSHROOM_DEATH = createEvent("entity.twilightforest.minoshroom.death");
	public static final SoundEvent MINOSHROOM_HURT = createEvent("entity.twilightforest.minoshroom.hurt");
	public static final SoundEvent MINOSHROOM_STEP = createEvent("entity.twilightforest.minoshroom.step");
	public static final SoundEvent MINOTAUR_AMBIENT = createEvent("entity.twilightforest.minotaur.ambient");
	public static final SoundEvent MINOTAUR_ATTACK = createEvent("entity.twilightforest.minotaur.attack");
	public static final SoundEvent MINOTAUR_DEATH = createEvent("entity.twilightforest.minotaur.death");
	public static final SoundEvent MINOTAUR_HURT = createEvent("entity.twilightforest.minotaur.hurt");
	public static final SoundEvent MINOTAUR_STEP = createEvent("entity.twilightforest.minotaur.step");
	public static final SoundEvent MISTWOLF_DEATH = createEvent("entity.twilightforest.mistwolf.death");
	public static final SoundEvent MISTWOLF_HURT = createEvent("entity.twilightforest.mistwolf.hurt");
	public static final SoundEvent MISTWOLF_IDLE = createEvent("entity.twilightforest.mistwolf.idle");
	public static final SoundEvent MISTWOLF_TARGET = createEvent("entity.twilightforest.mistwolf.target");
	public static final SoundEvent MOONWORM_SQUISH = createEvent("item.twilightforest.moonworm.squish");
	public static final SoundEvent MOSQUITO = createEvent("entity.twilightforest.mosquito.ambient");
	public static final SoundEvent NAGA_HISS = createEvent("entity.twilightforest.naga.hiss");
	public static final SoundEvent NAGA_HURT = createEvent("entity.twilightforest.naga.hurt");
	public static final SoundEvent NAGA_RATTLE = createEvent("entity.twilightforest.naga.rattle");
	public static final SoundEvent PEDESTAL_ACTIVATE = createEvent("block.twilightforest.pedestal.activate");
	public static final SoundEvent PHANTOM_AMBIENT = createEvent("entity.twilightforest.kphantom.ambient");
	public static final SoundEvent PHANTOM_DEATH = createEvent("entity.twilightforest.kphantom.death");
	public static final SoundEvent PHANTOM_HURT = createEvent("entity.twilightforest.kphantom.hurt");
	public static final SoundEvent PHANTOM_THROW_AXE = createEvent("entity.twilightforest.kphantom.axe");
	public static final SoundEvent PHANTOM_THROW_PICK = createEvent("entity.twilightforest.kphantom.pick");
	public static final SoundEvent PINCH_BEETLE_DEATH = createEvent("entity.twilightforest.pinchbeetle.death");
	public static final SoundEvent PINCH_BEETLE_HURT = createEvent("entity.twilightforest.pinchbeetle.hurt");
	public static final SoundEvent PINCH_BEETLE_STEP = createEvent("entity.twilightforest.pinchbeetle.step");
	public static final SoundEvent PORTAL_WOOSH = createEvent("block.twilightforest.portal.woosh");
	public static final SoundEvent POWDER_USE = createEvent("item.twilightforest.powder.use");
	public static final SoundEvent QUEST_RAM_AMBIENT = createEvent("entity.twilightforest.quest.ambient");
	public static final SoundEvent QUEST_RAM_DEATH = createEvent("entity.twilightforest.quest.death");
	public static final SoundEvent QUEST_RAM_HURT = createEvent("entity.twilightforest.quest.hurt");
	public static final SoundEvent QUEST_RAM_STEP = createEvent("entity.twilightforest.quest.step");
	public static final SoundEvent RAVEN_CAW = createEvent("entity.twilightforest.raven.caw");
	public static final SoundEvent RAVEN_SQUAWK = createEvent("entity.twilightforest.raven.squawk");
	public static final SoundEvent REACTOR_AMBIENT = createEvent("block.twilightforest.reactor.idle");
	public static final SoundEvent REAPPEAR_BLOCK = createEvent("block.twilightforest.reappear.reappear");
	public static final SoundEvent REAPPEAR_POOF = createEvent("block.twilightforest.reappear.vanish");
	public static final SoundEvent REDCAP_AMBIENT = createEvent("entity.twilightforest.redcap.ambient");
	public static final SoundEvent REDCAP_DEATH = createEvent("entity.twilightforest.redcap.death");
	public static final SoundEvent REDCAP_HURT = createEvent("entity.twilightforest.redcap.hurt");
	public static final SoundEvent SCEPTER_DRAIN = createEvent("item.twilightforest.scepter.drain");
	public static final SoundEvent SCEPTER_PEARL = createEvent("item.twilightforest.scepter.pearl");
	public static final SoundEvent SCEPTER_USE = createEvent("item.twilightforest.scepter.use");
	public static final SoundEvent SHIELD_ADD = createEvent("entity.twilightforest.shield.add");
	public static final SoundEvent SHIELD_BREAK = createEvent("entity.twilightforest.shield.break");
	public static final SoundEvent SKELETON_DRUID_AMBIENT = createEvent("entity.twilightforest.druid.ambient");
	public static final SoundEvent SKELETON_DRUID_DEATH = createEvent("entity.twilightforest.druid.death");
	public static final SoundEvent SKELETON_DRUID_HURT = createEvent("entity.twilightforest.druid.hurt");
	public static final SoundEvent SKELETON_DRUID_SHOOT = createEvent("entity.twilightforest.druid.shoot");
	public static final SoundEvent SKELETON_DRUID_STEP = createEvent("entity.twilightforest.druid.step");
	public static final SoundEvent SLIME_BEETLE_DEATH = createEvent("entity.twilightforest.slimebeetle.death");
	public static final SoundEvent SLIME_BEETLE_HURT = createEvent("entity.twilightforest.slimebeetle.hurt");
	public static final SoundEvent SLIME_BEETLE_SQUISH_SMALL = createEvent("entity.twilightforest.slimebeetle.squish");
	public static final SoundEvent SLIME_BEETLE_STEP = createEvent("entity.twilightforest.slimebeetle.step");
	public static final SoundEvent SMOKER_START = createEvent("block.twilightforest.smoker.start");
	public static final SoundEvent SNOW_QUEEN_AMBIENT = createEvent("entity.twilightforest.snowqueen.ambient");
	public static final SoundEvent SNOW_QUEEN_ATTACK = createEvent("entity.twilightforest.snowqueen.attack");
	public static final SoundEvent SNOW_QUEEN_BREAK = createEvent("entity.twilightforest.snowqueen.break");
	public static final SoundEvent SNOW_QUEEN_DEATH = createEvent("entity.twilightforest.snowqueen.death");
	public static final SoundEvent SNOW_QUEEN_HURT = createEvent("entity.twilightforest.snowqueen.hurt");
	public static final SoundEvent SWARM_SPIDER_AMBIENT = createEvent("entity.twilightforest.swarmspider.ambient");
	public static final SoundEvent SWARM_SPIDER_DEATH = createEvent("entity.twilightforest.swarmspider.death");
	public static final SoundEvent SWARM_SPIDER_HURT = createEvent("entity.twilightforest.swarmspider.hurt");
	public static final SoundEvent SWARM_SPIDER_STEP = createEvent("entity.twilightforest.swarmspider.step");
	public static final SoundEvent TEAR_BREAK = createEvent("entity.twilightforest.tear.break");
	public static final SoundEvent TERMITE_AMBIENT = createEvent("entity.twilightforest.termite.ambient");
	public static final SoundEvent TERMITE_DEATH = createEvent("entity.twilightforest.termite.death");
	public static final SoundEvent TERMITE_HURT = createEvent("entity.twilightforest.termite.hurt");
	public static final SoundEvent TERMITE_STEP = createEvent("entity.twilightforest.termite.step");
	public static final SoundEvent TIME_CORE = createEvent("block.twilightforest.core.time");
	public static final SoundEvent TINYBIRD_CHIRP = createEvent("entity.twilightforest.tinybird.chirp");
	public static final SoundEvent TINYBIRD_HURT = createEvent("entity.twilightforest.tinybird.hurt");
	public static final SoundEvent TINYBIRD_SONG = createEvent("entity.twilightforest.tinybird.song");
	public static final SoundEvent TOME_DEATH = createEvent("entity.twilightforest.tome.death");
	public static final SoundEvent TOME_HURT = createEvent("entity.twilightforest.tome.hurt");
	public static final SoundEvent TOME_IDLE = createEvent("entity.twilightforest.tome.idle");
	public static final SoundEvent TRANSFORMATION_CORE = createEvent("block.twilightforest.core.transformation");
	public static final SoundEvent UNLOCK_VANISHING_BLOCK = createEvent("block.twilightforest.vanish.unlock");
	public static final SoundEvent URGHAST_AMBIENT = createEvent("entity.twilightforest.urghast.ambient");
	public static final SoundEvent URGHAST_DEATH = createEvent("entity.twilightforest.urghast.death");
	public static final SoundEvent URGHAST_HURT = createEvent("entity.twilightforest.urghast.hurt");
	public static final SoundEvent URGHAST_TRAP_ACTIVE = createEvent("entity.twilightforest.urghast.trapactive");
	public static final SoundEvent URGHAST_TRAP_ON = createEvent("entity.twilightforest.urghast.trapon");
	public static final SoundEvent URGHAST_TRAP_SPINDOWN = createEvent("entity.twilightforest.urghast.trapspindown");
	public static final SoundEvent URGHAST_TRAP_WARMUP = createEvent("entity.twilightforest.urghast.trapwarmup");
	public static final SoundEvent VANISHING_BLOCK = createEvent("block.twilightforest.vanish.vanish");
	public static final SoundEvent WINTER_WOLF_DEATH = createEvent("entity.twilightforest.winterwolf.death");
	public static final SoundEvent WINTER_WOLF_HURT = createEvent("entity.twilightforest.winterwolf.hurt");
	public static final SoundEvent WINTER_WOLF_IDLE = createEvent("entity.twilightforest.winterwolf.idle");
	public static final SoundEvent WINTER_WOLF_SHOOT = createEvent("entity.twilightforest.winterwolf.shoot");
	public static final SoundEvent WINTER_WOLF_TARGET = createEvent("entity.twilightforest.winterwolf.target");
	public static final SoundEvent WRAITH_AMBIENT = createEvent("entity.twilightforest.wraith.ambient");
	public static final SoundEvent WRAITH_DEATH = createEvent("entity.twilightforest.wraith.death");
	public static final SoundEvent WRAITH_HURT = createEvent("entity.twilightforest.wraith.hurt");
	public static final SoundEvent YETI_DEATH = createEvent("entity.twilightforest.yeti.death");
	public static final SoundEvent YETI_GRAB = createEvent("entity.twilightforest.yeti.grab");
	public static final SoundEvent YETI_GROWL = createEvent("entity.twilightforest.yeti.growl");
	public static final SoundEvent YETI_HURT = createEvent("entity.twilightforest.yeti.hurt");
	public static final SoundEvent YETI_THROW = createEvent("entity.twilightforest.yeti.throw");
	
	//Parrot sounds
	public static final SoundEvent ALPHAYETI_PARROT = createEvent("entity.twilightforest.alphayeti.parrot");
	public static final SoundEvent CARMINITE_GOLEM_PARROT = createEvent("entity.twilightforest.carminitegolem.parrot");
	public static final SoundEvent HOSTILE_WOLF_PARROT = createEvent("entity.twilightforest.hostilewolf.parrot");
	public static final SoundEvent HYDRA_PARROT = createEvent("entity.twilightforest.hydra.parrot");
	public static final SoundEvent ICE_CORE_PARROT = createEvent("entity.twilightforest.icecore.parrot");
	public static final SoundEvent KOBOLD_PARROT = createEvent("entity.twilightforest.kobold.parrot");
	public static final SoundEvent MINOTAUR_PARROT = createEvent("entity.twilightforest.minotaur.parrot");
	public static final SoundEvent MOSQUITO_PARROT = createEvent("entity.twilightforest.mosquito.parrot");
	public static final SoundEvent NAGA_PARROT = createEvent("entity.twilightforest.naga.parrot");
	public static final SoundEvent REDCAP_PARROT = createEvent("entity.twilightforest.redcap.parrot");
	public static final SoundEvent TOME_PARROT = createEvent("entity.twilightforest.tome.parrot");
	public static final SoundEvent WRAITH_PARROT = createEvent("entity.twilightforest.wraith.parrot");

	public static final SoundEvent SLIDER = createEvent("random.twilightforest.slider");

	public static final SoundEvent CASKET_OPEN = createEvent("block.twilightforest.casket.open");
	public static final SoundEvent CASKET_CLOSE = createEvent("block.twilightforest.casket.close");
	public static final SoundEvent CASKET_LOCKED = createEvent("block.twilightforest.casket.locked");
	public static final SoundEvent CASKET_REPAIR = createEvent("block.twilightforest.casket.repair");

	public static final SoundEvent MUSIC = createEvent("music.bg");

	private static SoundEvent createEvent(String sound) {
		ResourceLocation name = TwilightForestMod.prefix(sound);
		return Registry.register(Registry.SOUND_EVENT, name, new SoundEvent(name));
	}

//	static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
//		event.getRegistry().registerAll(
//				ALPHAYETI_ALERT, ALPHAYETI_DEATH, ALPHAYETI_GRAB, ALPHAYETI_GROWL, ALPHAYETI_HURT, ALPHAYETI_ICE, ALPHAYETI_PANT, ALPHAYETI_ROAR, ALPHAYETI_THROW,
//				BIGHORN_AMBIENT, BIGHORN_DEATH, BIGHORN_HURT, BIGHORN_STEP,
//				BLOCKCHAIN_AMBIENT, BLOCKCHAIN_COLLIDE, BLOCKCHAIN_DEATH, BLOCKCHAIN_FIRED, BLOCKCHAIN_HIT, BLOCKCHAIN_HURT,
//				BOAR_AMBIENT, BOAR_DEATH, BOAR_HURT, BOAR_STEP,
//				BRITTLE_FLASK_BREAK, BRITTLE_FLASK_CRACK,
//				BROODLING_AMBIENT, BROODLING_DEATH, BROODLING_HURT, BROODLING_STEP,
//				BUILDER_OFF, BUILDER_ON, BUILDER_REPLACE, BUILDER_CREATE,
//				CARMINITE_GOLEM_ATTACK, CARMINITE_GOLEM_DEATH, CARMINITE_GOLEM_HURT, CARMINITE_GOLEM_STEP,
//				CHARM_KEEP, CHARM_LIFE,
//				CICADA,
//				DEER_DEATH, DEER_HURT, DEER_IDLE,
//				DOOR_ACTIVATED, DOOR_REAPPEAR, DOOR_VANISH,
//				FAN_WOOSH,
//				FIRE_BEETLE_DEATH, FIRE_BEETLE_HURT, FIRE_BEETLE_SHOOT, FIRE_BEETLE_STEP,
//				FLASK_FILL,
//				GHASTGUARD_AMBIENT, GHASTGUARD_DEATH, GHASTGUARD_HURT,
//				GHASTLING_AMBIENT, GHASTLING_DEATH, GHASTLING_HURT,
//				GLASS_SWORD_BREAK,
//				GOBLIN_KNIGHT_AMBIENT, GOBLIN_KNIGHT_DEATH, GOBLIN_KNIGHT_HURT,
//				GOBLIN_KNIGHT_MUFFLED_AMBIENT, GOBLIN_KNIGHT_MUFFLED_DEATH, GOBLIN_KNIGHT_MUFFLED_HURT,
//				HEDGE_SPIDER_AMBIENT, HEDGE_SPIDER_DEATH, HEDGE_SPIDER_HURT, HEDGE_SPIDER_STEP,
//				HELMET_CRAB_DEATH, HELMET_CRAB_HURT, HELMET_CRAB_STEP,
//				HOSTILE_WOLF_DEATH, HOSTILE_WOLF_HURT, HOSTILE_WOLF_IDLE, HOSTILE_WOLF_TARGET,
//				HYDRA_DEATH, HYDRA_GROWL, HYDRA_HURT, HYDRA_ROAR, HYDRA_SHOOT, HYDRA_WARN,
//				ICEBOMB_FIRED,
//				ICE_CORE_AMBIENT, ICE_CORE_DEATH, ICE_CORE_HURT, ICE_CORE_SHOOT,
//				ICE_GUARDIAN_AMBIENT, ICE_GUARDIAN_DEATH, ICE_GUARDIAN_HURT,
//				JET_ACTIVE, JET_POP, JET_START,
//				KING_SPIDER_AMBIENT, KING_SPIDER_DEATH, KING_SPIDER_HURT, KING_SPIDER_STEP,
//				KNIGHTMETAL_EQUIP,
//				KOBOLD_AMBIENT, KOBOLD_DEATH, KOBOLD_HURT, KOBOLD_MUNCH,
//				LAMP_BURN,
//				LICH_AMBIENT, LICH_CLONE_HURT, LICH_DEATH, LICH_HURT, LICH_SHOOT, LICH_TELEPORT,
//				LOCKED_VANISHING_BLOCK,
//				LOYAL_ZOMBIE_AMBIENT, LOYAL_ZOMBIE_DEATH, LOYAL_ZOMBIE_HURT, LOYAL_ZOMBIE_STEP,
//				MAGNET_GRAB,
//				MAZE_SLIME_DEATH, MAZE_SLIME_DEATH_SMALL, MAZE_SLIME_HURT, MAZE_SLIME_HURT_SMALL, MAZE_SLIME_SQUISH, MAZE_SLIME_SQUISH_SMALL,
//				MINION_AMBIENT, MINION_DEATH, MINION_HURT, MINION_STEP, MINION_SUMMON,
//				MINOSHROOM_AMBIENT, MINOSHROOM_ATTACK, MINOSHROOM_DEATH, MINOSHROOM_HURT, MINOSHROOM_STEP,
//				MINOTAUR_AMBIENT, MINOTAUR_ATTACK, MINOTAUR_DEATH, MINOTAUR_HURT, MINOTAUR_STEP,
//				MISTWOLF_DEATH, MISTWOLF_HURT, MISTWOLF_IDLE, MISTWOLF_TARGET,
//				MOONWORM_SQUISH,
//				MOSQUITO,
//				NAGA_HISS, NAGA_HURT, NAGA_RATTLE,
//				PEDESTAL_ACTIVATE,
//				PHANTOM_AMBIENT, PHANTOM_DEATH, PHANTOM_HURT, PHANTOM_THROW_AXE, PHANTOM_THROW_PICK,
//				PINCH_BEETLE_DEATH, PINCH_BEETLE_HURT, PINCH_BEETLE_STEP,
//				PORTAL_WOOSH,
//				POWDER_USE,
//				QUEST_RAM_AMBIENT, QUEST_RAM_DEATH, QUEST_RAM_HURT, QUEST_RAM_STEP,
//				RAVEN_CAW, RAVEN_SQUAWK,
//				REACTOR_AMBIENT,
//				REAPPEAR_BLOCK,
//				REDCAP_AMBIENT, REDCAP_DEATH, REDCAP_HURT,
//				SCEPTER_DRAIN, SCEPTER_PEARL, SCEPTER_USE,
//				SHIELD_ADD, SHIELD_BREAK,
//				SKELETON_DRUID_AMBIENT, SKELETON_DRUID_DEATH, SKELETON_DRUID_HURT, SKELETON_DRUID_SHOOT, SKELETON_DRUID_STEP,
//				SLIME_BEETLE_DEATH, SLIME_BEETLE_HURT, SLIME_BEETLE_SQUISH_SMALL, SLIME_BEETLE_STEP,
//				SMOKER_START,
//				SNOW_QUEEN_AMBIENT, SNOW_QUEEN_ATTACK, SNOW_QUEEN_BREAK, SNOW_QUEEN_DEATH, SNOW_QUEEN_HURT,
//				SWARM_SPIDER_AMBIENT, SWARM_SPIDER_DEATH, SWARM_SPIDER_HURT, SWARM_SPIDER_STEP,
//				TEAR_BREAK,
//				TERMITE_AMBIENT, TERMITE_DEATH, TERMITE_HURT, TERMITE_STEP,
//				TIME_CORE,
//				TINYBIRD_CHIRP, TINYBIRD_HURT, TINYBIRD_SONG,
//				TOME_DEATH, TOME_HURT, TOME_IDLE,
//				TRANSFORMATION_CORE,
//				UNLOCK_VANISHING_BLOCK,
//				URGHAST_AMBIENT, URGHAST_DEATH, URGHAST_HURT,
//				URGHAST_TRAP_ACTIVE, URGHAST_TRAP_ON, URGHAST_TRAP_SPINDOWN, URGHAST_TRAP_WARMUP,
//				VANISHING_BLOCK,
//				WINTER_WOLF_DEATH, WINTER_WOLF_HURT, WINTER_WOLF_IDLE, WINTER_WOLF_SHOOT, WINTER_WOLF_TARGET,
//				WRAITH_AMBIENT, WRAITH_DEATH, WRAITH_HURT,
//				YETI_DEATH, YETI_GRAB, YETI_GROWL, YETI_HURT, YETI_THROW,
//
//				ALPHAYETI_PARROT,
//				CARMINITE_GOLEM_PARROT,
//				HOSTILE_WOLF_PARROT,
//				HYDRA_PARROT,
//				ICE_CORE_PARROT,
//				KOBOLD_PARROT,
//				MOSQUITO_PARROT,
//				NAGA_PARROT,
//				REDCAP_PARROT,
//				TOME_PARROT,
//				WRAITH_PARROT,
//
//				SLIDER,
//				CASKET_CLOSE, CASKET_OPEN, CASKET_LOCKED, CASKET_REPAIR,
//				MUSIC
//		);
//	}

	public static void registerParrotSounds() {
		Parrot.MOB_SOUND_MAP.put(TFEntities.yeti_alpha, ALPHAYETI_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.blockchain_goblin, REDCAP_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.tower_broodling, SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.tower_golem, CARMINITE_GOLEM_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.fire_beetle, SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.mini_ghast, SoundEvents.PARROT_IMITATE_GHAST);
		Parrot.MOB_SOUND_MAP.put(TFEntities.tower_ghast, SoundEvents.PARROT_IMITATE_GHAST);
		Parrot.MOB_SOUND_MAP.put(TFEntities.hedge_spider, SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.helmet_crab, SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.hostile_wolf, HOSTILE_WOLF_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.hydra, HYDRA_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.stable_ice_core, ICE_CORE_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.king_spider, SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.kobold, KOBOLD_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.lich, SoundEvents.PARROT_IMITATE_BLAZE);
		Parrot.MOB_SOUND_MAP.put(TFEntities.maze_slime, SoundEvents.PARROT_IMITATE_SLIME);
		Parrot.MOB_SOUND_MAP.put(TFEntities.lich_minion, SoundEvents.PARROT_IMITATE_ZOMBIE);
		Parrot.MOB_SOUND_MAP.put(TFEntities.minoshroom, MINOTAUR_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.minotaur, MINOTAUR_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.mist_wolf, HOSTILE_WOLF_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.mosquito_swarm, MOSQUITO_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.naga, NAGA_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.knight_phantom, WRAITH_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.pinch_beetle, SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.redcap, REDCAP_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.redcap_sapper, REDCAP_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.skeleton_druid, SoundEvents.PARROT_IMITATE_SKELETON);
		Parrot.MOB_SOUND_MAP.put(TFEntities.slime_beetle, SoundEvents.PARROT_IMITATE_SLIME);
		Parrot.MOB_SOUND_MAP.put(TFEntities.snow_guardian, ICE_CORE_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.snow_queen, ICE_CORE_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.swarm_spider, SoundEvents.PARROT_IMITATE_SPIDER);
		Parrot.MOB_SOUND_MAP.put(TFEntities.tower_termite, SoundEvents.PARROT_IMITATE_SILVERFISH);
		Parrot.MOB_SOUND_MAP.put(TFEntities.death_tome, TOME_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.ur_ghast, SoundEvents.PARROT_IMITATE_GHAST);
		Parrot.MOB_SOUND_MAP.put(TFEntities.winter_wolf, HOSTILE_WOLF_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.wraith, WRAITH_PARROT);
		Parrot.MOB_SOUND_MAP.put(TFEntities.yeti, ALPHAYETI_PARROT);
		
	}

	private TFSounds() {}

}
