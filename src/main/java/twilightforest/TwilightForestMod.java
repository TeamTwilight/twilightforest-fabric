package twilightforest;

import com.chocohead.mm.api.ClassTinkerers;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import io.github.fabricators_of_create.porting_lib.event.common.AddPackFindersCallback;
import io.github.fabricators_of_create.porting_lib.loot.LootModifierManager;
import io.github.fabricators_of_create.porting_lib.util.PathResourcePack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.client.particle.TFParticleType;
import twilightforest.command.TFCommand;
import twilightforest.compat.CuriosCompat;
import twilightforest.compat.TConCompat;
import twilightforest.compat.TFCompat;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.enchantment.TFEnchantments;
import twilightforest.entity.TFEntities;
import twilightforest.inventory.TFContainers;
import twilightforest.item.FieryPickItem;
import twilightforest.item.TFItems;
import twilightforest.item.recipe.TFRecipes;
import twilightforest.item.recipe.UncraftingEnabledCondition;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.potions.TFMobEffects;
import twilightforest.util.TFStats;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.biomesources.LandmarkBiomeSource;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.feature.BlockSpikeFeature;
import twilightforest.world.registration.*;
import twilightforest.world.registration.biomes.BiomeKeys;
import twilightforest.world.registration.features.TFConfiguredFeatures;
import twilightforest.world.registration.features.TFPlacedFeatures;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Consumer;

public class TwilightForestMod implements ModInitializer {

	// TODO: might be a good idea to find proper spots for all of these? also remove redundants
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
		ModConfigEvent.RELOADING.register(TFConfig::onConfigChanged);

		CommandRegistrationCallback.EVENT.register(this::registerCommands);

		BiomeKeys.BIOMES.register();
		TFBlockEntities.BLOCK_ENTITIES.register();
		TFBlocks.BLOCKS.register();
		TFContainers.CONTAINERS.register();
		TFEnchantments.ENCHANTMENTS.register();
		TFEntities.ENTITIES.register();
		TFBiomeFeatures.FEATURES.register();
		TwilightFeatures.FOLIAGE_PLACERS.register();
		TFItems.ITEMS.register();
		TFMobEffects.MOB_EFFECTS.register();
		TFNoiseGenerationSettings.NOISE_GENERATORS.register();
		TFParticleType.PARTICLE_TYPES.register();
		TwilightFeatures.PLACEMENT_MODIFIERS.register();
		TFRecipes.RECIPE_SERIALIZERS.register();
		TFRecipes.RECIPE_TYPES.register();
		//TFPotions.POTIONS.register(modbus);
		TFEntities.SPAWN_EGGS.register();
		TFStats.STATS.register();
		TFStructureProcessors.STRUCTURE_PROCESSORS.register();
		TwilightFeatures.TREE_DECORATORS.register();
		TwilightFeatures.TRUNK_PLACERS.register();
		TFBlocks.registerItemblocks();
		TFEntities.init();

		if(FabricLoader.getInstance().isModLoaded(TFCompat.UNDERGARDEN_ID)) {
//			UndergardenCompat.ENTITIES.register();
		}

		if(FabricLoader.getInstance().isModLoaded(TFCompat.TCON_ID)) {
			TConCompat.FLUIDS.register();
			TConCompat.MODIFIERS.register();
		}

		TFStructures.register();
		if(FabricLoader.getInstance().isModLoaded(TFCompat.CURIOS_ID)) {
//			Bindings.getForgeBus().get().addListener(CuriosCompat::keepCurios);
		}
		ConfiguredWorldCarvers.register();
		TFConfiguredFeatures.init();
		TFPlacedFeatures.init();
		TFStructureProcessors.init();


		// Poke these so they exist when we need them FIXME this is probably terrible design
		new BiomeGrassColors();

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				TFCompat.preInitCompat();
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading preInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		} else {
			LOGGER.warn("Skipping compatibility!");
		}



		registerSerializers();
		registerLootModifiers();
		ItemEvents.init();
		AddPackFindersCallback.EVENT.register(TwilightForestMod::addClassicPack);
		TFEventListener.init();
		TFTickHandler.init();
		init();
	}

	public static void addClassicPack(PackType packType, Consumer<RepositorySource> sources) {
		try {
			if (packType == PackType.CLIENT_RESOURCES) {
				var resourcePath = FabricLoader.getInstance().getModContainer(TwilightForestMod.ID).get().findPath("classic").get();
				var pack = new PathResourcePack(FabricLoader.getInstance().getModContainer(TwilightForestMod.ID).get().getRootPaths().get(0).getFileName() + ":" + resourcePath, resourcePath);
				var metadataSection = pack.getMetadataSection(PackMetadataSection.SERIALIZER);
				if (metadataSection != null) {
					sources.accept((packConsumer, packConstructor) ->
							packConsumer.accept(packConstructor.create(
									"builtin/twilight_forest_legacy_resources", new TextComponent("Twilight Classic"), false,
									() -> pack, metadataSection, Pack.Position.TOP, PackSource.BUILT_IN)));
				}
			}
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void registerSerializers() {
		//How do I add a condition serializer as fast as possible? An event that fires really early
		new UncraftingEnabledCondition().register();
		TFTreasure.init();

		//TODO find a better place for these? they work fine here but idk
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("landmarks"), LandmarkBiomeSource.CODEC);

		Registry.register(Registry.CHUNK_GENERATOR, TwilightForestMod.prefix("structure_locating_wrapper"), ChunkGeneratorTwilight.CODEC);
	}

	public static void registerLootModifiers() {
		Registry.register(LootModifierManager.SERIALIZER, ID + ":fiery_pick_smelting", new FieryPickItem.Serializer());
		Registry.register(LootModifierManager.SERIALIZER, ID + ":giant_block_grouping", new TFEventListener.Serializer());
	}

	public void sendIMCs() {
		TFCompat.sendIMCs();
	}

	public static void init() {
		TFPacketHandler.init();
		TFAdvancements.init();
		BiomeKeys.addBiomeTypes();

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				TFCompat.initCompat();
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading init compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				TFCompat.postInitCompat();
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading postInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		TFConfig.build();
		BlockSpikeFeature.loadStalactites();

//		evt.enqueueWork(() -> {
			TFBlocks.tfCompostables();
			TFBlocks.tfBurnables();
			TFBlocks.tfPots();
			TFSounds.registerParrotSounds();
			TFDispenserBehaviors.init();

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

	public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
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
