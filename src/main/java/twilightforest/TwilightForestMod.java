package twilightforest;

import com.chocohead.mm.api.ClassTinkerers;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadow.cloth.autoconfig.serializer.Toml4jConfigSerializerExtended;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.command.TFCommand;
import twilightforest.compat.TFCompat;
import twilightforest.compat.clothConfig.configFiles.TFConfigCommon;
import twilightforest.compat.clothConfig.configFiles.TFConfig;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.tileentity.TFTileEntities;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.feature.TFGenCaveStalactite;
import twilightforest.world.registration.*;
import twilightforest.world.registration.biomes.BiomeKeys;

public class TwilightForestMod implements ModInitializer {

	public static final GameRules.Key<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRuleRegistry.register("tfEnforcedProgression", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true)); //Putting it in UPDATES since other world stuff is here

	public static CreativeModeTab creativeTab = FabricItemGroupBuilder.build(new ResourceLocation(TFConstants.ID, TFConstants.ID), () -> new ItemStack(TFBlocks.twilight_portal_miniature_structure));

	public static final Logger LOGGER = LogManager.getLogger(TFConstants.ID);

	public static TFConfigCommon COMMON_CONFIG;
	public static boolean SERVER_SIDE_ONLY = true;

	public static void commonConfigInit(){
		if(SERVER_SIDE_ONLY){
			COMMON_CONFIG = AutoConfig.getConfigHolder(TFConfigCommon.class).getConfig();
		}

		else{
			COMMON_CONFIG = AutoConfig.getConfigHolder(TFConfig.class).getConfig().tfConfigCommon;
		}
	}

	@Override
	public void onInitialize() {
		run();
		LOGGER.info("Portal Lighting: "+ TwilightForestMod.COMMON_CONFIG.portalLightning);
		init();
		TwilightForestMod.LOGGER.info("RUNNING OK");

	}

	public void run() {
		// FIXME: safeRunWhenOn is being real jank for some reason, look into it
		//noinspection Convert2Lambda,Anonymous2MethodRef

		clothConfigSetup();

		registerCommands();

		//TFPotions.POTIONS.register(modbus);
		//BiomeKeys.BIOMES.register(modbus);
		//modbus.addGenericListener(SoundEvent.class, TFSounds::registerSounds);

		TFStructures.registerFabricEvents();
		TFStructureProcessors.init();

		TreeConfigurations.init();
		TreeDecorators.init();
		TFStructures.register();
		TFBiomeFeatures.init();
		ConfiguredWorldCarvers.register();
		TwilightFeatures.registerPlacementConfigs();
		ConfiguredFeatures.init();
		TwilightSurfaceBuilders.register();
		registerSerializers();
		//TFContainers.CONTAINERS.register(modbus);
		//TFEnchantments.ENCHANTMENTS.register(modbus);

		// Poke these so they exist when we need them FIXME this is probably terrible design
		new TwilightFeatures();
		new BiomeGrassColors();

		if (TwilightForestMod.COMMON_CONFIG.doCompat) {
			try {
				TFCompat.preInitCompat();
			} catch (Exception e) {
				TwilightForestMod.COMMON_CONFIG.doCompat = false;
				LOGGER.error("Had an error loading preInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		} else {
			LOGGER.warn("Skipping compatibility!");
		}
	}

	public static void init() {
		TFPacketHandler.init();
		TFAdvancements.init();
		TFParticleType.init();

		BiomeKeys.addBiomeTypes();
		TFDimensions.init();

		TFTileEntities.init();
		TFBlocks.registerItemblocks();
		TFItems.init();

		TFEntities.registerEntities();
		TFEntities.addEntityAttributes();
		TFEntities.registerSpawnEggs();

		TFEventListener.registerFabricEvents();

		if (TwilightForestMod.COMMON_CONFIG.doCompat) {
			try {
				TFCompat.initCompat();
			} catch (Exception e) {
				TwilightForestMod.COMMON_CONFIG.doCompat = false;
				LOGGER.error("Had an error loading init compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		if (TwilightForestMod.COMMON_CONFIG.doCompat) {
			try {
				TFCompat.postInitCompat();
			} catch (Exception e) {
				TwilightForestMod.COMMON_CONFIG.doCompat = false;
				LOGGER.error("Had an error loading postInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

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

	public void clothConfigSetup(){
		AutoConfig.register(TFConfigCommon.class, Toml4jConfigSerializerExtended::new);
		commonConfigInit();

		ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
			if(SERVER_SIDE_ONLY) {
				AutoConfig.getConfigHolder(TFConfigCommon.class).registerLoadListener((manager, newData) -> {
					COMMON_CONFIG = newData;
					//COMMON_CONFIG = AutoConfig.getConfigHolder(newData.getClass()).getConfig();
					LOGGER.debug("Test: The TFConfigCommon has be reload after a load event!");
					return InteractionResult.SUCCESS;
				});

				AutoConfig.getConfigHolder(TFConfigCommon.class).registerSaveListener((manager, newData) -> {
					COMMON_CONFIG = newData;
					LOGGER.debug("Test: The TFConfigCommon has be reload after a save event!");
					//COMMON_CONFIG = AutoConfig.getConfigHolder(newData.getClass()).getConfig();
					return InteractionResult.SUCCESS;
				});
			}
		});
	}

	public static void registerSerializers() {
		//How do I add a condition serializer as fast as possible? An event that fires really early
		//CraftingHelper.register(new UncraftingEnabledCondition.Serializer());
		TFTreasure.init();
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> TFCommand.register(dispatcher));
	}

	public void sendIMCs() {
		TFCompat.IMCSender();
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
}
