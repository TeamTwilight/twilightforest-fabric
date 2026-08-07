package twilightforest;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.ComposterBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.config.ConfigSetup;
import twilightforest.events.*;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import io.github.fabricators_of_create.porting_lib.util.DeferredSpawnEggItem;
import twilightforest.mixin.ParrotAccessor;
import twilightforest.util.TFBoatTypes;
import twilightforest.util.TFRemapper;

import java.util.Locale;
import java.util.Map;

public final class TwilightForestMod implements ModInitializer {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final Logger LOGGER = LogManager.getLogger(ID);

	@Override
	public void onInitialize() {
		Reflection.initialize(ConfigSetup.class);

		initializeEvents();

		TFGameRules.register();

		// Add registry aliases BEFORE any DeferredRegister.register() calls
		TFRemapper.addRegistryAliases();

		TFArmorMaterials.ARMOR_MATERIALS.register();
		TFDataComponents.COMPONENTS.register();
		TFBlocks.BLOCKS.register();

		TFBoatTypes.init();

		TFItems.ITEMS.register();
		TFStats.STATS.register();
		TFLoot.NUMBERS.register();
		TFPOITypes.POIS.register();
		TFSounds.SOUNDS.register();
		TFLoot.FUNCTIONS.register();
		TFLoot.CONDITIONS.register();
		TFEntities.ENTITIES.register();
		TFFeatures.FEATURES.register();
		TFCreativeTabs.TABS.register();
		TFLoot.CONDITIONALS.register();
		TFEntities.SPAWN_EGGS.register();
		ItemDisplays.DISPLAYS.register();
		TFMenuTypes.CONTAINERS.register();
		TFRecipes.RECIPE_TYPES.register();
		TFAttributes.ATTRIBUTES.register();
		TFAdvancements.TRIGGERS.register();
		TFMobEffects.MOB_EFFECTS.register();
		TFItemSubPredicates.TYPES.register();
		Enforcements.ENFORCEMENTS.register();
		TFCaveCarvers.CARVER_TYPES.register();
		TFRecipes.RECIPE_SERIALIZERS.register();
		TFMapDecorations.DECORATIONS.register();
		TFParticleType.PARTICLE_TYPES.register();
		TravellersModifierTypes.TYPES.register();
		TFBlockEntities.BLOCK_ENTITIES.register();
		TFLootModifiers.LOOT_MODIFIERS.register();
		TFStructureTypes.STRUCTURE_TYPES.register();
		TFFeatureModifiers.TRUNK_PLACERS.register();
		BiomeLayerTypes.BIOME_LAYER_TYPES.register();
		TFDataAttachments.register();
		TFDataSerializers.init();
		TFFeatureModifiers.FOLIAGE_PLACERS.register();
		TFFeatureModifiers.TREE_DECORATORS.register();
		TFEnchantmentEffects.ENTITY_EFFECTS.register();
		TFFeatureModifiers.PLACEMENT_MODIFIERS.register();
		TFDensityFunctions.DENSITY_FUNCTION_TYPES.register();
		TFStructureProcessors.STRUCTURE_PROCESSORS.register();
		TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register();
		ChunkBlanketProcessors.CHUNK_BLANKETING_TYPES.register();
		TFStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES.register();
		TemplateMarkerHandlers.TEMPLATE_MARKER_HANDLER_TYPES.register();

		// Initialize deferred spawn eggs (dispenser behaviors and type mapping)
		DeferredSpawnEggItem.init();

		RegistrationEvents.registerPackets();
		RegistrationEvents.registerServerPacketHandlers();
		RegistrationEvents.addEntityAttributes();
		RegistrationEvents.registerSpawnPlacements();
		RegistrationEvents.commonInit();
		RegistrationEvents.registerExtraStuff();
		RegistrationEvents.createDataMaps();
		RegistrationEvents.setRegistriesForDatapack();

		// Inject the original wooden drying racks, railings, etc. into the original Creative Mode tab (refer to NeoForge version addToTabs)
		TFCreativeTabs.addCreativeTabEntries();

		// Register grass color modifiers for client-side rendering (must be called at mod init, not just datapack bootstrap)
		TFBiomes.registerGrassColorModifiers();

		// Compat mods - will be handled via Fabric's mod loading
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			// Trinkets compat will be registered via fabric.mod.json entrypoints
			LOGGER.info("Trinkets detected, loading compat");
		}

		// Register compostables (migrated from NeoForge DataMaps)
		registerCompostables();

		// Register furnace fuels (migrated from NeoForge DataMaps)
		registerFurnaceFuels();

		// Register parrot imitations (migrated from NeoForge DataMaps)
		try {
			registerParrotImitations();
		} catch (Exception e) {
			LOGGER.warn("Failed to register parrot imitations: {}", e.getMessage());
		}
	}

	public static ResourceLocation prefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static ResourceLocation getModelTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static ResourceLocation getGuiTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static ResourceLocation getEnvTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}

	private void initializeEvents() {
		CapabilityEvents.init();
		CharmEvents.init();
		EntityEvents.init();
		HostileMountEvents.init();
		MiscEvents.init();
		ProgressionEvents.init();
		RegistrationEvents.init();
		ToolEvents.init();
		TravellersGearEvents.init();
	}

	private void registerCompostables() {
		// Leaves - 0.3
		ComposterBlock.COMPOSTABLES.put(TFBlocks.FALLEN_LEAVES.asItem(), 0.1F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.CANOPY_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.CLOVER_PATCH.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.DARK_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.FIDDLEHEAD.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HEDGE.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MANGROVE_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MAYAPPLE.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MINING_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TWILIGHT_OAK_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.RAINBOW_OAK_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.ROOT_STRAND.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.SORTING_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.THORN_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TIME_LEAVES.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TRANSFORMATION_LEAVES.asItem(), 0.3F);
		// Saplings - 0.3
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TWILIGHT_OAK_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.CANOPY_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MANGROVE_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.DARKWOOD_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.RAINBOW_OAK_SAPLING.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.TORCHBERRIES.get(), 0.3F);
		// Berries - 0.3
		ComposterBlock.COMPOSTABLES.put(TFItems.RASPBERRY.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.BLUEBERRY.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.BLACKBERRY.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.MALOBERRY.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.BLIGHTBERRY.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.DUSKBERRY.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.SKYBERRY.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(TFItems.STINGBERRY.get(), 0.3F);
		// 0.5
		ComposterBlock.COMPOSTABLES.put(TFBlocks.BEANSTALK_LEAVES.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MOSS_PATCH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.ROOT_BLOCK.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.THORN_ROSE.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TROLLVIDR.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HOLLOW_OAK_SAPLING.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TIME_SAPLING.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TRANSFORMATION_SAPLING.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MINING_SAPLING.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.SORTING_SAPLING.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TORCHBERRY_PLANT.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFItems.LIVEROOT.get(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.RASPBERRY_BUSH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.BLUEBERRY_BUSH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.BLACKBERRY_BUSH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MALOBERRY_BUSH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.BLIGHTBERRY_BUSH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.DUSKBERRY_BUSH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.SKYBERRY_BUSH.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.STINGBERRY_BUSH.asItem(), 0.5F);
		// 0.65
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_MUSHGLOOM_STEM.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_WATER_LILY.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.LIVEROOT_BLOCK.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MUSHGLOOM.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.UBEROUS_SOIL.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_STALK.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.UNRIPE_TROLLBER.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TROLLBER.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(TFItems.MAZE_WAFER.get(), 0.65F);
		// 0.85
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_LILY_PAD.asItem(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_MUSHGLOOM.asItem(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(TFItems.EXPERIMENT_115.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(TFItems.MAGIC_BEANS.get(), 0.85F);
	}

	private void registerFurnaceFuels() {
		// Banisters burn for 300 ticks (same as wooden tools)
		FuelRegistry.INSTANCE.add(TFBlocks.OAK_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.SPRUCE_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.BIRCH_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.JUNGLE_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.ACACIA_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.DARK_OAK_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.CRIMSON_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.WARPED_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.VANGROVE_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.BAMBOO_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.CHERRY_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.TWILIGHT_OAK_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.CANOPY_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.MANGROVE_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.DARK_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.TIME_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.TRANSFORMATION_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.MINING_BANISTER.asItem(), 300);
		FuelRegistry.INSTANCE.add(TFBlocks.SORTING_BANISTER.asItem(), 300);
	}

	private void registerParrotImitations() {
		try {
			Map<EntityType<?>, SoundEvent> map = ParrotAccessor.twilightforest$getMobSoundMap();
			map.put(TFEntities.ALPHA_YETI.get(), TFSounds.ALPHA_YETI_PARROT.get());
			map.put(TFEntities.BLOCKCHAIN_GOBLIN.get(), TFSounds.REDCAP_PARROT.get());
			map.put(TFEntities.CARMINITE_BROODLING.get(), SoundEvents.PARROT_IMITATE_SPIDER);
			map.put(TFEntities.CARMINITE_GOLEM.get(), TFSounds.CARMINITE_GOLEM_PARROT.get());
			map.put(TFEntities.FIRE_BEETLE.get(), SoundEvents.PARROT_IMITATE_SPIDER);
			map.put(TFEntities.CARMINITE_GHASTLING.get(), SoundEvents.PARROT_IMITATE_GHAST);
			map.put(TFEntities.CARMINITE_GHASTGUARD.get(), SoundEvents.PARROT_IMITATE_GHAST);
			map.put(TFEntities.HEDGE_SPIDER.get(), SoundEvents.PARROT_IMITATE_SPIDER);
			map.put(TFEntities.HELMET_CRAB.get(), SoundEvents.PARROT_IMITATE_SPIDER);
			map.put(TFEntities.HOSTILE_WOLF.get(), TFSounds.HOSTILE_WOLF_PARROT.get());
			map.put(TFEntities.HYDRA.get(), TFSounds.HYDRA_PARROT.get());
			map.put(TFEntities.STABLE_ICE_CORE.get(), TFSounds.ICE_CORE_PARROT.get());
			map.put(TFEntities.KING_SPIDER.get(), SoundEvents.PARROT_IMITATE_SPIDER);
			map.put(TFEntities.KOBOLD.get(), TFSounds.KOBOLD_PARROT.get());
			map.put(TFEntities.LICH.get(), SoundEvents.PARROT_IMITATE_BLAZE);
			map.put(TFEntities.MAZE_SLIME.get(), SoundEvents.PARROT_IMITATE_SLIME);
			map.put(TFEntities.LICH_MINION.get(), SoundEvents.PARROT_IMITATE_ZOMBIE);
			map.put(TFEntities.MINOSHROOM.get(), TFSounds.MINOTAUR_PARROT.get());
			map.put(TFEntities.MINOTAUR.get(), TFSounds.MINOTAUR_PARROT.get());
			map.put(TFEntities.MIST_WOLF.get(), TFSounds.HOSTILE_WOLF_PARROT.get());
			map.put(TFEntities.MOSQUITO_SWARM.get(), TFSounds.MOSQUITO_PARROT.get());
			map.put(TFEntities.NAGA.get(), TFSounds.NAGA_PARROT.get());
			map.put(TFEntities.KNIGHT_PHANTOM.get(), TFSounds.WRAITH_PARROT.get());
			map.put(TFEntities.PINCH_BEETLE.get(), SoundEvents.PARROT_IMITATE_SPIDER);
			map.put(TFEntities.REDCAP.get(), TFSounds.REDCAP_PARROT.get());
			map.put(TFEntities.REDCAP_SAPPER.get(), TFSounds.REDCAP_PARROT.get());
			map.put(TFEntities.SKELETON_DRUID.get(), SoundEvents.PARROT_IMITATE_SKELETON);
			map.put(TFEntities.SLIME_BEETLE.get(), SoundEvents.PARROT_IMITATE_SLIME);
			map.put(TFEntities.SNOW_GUARDIAN.get(), TFSounds.ICE_CORE_PARROT.get());
			map.put(TFEntities.SNOW_QUEEN.get(), TFSounds.ICE_CORE_PARROT.get());
			map.put(TFEntities.SWARM_SPIDER.get(), SoundEvents.PARROT_IMITATE_SPIDER);
			map.put(TFEntities.TOWERWOOD_BORER.get(), SoundEvents.PARROT_IMITATE_SILVERFISH);
			map.put(TFEntities.DEATH_TOME.get(), TFSounds.DEATH_TOME_PARROT.get());
			map.put(TFEntities.UR_GHAST.get(), SoundEvents.PARROT_IMITATE_GHAST);
			map.put(TFEntities.WINTER_WOLF.get(), TFSounds.HOSTILE_WOLF_PARROT.get());
			map.put(TFEntities.WRAITH.get(), TFSounds.WRAITH_PARROT.get());
			map.put(TFEntities.YETI.get(), TFSounds.ALPHA_YETI_PARROT.get());
		} catch (AssertionError e) {
			LOGGER.error("Failed to register parrot imitations", e);
		}
	}
}