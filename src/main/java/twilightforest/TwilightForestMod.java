package twilightforest;

import com.chocohead.mm.api.ClassTinkerers;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadow.cloth.autoconfig.serializer.Toml4jConfigSerializerExtended;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.command.TFCommand;
import twilightforest.compat.TFCompat;
import twilightforest.compat.clothConfig.TFConfigCommon;
import twilightforest.compat.clothConfig.TFConfig;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.tileentity.TFTileEntities;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.feature.TFGenCaveStalactite;
import twilightforest.world.registration.*;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.Locale;

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

	public static TFConfigCommon COMMON_CONFIG;

	public static void commonConfigReload(){
		COMMON_CONFIG = AutoConfig.getConfigHolder(TFConfig.tfConfigCommon.getClass()).getConfig();
	}


	public void run() {
		// FIXME: safeRunWhenOn is being real jank for some reason, look into it
		//noinspection Convert2Lambda,Anonymous2MethodRef

		AutoConfig.register(TFConfigCommon.class, Toml4jConfigSerializerExtended::new);
		commonConfigReload();

		//Move this to event class as this is the event in case the player changes anything within cloth config
		AutoConfig.getConfigHolder(TFConfigCommon.class).registerLoadListener((manager, newData) -> {
			commonConfigReload();
			return InteractionResult.SUCCESS;
		});

		//This is trash
		/*
		ModLoadingContext.get().setActiveContainer(new FMLModContainer(ID), null);
		{
			final Pair<twilightforest.TFConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(twilightforest.TFConfig.Common::new);
			ModLoadingContext.get().getActiveContainer().addConfig(new ModConfig(ModConfig.Type.COMMON, specPair.getRight(), ModLoadingContext.get().getActiveContainer()));
			twilightforest.TFConfig.COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<twilightforest.TFConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(twilightforest.TFConfig.Client::new);
			ModLoadingContext.get().getActiveContainer().addConfig(new ModConfig(ModConfig.Type.CLIENT, specPair.getRight(), ModLoadingContext.get().getActiveContainer()));
			twilightforest.TFConfig.CLIENT_CONFIG = specPair.getLeft();
		}

		 */

		ASMHooks.registerMultipartEvents();
		this.registerCommands();

		//IEventBus modbus = FMLJavaModLoadingContext.getModEventBus();
//		TFBlocks.BLOCKS.register(modbus);
//		TFItems.ITEMS.register(modbus);
//		TFPotions.POTIONS.register(modbus);
//		BiomeKeys.BIOMES.register(modbus);
//		modbus.addGenericListener(SoundEvent.class, TFSounds::registerSounds);
		TFTileEntities.init();
		TFParticleType.init();
		TFStructures.registerFabricEvents();
		TFStructureProcessors.init();
		TreeConfigurations.init();
		TreeDecorators.init();
		TFStructures.register();
		//MinecraftForge.EVENT_BUS.addListener(TFStructures::load);
		TFBiomeFeatures.init();
		ConfiguredWorldCarvers.register();
		TwilightFeatures.registerPlacementConfigs();
		ConfiguredFeatures.init();
		TwilightSurfaceBuilders.register();
		registerSerializers();
		//TFContainers.CONTAINERS.register(modbus);
//		TFEnchantments.ENCHANTMENTS.register(modbus);
		// Poke these so they exist when we need them FIXME this is probably terrible design
		new TwilightFeatures();
		new BiomeGrassColors();

		if (TwilightForestMod.COMMON_CONFIG.compat) {
			try {
				TFCompat.preInitCompat();
			} catch (Exception e) {
				TwilightForestMod.COMMON_CONFIG.compat = false;
				LOGGER.error("Had an error loading preInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		} else {
			LOGGER.warn("Skipping compatibility!");
		}
	}

	public static void registerSerializers() {
		//How do I add a condition serializer as fast as possible? An event that fires really early
		//CraftingHelper.register(new UncraftingEnabledCondition.Serializer());
		TFTreasure.init();
	}

//	@SubscribeEvent
//	public static void registerLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
//		evt.getRegistry().register(new FieryPickItem.Serializer().setRegistryName(ID + ":fiery_pick_smelting"));
//		evt.getRegistry().register(new TFEventListener.Serializer().setRegistryName(ID + ":giant_block_grouping"));
//	}

//	@SubscribeEvent
//	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
//		event.register(IShieldCapability.class);
//	}

	public void sendIMCs() {
		TFCompat.IMCSender();
	}

	public static void init() {
		TFPacketHandler.init();
		TFAdvancements.init();
		BiomeKeys.addBiomeTypes();
		TFDimensions.init();
		TFBlocks.registerItemblocks();
		TFEntities.registerEntities();
		TFEntities.addEntityAttributes();
		TFEntities.registerSpawnEggs();
		TFEventListener.registerFabricEvents();

		if (TwilightForestMod.COMMON_CONFIG.compat) {
			try {
				TFCompat.initCompat();
			} catch (Exception e) {
				TwilightForestMod.COMMON_CONFIG.compat = false;
				LOGGER.error("Had an error loading init compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		if (TwilightForestMod.COMMON_CONFIG.compat) {
			try {
				TFCompat.postInitCompat();
			} catch (Exception e) {
				TwilightForestMod.COMMON_CONFIG.compat = false;
				LOGGER.error("Had an error loading postInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		//twilightforest.TFConfig.build();
		TFGenCaveStalactite.loadStalactites();

		WoodType.register(TFBlocks.TWILIGHT_OAK);
		WoodType.register(TFBlocks.CANOPY);
		WoodType.register(TFBlocks.MANGROVE);
		WoodType.register(TFBlocks.DARKWOOD);
		WoodType.register(TFBlocks.TIMEWOOD);
		WoodType.register(TFBlocks.TRANSFORMATION);
		WoodType.register(TFBlocks.MINING);
		WoodType.register(TFBlocks.SORTING);
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> TFCommand.register(dispatcher));
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
		return rarity != null ? rarity : Rarity.EPIC;
	}

	@Override
	public void onInitialize() {
		run();
		LOGGER.info("Portal Lighting: "+ TwilightForestMod.COMMON_CONFIG.portal_lighting);
		init();
		TwilightForestMod.LOGGER.info("RUNNING OK");

	}
}
