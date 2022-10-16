package twilightforest;

import com.chocohead.mm.api.ClassTinkerers;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.command.TFCommand;
import twilightforest.compat.trinkets.TrinketsCompat;
import twilightforest.compat.top.TopCompat;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.events.*;
import twilightforest.init.*;
import twilightforest.init.custom.DwarfRabbitVariant;
import twilightforest.init.custom.TinyBirdVariant;
import twilightforest.network.TFPacketHandler;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.biomesources.LandmarkBiomeSource;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFStructureProcessors;

import java.io.IOException;
import java.util.Locale;

public class TwilightForestMod implements ModInitializer {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/model/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";
	// odd one out, as armor textures are a stringy mess at present
	public static final String ARMOR_DIR = ID + ":textures/armor/";

	public static final GameRules.Key<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRuleRegistry.register("tfEnforcedProgression", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true)); //Putting it in UPDATES since other world stuff is here

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final Rarity rarity = ClassTinkerers.getEnum(Rarity.class, "TWILIGHT");

	@Override
	public void onInitialize() {
		{
			final Pair<TFConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TFConfig.Common::new);
			ModLoadingContext.registerConfig(ID, ModConfig.Type.COMMON, specPair.getRight());
			TFConfig.COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TFConfig.Client::new);
			ModLoadingContext.registerConfig(ID, ModConfig.Type.CLIENT, specPair.getRight());
			TFConfig.CLIENT_CONFIG = specPair.getLeft();
		}

		CommandRegistrationCallback.EVENT.register(this::registerCommands);
		MinecraftForge.EVENT_BUS.addListener(Stalactite::reloadStalactites);

		TFBannerPatterns.BANNER_PATTERNS.register();
		BiomeKeys.BIOMES.register();
		TFBlockEntities.BLOCK_ENTITIES.register();
		TFBlocks.BLOCKS.register();
		TFLoot.CONDITIONS.register();
		TFMenuTypes.CONTAINERS.register();
		TFDimensionSettings.DIMENSION_TYPES.register();
		TFEnchantments.ENCHANTMENTS.register();
		TFEntities.ENTITIES.register();
		TFFeatures.FEATURES.register();
		TFFeatureModifiers.FOLIAGE_PLACERS.register();
		TFLoot.FUNCTIONS.register();
		TFItems.ITEMS.register();
		TFLootModifiers.LOOT_MODIFIERS.register();
		TFMobEffects.MOB_EFFECTS.register();
		TFDimensionSettings.NOISE_GENERATORS.register();
		TFParticleType.PARTICLE_TYPES.register();
		TFFeatureModifiers.PLACEMENT_MODIFIERS.register();
		TFRecipes.RECIPE_SERIALIZERS.register();
		TFRecipes.RECIPE_TYPES.register();
		//TFPotions.POTIONS.register(modbus);
		TFSounds.SOUNDS.register();
		TFEntities.SPAWN_EGGS.register();
		TFStats.STATS.register();
		TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register();
		TFStructureProcessors.STRUCTURE_PROCESSORS.register();
		TFStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES.register();
		TFStructureSets.STRUCTURE_SETS.register();
		TFStructureTypes.STRUCTURE_TYPES.register();
		TFStructures.STRUCTURES.register();
		TFFeatureModifiers.TREE_DECORATORS.register();
		TFFeatureModifiers.TRUNK_PLACERS.register();
		TFBlocks.registerItemblocks();
		TFEntities.init();

		DwarfRabbitVariant.DWARF_RABBITS.register();
		TinyBirdVariant.TINY_BIRDS.register();

		TFStructures.register();
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			TrinketsCompat.init();
		}
		ConfiguredWorldCarvers.register();
		TFConfiguredFeatures.init();
		TFPlacedFeatures.init();
		TFStructureProcessors.init();
		registerSerializers();


		new BiomeGrassColors();

		addClassicPack();
		initEvents();
		init();
	}

	public static void initEvents() {
		TFTickHandler.init();
		PlayerEvents.init();
		ProgressionEvents.init();
		MiscEvents.init();
		HostileMountEvents.init();
		CharmEvents.init();
		ToolEvents.init();
	}

	public static void addClassicPack() {
		ModContainer tf = FabricLoader.getInstance().getModContainer(ID)
				.orElseThrow(() -> new IllegalStateException("Twilight Forest's ModContainer couldn't be found!"));
		ResourceLocation packId = prefix("classic");
		ResourceManagerHelper.registerBuiltinResourcePack(packId, tf, "Twilight Classic", ResourcePackActivationType.NORMAL);
	}

	public static void registerSerializers() {
			Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
			Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("landmarks"), LandmarkBiomeSource.CODEC);

			Registry.register(Registry.CHUNK_GENERATOR, TwilightForestMod.prefix("structure_locating_wrapper"), ChunkGeneratorTwilight.CODEC);
	}

	public static void init() {
		TFPacketHandler.init();
		TFAdvancements.init();

//		evt.enqueueWork(() -> {
			TFBlocks.tfCompostables();
			TFBlocks.tfBurnables();
			TFBlocks.tfPots();
			TFEntities.registerSpawnPlacements();
			TFSounds.registerParrotSounds();
			TFDispenserBehaviors.init();
			TFStats.init();

			WoodType.register(TFBlocks.TWILIGHT_OAK);
			WoodType.register(TFBlocks.CANOPY);
			WoodType.register(TFBlocks.MANGROVE);
			WoodType.register(TFBlocks.DARKWOOD);
			WoodType.register(TFBlocks.TIMEWOOD);
			WoodType.register(TFBlocks.TRANSFORMATION);
			WoodType.register(TFBlocks.MINING);
			WoodType.register(TFBlocks.SORTING);

			CauldronInteraction.WATER.put(TFItems.ARCTIC_HELMET.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_CHESTPLATE.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_BOOTS.get(), CauldronInteraction.DYED_ITEM);

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
//		});
	}

	public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
		TFCommand.register(dispatcher);
	}

	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(ID, name.toLowerCase(Locale.ROOT));
	}

	public static ResourceLocation getModelTexture(String name) {
		return new ResourceLocation(ID, MODEL_DIR + name);
	}

	public static ResourceLocation getGuiTexture(String name) {
		return new ResourceLocation(ID, GUI_DIR + name);
	}

	public static ResourceLocation getEnvTexture(String name) {
		return new ResourceLocation(ID, ENVIRO_DIR + name);
	}

	public static Rarity getRarity() {
		return rarity;
	}
}
