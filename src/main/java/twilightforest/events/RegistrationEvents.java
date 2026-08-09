package twilightforest.events;

import com.google.common.collect.Maps;
import io.github.fabricators_of_create.porting_lib.resources.data_maps.PortingLibDataMaps;
import io.github.fabricators_of_create.porting_lib.resources.events.AddReloadListenersEvent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.command.TFCommand;
import twilightforest.config.ConfigSetup;
import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.entity.RovingCube;
import twilightforest.entity.boss.*;
import twilightforest.entity.monster.*;
import twilightforest.entity.passive.*;
import twilightforest.init.*;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.network.*;
import twilightforest.util.HolidayEvent;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;

public class RegistrationEvents {

	public static final RegistrationEvents INSTANCE = new RegistrationEvents();

	private final TFCommand tfCommand = TFCommand.INSTANCE;

	private final HolidayEvent holidayEvent = HolidayEvent.INSTANCE;

	private final StructureTemplateDefinitions structureTemplateDefinitions = StructureTemplateDefinitions.INSTANCE;

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			INSTANCE.tfCommand.register(dispatcher, registryAccess);
		});
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new QuestReloadListener());
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(TravellersModifiersManager.CacheInvalidationReloadListener.INSTANCE);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(StalactiteReloadListener.INSTANCE);
		AddReloadListenersEvent.EVENT.register(INSTANCE.structureTemplateDefinitions::registerListener);

		ConfigSetup.loadConfigs();
		ConfigSetup.reloadConfigs();
		ConfigSetup.syncUncraftingConfig();
	}

	public static void registerPackets() {
		PayloadTypeRegistry.playS2C().register(AreaProtectionPacket.TYPE, AreaProtectionPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(MagicMapPacket.TYPE, MagicMapPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(MazeMapPacket.TYPE, MazeMapPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(MovePlayerPacket.TYPE, MovePlayerPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(SpawnCharmPacket.TYPE, SpawnCharmPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(StructureProtectionPacket.TYPE, StructureProtectionPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(UpdateThrownPacket.TYPE, UpdateThrownPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(LifedrainParticlePacket.TYPE, LifedrainParticlePacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(SyncQuestsPacket.TYPE, SyncQuestsPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket.STREAM_CODEC);

		PayloadTypeRegistry.playC2S().register(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(SwapHotbarPacket.TYPE, SwapHotbarPacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(PerformSidestepPacket.TYPE, PerformSidestepPacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(CycleMapSlotPacket.TYPE, CycleMapSlotPacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(UncraftingGuiPacket.TYPE, UncraftingGuiPacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(WipeOreMeterPacket.TYPE, WipeOreMeterPacket.STREAM_CODEC);

		PayloadTypeRegistry.playS2C().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
	}

	public static void registerServerPacketHandlers() {
		ServerPlayNetworking.registerGlobalReceiver(PerformDoubleJumpPacket.TYPE, (payload, context) ->
			PerformDoubleJumpPacket.handle(payload, IPayloadContext.fromServerNetworking(context)));
		ServerPlayNetworking.registerGlobalReceiver(SwapHotbarPacket.TYPE, (payload, context) ->
			SwapHotbarPacket.handle(payload, IPayloadContext.fromServerNetworking(context)));
		ServerPlayNetworking.registerGlobalReceiver(PerformSidestepPacket.TYPE, (payload, context) ->
			PerformSidestepPacket.handle(payload, IPayloadContext.fromServerNetworking(context)));
		ServerPlayNetworking.registerGlobalReceiver(CycleMapSlotPacket.TYPE, (payload, context) ->
			CycleMapSlotPacket.handle(payload, IPayloadContext.fromServerNetworking(context)));
		ServerPlayNetworking.registerGlobalReceiver(UncraftingGuiPacket.TYPE, (payload, context) ->
			UncraftingGuiPacket.handle(payload, IPayloadContext.fromServerNetworking(context)));
		ServerPlayNetworking.registerGlobalReceiver(WipeOreMeterPacket.TYPE, (payload, context) ->
			WipeOreMeterPacket.handle(payload, IPayloadContext.fromServerNetworking(context)));

		ServerPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, (payload, context) ->
			GogglesZoomPacket.handle(payload, IPayloadContext.fromServerNetworking(context)));
		ServerPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, (payload, context) ->
			GradualGlidePacket.handle(payload, IPayloadContext.fromServerNetworking(context)));
	}

	public static void registerClientPacketHandlers() {
		ClientPlayNetworking.registerGlobalReceiver(AreaProtectionPacket.TYPE, (payload, context) ->
			AreaProtectionPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(CreateMovingCicadaSoundPacket.TYPE, (payload, context) ->
			CreateMovingCicadaSoundPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(EnforceProgressionStatusPacket.TYPE, (payload, context) ->
			EnforceProgressionStatusPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(MagicMapPacket.TYPE, (payload, context) ->
			MagicMapPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(MazeMapPacket.TYPE, (payload, context) ->
			MazeMapPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(MissingAdvancementToastPacket.TYPE, (payload, context) ->
			MissingAdvancementToastPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(MovePlayerPacket.TYPE, (payload, context) ->
			MovePlayerPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(ParticlePacket.TYPE, (payload, context) ->
			ParticlePacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(SpawnCharmPacket.TYPE, (payload, context) ->
			SpawnCharmPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(SpawnFallenLeafFromPacket.TYPE, (payload, context) ->
			SpawnFallenLeafFromPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(StructureProtectionPacket.TYPE, (payload, context) ->
			StructureProtectionPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(SyncUncraftingTableConfigPacket.TYPE, (payload, context) ->
			SyncUncraftingTableConfigPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(UpdateTFMultipartPacket.TYPE, (payload, context) ->
			UpdateTFMultipartPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(UpdateThrownPacket.TYPE, (payload, context) ->
			UpdateThrownPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(LifedrainParticlePacket.TYPE, (payload, context) ->
			LifedrainParticlePacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(UpdateDeathTimePacket.TYPE, (payload, context) ->
			UpdateDeathTimePacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.AddTFBossBarPacket.TYPE, (payload, context) ->
			TFBossBarPacket.AddTFBossBarPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, (payload, context) ->
			TFBossBarPacket.UpdateTFBossBarStylePacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(SetMasonJarItemPacket.TYPE, (payload, context) ->
			SetMasonJarItemPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(SyncQuestsPacket.TYPE, (payload, context) ->
			SyncQuestsPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(TravellersWingsStatePacket.TYPE, (payload, context) ->
			TravellersWingsStatePacket.handle(payload, IPayloadContext.fromClientNetworking(context)));

		// Bidirectional packets
		ClientPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, (payload, context) ->
			GogglesZoomPacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
		ClientPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, (payload, context) ->
			GradualGlidePacket.handle(payload, IPayloadContext.fromClientNetworking(context)));
	}

	public static void addEntityAttributes() {
		FabricDefaultAttributeRegistry.register(TFEntities.BOAR.get(), Boar.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.BIGHORN_SHEEP.get(), Sheep.createAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.DEER.get(), Deer.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.REDCAP.get(), Redcap.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.SWARM_SPIDER.get(), SwarmSpider.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.NAGA.get(), Naga.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.SKELETON_DRUID.get(), AbstractSkeleton.createAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.HOSTILE_WOLF.get(), HostileWolf.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.WRAITH.get(), Wraith.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.HEDGE_SPIDER.get(), Spider.createAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.HYDRA.get(), Hydra.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.LICH.get(), Lich.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.PENGUIN.get(), Penguin.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.LICH_MINION.get(), Zombie.createAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.LOYAL_ZOMBIE.get(), LoyalZombie.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.TINY_BIRD.get(), TinyBird.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.SQUIRREL.get(), Squirrel.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.DWARF_RABBIT.get(), DwarfRabbit.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.RAVEN.get(), Raven.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.QUEST_RAM.get(), QuestRam.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.KOBOLD.get(), Kobold.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarm.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.DEATH_TOME.get(), DeathTome.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.MINOTAUR.get(), Minotaur.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.MINOSHROOM.get(), Minoshroom.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.FIRE_BEETLE.get(), FireBeetle.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.SLIME_BEETLE.get(), SlimeBeetle.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.PINCH_BEETLE.get(), PinchBeetle.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.MAZE_SLIME.get(), Monster.createMonsterAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.REDCAP_SAPPER.get(), RedcapSapper.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.MIST_WOLF.get(), MistWolf.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.KING_SPIDER.get(), KingSpider.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_GHASTLING.get(), CarminiteGhastling.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_GHASTGUARD.get(), CarminiteGhastguard.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_GOLEM.get(), CarminiteGolem.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.TOWERWOOD_BORER.get(), TowerwoodBorer.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_BROODLING.get(), TowerBroodling.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.UR_GHAST.get(), UrGhast.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), BlockChainGoblin.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), UpperGoblinKnight.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), LowerGoblinKnight.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.HELMET_CRAB.get(), HelmetCrab.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.KNIGHT_PHANTOM.get(), KnightPhantom.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.YETI.get(), Yeti.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.ALPHA_YETI.get(), AlphaYeti.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.WINTER_WOLF.get(), WinterWolf.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.SNOW_GUARDIAN.get(), SnowGuardian.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.STABLE_ICE_CORE.get(), StableIceCore.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.UNSTABLE_ICE_CORE.get(), UnstableIceCore.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.SNOW_QUEEN.get(), SnowQueen.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.TROLL.get(), Troll.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.GIANT_MINER.get(), GiantMiner.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.ARMORED_GIANT.get(), GiantMiner.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.ICE_CRYSTAL.get(), IceCrystal.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.HARBINGER_CUBE.get(), HarbingerCube.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.ADHERENT.get(), Adherent.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.ROVING_CUBE.get(), RovingCube.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.PLATEAU_BOSS.get(), PlateauBoss.registerAttributes().build());
		FabricDefaultAttributeRegistry.register(TFEntities.RISING_ZOMBIE.get(), Zombie.createAttributes().build());
	}

	public static void registerSpawnPlacements() {
		SpawnPlacements.register(TFEntities.BOAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.BIGHORN_SHEEP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.REDCAP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SKELETON_DRUID.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkeletonDruid::checkDruidSpawnRules);
		SpawnPlacements.register(TFEntities.WRAITH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Wraith::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HOSTILE_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HostileWolf::checkWolfSpawnRules);
		SpawnPlacements.register(TFEntities.HYDRA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TFEntities.LICH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.PENGUIN.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Penguin::canSpawn);
		SpawnPlacements.register(TFEntities.LICH_MINION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.LOYAL_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TFEntities.TINY_BIRD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.SQUIRREL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.DWARF_RABBIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.RAVEN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.QUEST_RAM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.KOBOLD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MOSQUITO_SWARM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.DEATH_TOME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MINOTAUR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MINOSHROOM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.FIRE_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SLIME_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.PINCH_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MIST_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.CARMINITE_GHASTLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastling::canSpawnHere);
		SpawnPlacements.register(TFEntities.CARMINITE_GOLEM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.TOWERWOOD_BORER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.CARMINITE_GHASTGUARD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastguard::ghastSpawnHandler);
		SpawnPlacements.register(TFEntities.UR_GHAST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HELMET_CRAB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.KNIGHT_PHANTOM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TFEntities.NAGA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SWARM_SPIDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SwarmSpider::getCanSpawnHere);
		SpawnPlacements.register(TFEntities.KING_SPIDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.CARMINITE_BROODLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HEDGE_SPIDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HedgeSpider::canSpawn);
		SpawnPlacements.register(TFEntities.REDCAP_SAPPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MAZE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MazeSlime::getCanSpawnHere);
		SpawnPlacements.register(TFEntities.YETI.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Yeti::yetiSnowyForestSpawnHandler);
		SpawnPlacements.register(TFEntities.ALPHA_YETI.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.WINTER_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WinterWolf::canSpawnHere);
		SpawnPlacements.register(TFEntities.SNOW_GUARDIAN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.STABLE_ICE_CORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.UNSTABLE_ICE_CORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SNOW_QUEEN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.TROLL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.GIANT_MINER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn);
		SpawnPlacements.register(TFEntities.ARMORED_GIANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn);
		SpawnPlacements.register(TFEntities.ICE_CRYSTAL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HARBINGER_CUBE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.ADHERENT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.ROVING_CUBE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.RISING_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
	}

	public static void commonInit() {
		TFDispenserBehaviors.init();
		TFStats.init();

		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_HELMET.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_CHESTPLATE.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_BOOTS.get(), CauldronInteraction.DYED_ITEM);

		AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
		AxeItem.STRIPPABLES.put(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get());
		AxeItem.STRIPPABLES.put(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get());
		AxeItem.STRIPPABLES.put(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get());
		AxeItem.STRIPPABLES.put(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get());
		AxeItem.STRIPPABLES.put(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get());
		AxeItem.STRIPPABLES.put(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
		AxeItem.STRIPPABLES.put(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get());
		AxeItem.STRIPPABLES.put(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get());

		AxeItem.STRIPPABLES.put(TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		AxeItem.STRIPPABLES.put(TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get());
		AxeItem.STRIPPABLES.put(TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		AxeItem.STRIPPABLES.put(TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get());
		AxeItem.STRIPPABLES.put(TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get());
		AxeItem.STRIPPABLES.put(TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		AxeItem.STRIPPABLES.put(TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get());
		AxeItem.STRIPPABLES.put(TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get());

		FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;

		/*
		pot.addPlant(TFBlocks.TWILIGHT_OAK_SAPLING.getId(), TFBlocks.POTTED_TWILIGHT_OAK_SAPLING);
		pot.addPlant(TFBlocks.CANOPY_SAPLING.getId(), TFBlocks.POTTED_CANOPY_SAPLING);
		pot.addPlant(TFBlocks.MANGROVE_SAPLING.getId(), TFBlocks.POTTED_MANGROVE_SAPLING);
		pot.addPlant(TFBlocks.DARKWOOD_SAPLING.getId(), TFBlocks.POTTED_DARKWOOD_SAPLING);
		pot.addPlant(TFBlocks.HOLLOW_OAK_SAPLING.getId(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING);
		pot.addPlant(TFBlocks.RAINBOW_OAK_SAPLING.getId(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING);
		pot.addPlant(TFBlocks.TIME_SAPLING.getId(), TFBlocks.POTTED_TIME_SAPLING);
		pot.addPlant(TFBlocks.TRANSFORMATION_SAPLING.getId(), TFBlocks.POTTED_TRANSFORMATION_SAPLING);
		pot.addPlant(TFBlocks.MINING_SAPLING.getId(), TFBlocks.POTTED_MINING_SAPLING);
		pot.addPlant(TFBlocks.SORTING_SAPLING.getId(), TFBlocks.POTTED_SORTING_SAPLING);
		pot.addPlant(TFBlocks.MAYAPPLE.getId(), TFBlocks.POTTED_MAYAPPLE);
		pot.addPlant(TFBlocks.FIDDLEHEAD.getId(), TFBlocks.POTTED_FIDDLEHEAD);
		pot.addPlant(TFBlocks.MUSHGLOOM.getId(), TFBlocks.POTTED_MUSHGLOOM);
		pot.addPlant(TFBlocks.BROWN_THORNS.getId(), TFBlocks.POTTED_THORN);
		pot.addPlant(TFBlocks.GREEN_THORNS.getId(), TFBlocks.POTTED_GREEN_THORN);
		pot.addPlant(TFBlocks.BURNT_THORNS.getId(), TFBlocks.POTTED_DEAD_THORN);
		*/

		FireBlock fireblock = (FireBlock) Blocks.FIRE;
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_GATE.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.CANOPY_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.CANOPY_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_CANOPY_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_CANOPY_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.CANOPY_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.CANOPY_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.CANOPY_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.CANOPY_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.CANOPY_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.CANOPY_GATE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.CANOPY_BOOKSHELF.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.MANGROVE_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.MANGROVE_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_MANGROVE_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_MANGROVE_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.MANGROVE_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MANGROVE_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MANGROVE_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MANGROVE_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MANGROVE_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MANGROVE_GATE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MANGROVE_ROOT.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.DARK_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.DARK_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_DARK_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_DARK_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.DARK_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.DARK_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.DARK_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.DARK_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.DARK_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.DARK_GATE.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.TIME_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.TIME_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_TIME_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_TIME_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.TIME_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TIME_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TIME_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TIME_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TIME_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TIME_GATE.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.TRANSFORMATION_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_GATE.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.MINING_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.MINING_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_MINING_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_MINING_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.MINING_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MINING_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MINING_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MINING_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MINING_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.MINING_GATE.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.SORTING_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.SORTING_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_SORTING_LOG.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.STRIPPED_SORTING_WOOD.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.SORTING_BANISTER.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.SORTING_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.SORTING_SLAB.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.SORTING_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.SORTING_FENCE.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.SORTING_GATE.get(), 5, 20);

		fireblock.setFlammable(TFBlocks.RASPBERRY_BUSH.get(), 4, 25);
		fireblock.setFlammable(TFBlocks.BLUEBERRY_BUSH.get(), 4, 25);
		fireblock.setFlammable(TFBlocks.BLACKBERRY_BUSH.get(), 4, 25);
		fireblock.setFlammable(TFBlocks.MALOBERRY_BUSH.get(), 4, 25);
		fireblock.setFlammable(TFBlocks.CLOVER_PATCH.get(), 60, 100);
		fireblock.setFlammable(TFBlocks.FALLEN_LEAVES.get(), 60, 100);
		fireblock.setFlammable(TFBlocks.FIDDLEHEAD.get(), 60, 100);
		fireblock.setFlammable(TFBlocks.MAYAPPLE.get(), 60, 100);
		fireblock.setFlammable(TFBlocks.MOSS_PATCH.get(), 60, 100);
		fireblock.setFlammable(TFBlocks.ROOT_STRAND.get(), 60, 100);
		fireblock.setFlammable(TFBlocks.TORCHBERRY_PLANT.get(), 60, 100);
		fireblock.setFlammable(TFBlocks.ROOT_BLOCK.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.ARCTIC_FUR_BLOCK.get(), 20, 20);
		fireblock.setFlammable(TFBlocks.LIVEROOT_BLOCK.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.CHISELED_CANOPY_BOOKSHELF.get(), 30, 20);
		fireblock.setFlammable(TFBlocks.HUGE_STALK.get(), 5, 5);

		fireblock.setFlammable(TFBlocks.TOWERWOOD.get(), 0, 1);
		fireblock.setFlammable(TFBlocks.CRACKED_TOWERWOOD.get(), 0, 1);
		fireblock.setFlammable(TFBlocks.MOSSY_TOWERWOOD.get(), 0, 1);
		fireblock.setFlammable(TFBlocks.ENCASED_TOWERWOOD.get(), 0, 1);
		fireblock.setFlammable(TFBlocks.INFESTED_TOWERWOOD.get(), 0, 1);

		fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.CANOPY_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.MANGROVE_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.DARK_LEAVES.get(), 0, 1);
		fireblock.setFlammable(TFBlocks.HARDENED_DARK_LEAVES.get(), 0, 1);
		fireblock.setFlammable(TFBlocks.TIME_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.TRANSFORMATION_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.MINING_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.SORTING_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.BEANSTALK_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.THORN_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.RAINBOW_OAK_LEAVES.get(), 30, 60);
		fireblock.setFlammable(TFBlocks.HARDENED_DARK_LEAVES.get(), 0, 1);

		fireblock.setFlammable(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.OAK_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.SPRUCE_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.BIRCH_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.JUNGLE_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.ACACIA_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.DARK_OAK_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.CRIMSON_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.WARPED_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.VANGROVE_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get(), 5, 5);
		fireblock.setFlammable(TFBlocks.CHERRY_BANISTER.get(), 5, 20);
		fireblock.setFlammable(TFBlocks.BAMBOO_BANISTER.get(), 5, 20);

		GiantToolGroupingModifier.CONVERSIONS.put(Blocks.COBBLESTONE, TFBlocks.GIANT_COBBLESTONE.get().asItem());
		GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LOG, TFBlocks.GIANT_LOG.get().asItem());
		GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LEAVES, TFBlocks.GIANT_LEAVES.get().asItem());
		GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OBSIDIAN, TFBlocks.GIANT_OBSIDIAN.get().asItem());

		JarBlockEntity.addLid(TFBlocks.MANGROVE_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.CANOPY_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.DARK_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.MINING_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.SORTING_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.TIME_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.TRANSFORMATION_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.TWILIGHT_OAK_LOG.asItem());
		JarBlockEntity.addLid(Items.ACACIA_LOG);
		JarBlockEntity.addLid(Items.BIRCH_LOG);
		JarBlockEntity.addLid(Items.CHERRY_LOG);
		JarBlockEntity.addLid(Items.DARK_OAK_LOG);
		JarBlockEntity.addLid(Items.JUNGLE_LOG);
		JarBlockEntity.addLid(Items.MANGROVE_LOG);
		JarBlockEntity.addLid(Items.OAK_LOG);
		JarBlockEntity.addLid(Items.SPRUCE_LOG);
		JarBlockEntity.addLid(Items.CRIMSON_STEM);
		JarBlockEntity.addLid(Items.WARPED_STEM);
		JarBlockEntity.addLid(TFBlocks.STRIPPED_MANGROVE_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.STRIPPED_CANOPY_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.STRIPPED_DARK_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.STRIPPED_MINING_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.STRIPPED_SORTING_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.STRIPPED_TIME_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem());
		JarBlockEntity.addLid(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem());
		JarBlockEntity.addLid(Items.STRIPPED_ACACIA_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_BIRCH_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_CHERRY_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_DARK_OAK_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_JUNGLE_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_MANGROVE_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_OAK_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_SPRUCE_LOG);
		JarBlockEntity.addLid(Items.STRIPPED_CRIMSON_STEM);
		JarBlockEntity.addLid(Items.STRIPPED_WARPED_STEM);
		JarBlockEntity.addLid(TFBlocks.CINDER_LOG.asItem());
		JarBlockEntity.addLid(Items.BAMBOO_BLOCK);
		JarBlockEntity.addLid(Items.STRIPPED_BAMBOO_BLOCK);
		JarBlockEntity.addLid(Items.PUMPKIN);
		// HolidayEvent needs migration to Fabric
	}

	public static void registerExtraStuff() {
		Registry.register(BuiltInRegistries.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
	}

	public static void createDataMaps() {
		PortingLibDataMaps.registerDataMap(TFDataMaps.CRUMBLE_HORN);
		PortingLibDataMaps.registerDataMap(TFDataMaps.TRANSFORMATION_POWDER);
		PortingLibDataMaps.registerDataMap(TFDataMaps.OMINOUS_FIRE);
		PortingLibDataMaps.registerDataMap(TFDataMaps.MAGIC_MAP_BIOME_COLOR);
		PortingLibDataMaps.registerDataMap(TFDataMaps.ORE_MAP_ORE_COLOR);
	}
}