package twilightforest;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.EquipmentDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.command.TFCommand;
import twilightforest.config.ConfigSetup;
import twilightforest.dispenser.*;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.RovingCube;
import twilightforest.entity.boss.*;
import twilightforest.entity.monster.*;
import twilightforest.entity.passive.*;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.events.LootEvents;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.*;
import twilightforest.util.HolidayEvent;
import twilightforest.util.Restriction;
import twilightforest.util.TFRemapper;
import twilightforest.util.woods.WoodPalette;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.BiomeDensitySource;
import twilightforest.world.components.speleothem.StalactiteReloadListener;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Locale;

public final class TFMain implements ModInitializer {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		Reflection.initialize(ConfigSetup.class);

		TFKeyBinds.init();
		TFItems.init();
		TFStats.init();
		TFLoot.init();
		TFBlocks.init();
		TFPOITypes.init();
		TFSounds.init();
		TFGameRules.init();
		TFFeatures.init();
		TFCreativeTabs.init();
		ItemDisplays.init();
		TFMenuTypes.init();
		TFRecipes.init();
		TFEntities.init();
		TFAttributes.init();
		TFAdvancements.init();
		TFMobEffects.init();
		//TFItemSubPredicates.TYPES.register(bus); TODO: check comment
		Enforcements.init();
		TFCaveCarvers.init();
		TFDataComponents.init();
		TFMapDecorations.init();
		TFParticleType.init();
		TravellersModifierTypes.init();
		TFBlockEntities.init();
		//TFLootModifiers.LOOT_MODIFIERS.register(bus); TODO: [Fabric] check comment
		TFConsumeEffects.init();
		TFStructureTypes.init();
		BiomeLayerTypes.init();
		TFDataAttachments.init();
		TFDataSerializers.init();
		TFFeatureModifiers.init();
		TFEnchantmentEffects.init();
		TFDensityFunctions.init();
		TFStructureProcessors.init();
		TFStructurePieceTypes.init();
		ChunkBlanketProcessors.init();
		TFStructurePlacementTypes.init();
		TemplateMarkerHandlers.init();

		TFRemapper.addRegistryAliases();

		registerPackets();
		registerCustomRegistries();
		registerDynamicRegistries();
		registerBiomeSource();
		registerConfig();
		registerGiantToolDropConversions();
		registerJarLids();
		registerStrippableBlocks();
		registerFlammableBlocks();
		registerDispenseBehaviors();
		registerCommands();
		registerEntityAttributes();
		registerSpawnPlacements();
		registerValidBlockEntityTypes();
		registerReloadListeners();
		registerCauldronInteractions();
		registerItemStorage();
	}

	private static void registerPackets() {
		PayloadTypeRegistry.clientboundPlay().register(AreaProtectionPacket.TYPE, AreaProtectionPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MagicMapPacket.TYPE, MagicMapPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MazeMapPacket.TYPE, MazeMapPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MovePlayerPacket.TYPE, MovePlayerPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SpawnCharmPacket.TYPE, SpawnCharmPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StructureProtectionPacket.TYPE, StructureProtectionPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UpdateThrownPacket.TYPE, UpdateThrownPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(LifedrainParticlePacket.TYPE, LifedrainParticlePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncQuestsPacket.TYPE, SyncQuestsPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SwapHotbarPacket.TYPE, SwapHotbarPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(PerformSidestepPacket.TYPE, PerformSidestepPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(CycleMapSlotPacket.TYPE, CycleMapSlotPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(UncraftingGuiPacket.TYPE, UncraftingGuiPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(WipeOreMeterPacket.TYPE, WipeOreMeterPacket.STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, GogglesZoomPacket::handleServer);
		ServerPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, GradualGlidePacket::handleServer);
		ServerPlayNetworking.registerGlobalReceiver(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(SwapHotbarPacket.TYPE, SwapHotbarPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(PerformSidestepPacket.TYPE, PerformSidestepPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(CycleMapSlotPacket.TYPE, CycleMapSlotPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(UncraftingGuiPacket.TYPE, UncraftingGuiPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(WipeOreMeterPacket.TYPE, WipeOreMeterPacket::handle);
	}

	private static void registerCustomRegistries() {
		FabricRegistryBuilder.create(TFRegistries.Keys.ENFORCEMENT).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.CHUNK_BLANKET_TYPE).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_TYPE).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.ITEM_DISPLAY_TYPE).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.TRAVELLERS_MODIFIER_TYPE).buildAndRegister();
	}

	private static void registerDynamicRegistries() {
		DynamicRegistries.registerSynced(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC);
		DynamicRegistries.register(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC);
		DynamicRegistries.register(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors.DISPATCH_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, TemplateMarkerHandlers.DISPATCH_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlerList.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariant.DIRECT_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariant.DIRECT_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifier.CODEC);
	}

	private static void registerBiomeSource() {
		Registry.register(
			BuiltInRegistries.BIOME_SOURCE,
			TFMain.prefix("twilight_biomes"),
			TFBiomeProvider.TF_CODEC
		);
	}

	private static void registerConfig() {
		ConfigSetup.loadConfigs();
		ConfigSetup.reloadConfigs();
		ConfigSetup.syncUncraftingConfig();
	}

	private static void registerGiantToolDropConversions() {
		LootEvents.GIANT_BLOCK_CONVERSIONS.put(Blocks.COBBLESTONE, TFBlocks.GIANT_COBBLESTONE.asItem());
		LootEvents.GIANT_BLOCK_CONVERSIONS.put(Blocks.OAK_LOG, TFBlocks.GIANT_LOG.asItem());
		LootEvents.GIANT_BLOCK_CONVERSIONS.put(Blocks.OAK_LEAVES, TFBlocks.GIANT_LEAVES.asItem());
		LootEvents.GIANT_BLOCK_CONVERSIONS.put(Blocks.OBSIDIAN, TFBlocks.GIANT_OBSIDIAN.asItem());
	}

	private static void registerJarLids() {
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
		JarBlockEntity.addLid(Items.PUMPKIN, HolidayEvent.INSTANCE::isHalloweenWeek);
	}

	private static void registerStrippableBlocks() {
		StrippableBlockRegistry.register(TFBlocks.TWILIGHT_OAK_LOG, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);
		StrippableBlockRegistry.register(TFBlocks.CANOPY_LOG, TFBlocks.STRIPPED_CANOPY_LOG);
		StrippableBlockRegistry.register(TFBlocks.MANGROVE_LOG, TFBlocks.STRIPPED_MANGROVE_LOG);
		StrippableBlockRegistry.register(TFBlocks.DARK_LOG, TFBlocks.STRIPPED_DARK_LOG);
		StrippableBlockRegistry.register(TFBlocks.TIME_LOG, TFBlocks.STRIPPED_TIME_LOG);
		StrippableBlockRegistry.register(TFBlocks.TRANSFORMATION_LOG, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		StrippableBlockRegistry.register(TFBlocks.MINING_LOG, TFBlocks.STRIPPED_MINING_LOG);
		StrippableBlockRegistry.register(TFBlocks.SORTING_LOG, TFBlocks.STRIPPED_SORTING_LOG);
		StrippableBlockRegistry.register(TFBlocks.TWILIGHT_OAK_WOOD, TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD);
		StrippableBlockRegistry.register(TFBlocks.CANOPY_WOOD, TFBlocks.STRIPPED_CANOPY_WOOD);
		StrippableBlockRegistry.register(TFBlocks.MANGROVE_WOOD, TFBlocks.STRIPPED_MANGROVE_WOOD);
		StrippableBlockRegistry.register(TFBlocks.DARK_WOOD, TFBlocks.STRIPPED_DARK_WOOD);
		StrippableBlockRegistry.register(TFBlocks.TIME_WOOD, TFBlocks.STRIPPED_TIME_WOOD);
		StrippableBlockRegistry.register(TFBlocks.TRANSFORMATION_WOOD, TFBlocks.STRIPPED_TRANSFORMATION_WOOD);
		StrippableBlockRegistry.register(TFBlocks.MINING_WOOD, TFBlocks.STRIPPED_MINING_WOOD);
		StrippableBlockRegistry.register(TFBlocks.SORTING_WOOD, TFBlocks.STRIPPED_SORTING_WOOD);
	}

	private static void registerFlammableBlocks() {
		FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CANOPY_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.CANOPY_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_CANOPY_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_CANOPY_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.CANOPY_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CANOPY_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CANOPY_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CANOPY_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CANOPY_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CANOPY_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CANOPY_BOOKSHELF, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_MANGROVE_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_MANGROVE_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_ROOT, 5, 20);
		flammableBlockRegistry.add(TFBlocks.DARK_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.DARK_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_DARK_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_DARK_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_DARK_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.DARK_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.DARK_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.DARK_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.DARK_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.DARK_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.DARK_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TIME_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.TIME_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_TIME_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_TIME_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TIME_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.TIME_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TIME_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TIME_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TIME_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TIME_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TIME_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_TRANSFORMATION_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_TRANSFORMATION_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MINING_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.MINING_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_MINING_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_MINING_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_MINING_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.MINING_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MINING_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MINING_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MINING_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MINING_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.MINING_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.SORTING_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.SORTING_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_SORTING_LOG, 5, 5);
		flammableBlockRegistry.add(TFBlocks.STRIPPED_SORTING_WOOD, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.SORTING_BANISTER, 5, 5);
		flammableBlockRegistry.add(TFBlocks.SORTING_PLANKS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.SORTING_SLAB, 5, 20);
		flammableBlockRegistry.add(TFBlocks.SORTING_STAIRS, 5, 20);
		flammableBlockRegistry.add(TFBlocks.SORTING_FENCE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.SORTING_GATE, 5, 20);
		flammableBlockRegistry.add(TFBlocks.RASPBERRY_BUSH, 4, 25);
		flammableBlockRegistry.add(TFBlocks.BLUEBERRY_BUSH, 4, 25);
		flammableBlockRegistry.add(TFBlocks.BLACKBERRY_BUSH, 4, 25);
		flammableBlockRegistry.add(TFBlocks.MALOBERRY_BUSH, 4, 25);
		flammableBlockRegistry.add(TFBlocks.CLOVER_PATCH, 60, 100);
		flammableBlockRegistry.add(TFBlocks.FALLEN_LEAVES, 60, 100);
		flammableBlockRegistry.add(TFBlocks.FIDDLEHEAD, 60, 100);
		flammableBlockRegistry.add(TFBlocks.MAYAPPLE, 60, 100);
		flammableBlockRegistry.add(TFBlocks.MOSS_PATCH, 60, 100);
		flammableBlockRegistry.add(TFBlocks.ROOT_STRAND, 60, 100);
		flammableBlockRegistry.add(TFBlocks.TORCHBERRY_PLANT, 60, 100);
		flammableBlockRegistry.add(TFBlocks.ROOT_BLOCK, 5, 20);
		flammableBlockRegistry.add(TFBlocks.ARCTIC_FUR_BLOCK, 20, 20);
		flammableBlockRegistry.add(TFBlocks.LIVEROOT_BLOCK, 5, 20);
		flammableBlockRegistry.add(TFBlocks.CHISELED_CANOPY_BOOKSHELF, 30, 20);
		flammableBlockRegistry.add(TFBlocks.HUGE_STALK, 5, 5);
		flammableBlockRegistry.add(TFBlocks.TOWERWOOD, 0, 1);
		flammableBlockRegistry.add(TFBlocks.CRACKED_TOWERWOOD, 0, 1);
		flammableBlockRegistry.add(TFBlocks.MOSSY_TOWERWOOD, 0, 1);
		flammableBlockRegistry.add(TFBlocks.ENCASED_TOWERWOOD, 0, 1);
		flammableBlockRegistry.add(TFBlocks.INFESTED_TOWERWOOD, 0, 1);
		flammableBlockRegistry.add(TFBlocks.TWILIGHT_OAK_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.CANOPY_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.MANGROVE_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.DARK_LEAVES, 0, 1);
		flammableBlockRegistry.add(TFBlocks.HARDENED_DARK_LEAVES, 0, 1);
		flammableBlockRegistry.add(TFBlocks.TIME_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.TRANSFORMATION_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.MINING_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.SORTING_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.BEANSTALK_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.THORN_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.RAINBOW_OAK_LEAVES, 30, 60);
		flammableBlockRegistry.add(TFBlocks.HARDENED_DARK_LEAVES, 0, 1);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_OAK_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.OAK_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.SPRUCE_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.BIRCH_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.JUNGLE_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.ACACIA_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.DARK_OAK_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.CRIMSON_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.WARPED_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.VANGROVE_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL, 5, 5);
		flammableBlockRegistry.add(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE, 5, 5);
		flammableBlockRegistry.add(TFBlocks.CHERRY_BANISTER, 5, 20);
		flammableBlockRegistry.add(TFBlocks.BAMBOO_BANISTER, 5, 20);
	}

	private static void registerDispenseBehaviors() {
		DispenserBlock.registerBehavior(TFItems.MOONWORM_QUEEN, new DamageableStackDispenseBehavior() {
			@Override
			protected Projectile getProjectileEntity(Level level, Position position, ItemStack stack) {
				return new MoonwormShot(level, position.x(), position.y(), position.z());
			}

			@Override
			protected int getDamageAmount() {
				return 2;
			}

			@Override
			protected SoundEvent getFiredSound() {
				return TFSounds.MOONWORM_SQUISH.value();
			}
		});
		DispenserBlock.registerBehavior(TFItems.TWILIGHT_SCEPTER, new DamageableStackDispenseBehavior() {
			@Override
			protected Projectile getProjectileEntity(Level level, Position position, ItemStack stack) {
				return new TwilightWandBolt(level, position.x(), position.y(), position.z());
			}

			@Override
			protected int getDamageAmount() {
				return 1;
			}

			@Override
			protected SoundEvent getFiredSound() {
				return TFSounds.TWILIGHT_SCEPTER_USE.value();
			}

			@Override
			protected float getProjectileInaccuracy() {
				return 6.0F;
			}
		});
		DispenseItemBehavior idispenseitembehavior = EquipmentDispenseItemBehavior.INSTANCE;
		DispenserBlock.registerBehavior(TFBlocks.NAGA_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.LICH_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.MINOSHROOM_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.HYDRA_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.KNIGHT_PHANTOM_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.UR_GHAST_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.ALPHA_YETI_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.SNOW_QUEEN_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.QUEST_RAM_TROPHY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.CREEPER_SKULL_CANDLE.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.PLAYER_SKULL_CANDLE.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.SKELETON_SKULL_CANDLE.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.WITHER_SKELE_SKULL_CANDLE.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.ZOMBIE_SKULL_CANDLE.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.CICADA.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.FIREFLY.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.MOONWORM.asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFItems.PEACOCK_FEATHER_FAN.asItem(), new FeatherFanDispenseBehavior());
		DispenserBlock.registerBehavior(TFItems.CRUMBLE_HORN.asItem(), new CrumbleDispenseBehavior());
		DispenserBlock.registerBehavior(TFItems.TRANSFORMATION_POWDER.asItem(), new TransformationDispenseBehavior());
		DispenserBlock.registerProjectileBehavior(TFItems.ICE_BOMB);
		DispenserBlock.registerBehavior(Items.CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.BLACK_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.GRAY_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.LIGHT_GRAY_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.WHITE_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.RED_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.ORANGE_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.YELLOW_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.GREEN_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.LIME_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.BLUE_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.CYAN_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.LIGHT_BLUE_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.PURPLE_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.MAGENTA_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.PINK_CANDLE, new CandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.BROWN_CANDLE, new CandleDispenseBehavior());
	}

	private static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, _) -> TFCommand.INSTANCE.register(dispatcher, buildContext));
	}

	@SuppressWarnings("DataFlowIssue")
	private static void registerEntityAttributes() {
		FabricDefaultAttributeRegistry.register(TFEntities.BOAR, Boar.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.BIGHORN_SHEEP, Sheep.createAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.DEER, Deer.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.REDCAP, Redcap.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.SWARM_SPIDER, SwarmSpider.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.NAGA, Naga.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.SKELETON_DRUID, AbstractSkeleton.createAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.HOSTILE_WOLF, HostileWolf.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.WRAITH, Wraith.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.HEDGE_SPIDER, Spider.createAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.HYDRA, Hydra.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.LICH, Lich.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.PENGUIN, Penguin.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.LICH_MINION, Zombie.createAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.LOYAL_ZOMBIE, LoyalZombie.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.TINY_BIRD, TinyBird.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.SQUIRREL, Squirrel.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.DWARF_RABBIT, DwarfRabbit.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.RAVEN, Raven.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.QUEST_RAM, QuestRam.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.KOBOLD, Kobold.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.MOSQUITO_SWARM, MosquitoSwarm.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.DEATH_TOME, DeathTome.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.MINOTAUR, Minotaur.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.MINOSHROOM, Minoshroom.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.FIRE_BEETLE, FireBeetle.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.SLIME_BEETLE, SlimeBeetle.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.PINCH_BEETLE, PinchBeetle.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.MAZE_SLIME, Monster.createMonsterAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.REDCAP_SAPPER, RedcapSapper.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.MIST_WOLF, MistWolf.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.KING_SPIDER, KingSpider.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_GHASTLING, CarminiteGhastling.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_GHASTGUARD, CarminiteGhastguard.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_GOLEM, CarminiteGolem.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.TOWERWOOD_BORER, TowerwoodBorer.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.CARMINITE_BROODLING, TowerBroodling.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.UR_GHAST, UrGhast.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.BLOCKCHAIN_GOBLIN, BlockChainGoblin.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.UPPER_GOBLIN_KNIGHT, UpperGoblinKnight.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.LOWER_GOBLIN_KNIGHT, LowerGoblinKnight.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.HELMET_CRAB, HelmetCrab.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.KNIGHT_PHANTOM, KnightPhantom.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.YETI, Yeti.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.ALPHA_YETI, AlphaYeti.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.WINTER_WOLF, WinterWolf.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.SNOW_GUARDIAN, SnowGuardian.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.STABLE_ICE_CORE, StableIceCore.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.UNSTABLE_ICE_CORE, UnstableIceCore.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.SNOW_QUEEN, SnowQueen.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.TROLL, Troll.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.GIANT_MINER, GiantMiner.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.ARMORED_GIANT, GiantMiner.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.ICE_CRYSTAL, IceCrystal.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.HARBINGER_CUBE, HarbingerCube.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.ADHERENT, Adherent.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.ROVING_CUBE, RovingCube.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.PLATEAU_BOSS, PlateauBoss.registerAttributes());
		//FabricDefaultAttributeRegistry.register(TFEntities.BOGGARD, Boggard.registerAttributes());
		FabricDefaultAttributeRegistry.register(TFEntities.RISING_ZOMBIE, Zombie.createAttributes());
	}

	private static void registerSpawnPlacements() {
		SpawnPlacements.register(TFEntities.BOAR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.BIGHORN_SHEEP, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.DEER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.REDCAP, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SKELETON_DRUID, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkeletonDruid::checkDruidSpawnRules);
		SpawnPlacements.register(TFEntities.WRAITH, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Wraith::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HOSTILE_WOLF, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HostileWolf::checkWolfSpawnRules);
		SpawnPlacements.register(TFEntities.HYDRA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TFEntities.LICH, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.PENGUIN, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Penguin::canSpawn);
		SpawnPlacements.register(TFEntities.LICH_MINION, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.LOYAL_ZOMBIE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TFEntities.TINY_BIRD, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.SQUIRREL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.DWARF_RABBIT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.RAVEN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.QUEST_RAM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		SpawnPlacements.register(TFEntities.KOBOLD, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MOSQUITO_SWARM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.DEATH_TOME, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MINOTAUR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MINOSHROOM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.FIRE_BEETLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SLIME_BEETLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.PINCH_BEETLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MIST_WOLF, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.CARMINITE_GHASTLING, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastling::canSpawnHere);
		SpawnPlacements.register(TFEntities.CARMINITE_GOLEM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.TOWERWOOD_BORER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.CARMINITE_GHASTGUARD, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastguard::ghastSpawnHandler);
		SpawnPlacements.register(TFEntities.UR_GHAST, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.BLOCKCHAIN_GOBLIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.UPPER_GOBLIN_KNIGHT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.LOWER_GOBLIN_KNIGHT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HELMET_CRAB, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.KNIGHT_PHANTOM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(TFEntities.NAGA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SWARM_SPIDER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SwarmSpider::getCanSpawnHere);
		SpawnPlacements.register(TFEntities.KING_SPIDER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.CARMINITE_BROODLING, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HEDGE_SPIDER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HedgeSpider::canSpawn);
		SpawnPlacements.register(TFEntities.REDCAP_SAPPER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.MAZE_SLIME, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MazeSlime::getCanSpawnHere);
		SpawnPlacements.register(TFEntities.YETI, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Yeti::yetiSnowyForestSpawnHandler);
		SpawnPlacements.register(TFEntities.ALPHA_YETI, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.WINTER_WOLF, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WinterWolf::canSpawnHere);
		SpawnPlacements.register(TFEntities.SNOW_GUARDIAN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.STABLE_ICE_CORE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.UNSTABLE_ICE_CORE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.SNOW_QUEEN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.TROLL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.GIANT_MINER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn);
		SpawnPlacements.register(TFEntities.ARMORED_GIANT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn);
		SpawnPlacements.register(TFEntities.ICE_CRYSTAL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.HARBINGER_CUBE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.ADHERENT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.ROVING_CUBE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TFEntities.RISING_ZOMBIE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
	}

	private static void registerValidBlockEntityTypes() {
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.TWILIGHT_OAK_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.CANOPY_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.CANOPY_WALL_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.MANGROVE_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.MANGROVE_WALL_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.DARK_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.DARK_WALL_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.TIME_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.TIME_WALL_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.TRANSFORMATION_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.MINING_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.MINING_WALL_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.SORTING_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addValidBlock(TFBlocks.SORTING_WALL_HANGING_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.TWILIGHT_OAK_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.TWILIGHT_WALL_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.CANOPY_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.CANOPY_WALL_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.MANGROVE_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.MANGROVE_WALL_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.DARK_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.DARK_WALL_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.TIME_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.TIME_WALL_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.TRANSFORMATION_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.TRANSFORMATION_WALL_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.MINING_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.MINING_WALL_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.SORTING_SIGN);
		BlockEntityType.SIGN.addValidBlock(TFBlocks.SORTING_WALL_SIGN);
	}

	private static void registerReloadListeners() {
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(prefix("quest"), new QuestReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(prefix("stalactite"), new StalactiteReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(prefix("structure_template_definitions"), StructureTemplateDefinitions.INSTANCE);
	}

	private static void registerCauldronInteractions() {
		CauldronInteractions.WATER.put(TFItems.ARCTIC_HELMET, CauldronInteractions::dyedItemIteration);
		CauldronInteractions.WATER.put(TFItems.ARCTIC_CHESTPLATE, CauldronInteractions::dyedItemIteration);
		CauldronInteractions.WATER.put(TFItems.ARCTIC_LEGGINGS, CauldronInteractions::dyedItemIteration);
		CauldronInteractions.WATER.put(TFItems.ARCTIC_BOOTS, CauldronInteractions::dyedItemIteration);
	}

	private static void registerItemStorage() {
		ItemStorage.SIDED.registerForBlockEntity(
			(masonJar, side) -> side == Direction.UP ? masonJar.getItemHandler() : null,
			TFBlockEntities.MASON_JAR
		);
		ItemStorage.SIDED.registerForBlockEntity(
			(entity, side) -> entity.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER) ? null : ContainerStorage.of(entity, side),
			TFBlockEntities.CHISELED_CANOPY_BOOKSHELF
		);
	}

	public static Identifier prefix(String name) {
		return Identifier.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static Identifier getModelTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static Identifier getGuiTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static Identifier getEnvTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}
}