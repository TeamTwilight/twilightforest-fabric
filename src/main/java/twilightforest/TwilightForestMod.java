package twilightforest;

import com.google.common.collect.Maps;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadow.cloth.autoconfig.serializer.Toml4jConfigSerializerExtended;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.client.particle.TFParticleType;
import twilightforest.command.TFCommand;
import twilightforest.compat.TFCompat;
import twilightforest.compat.clothConfig.configFiles.TFConfig;
import twilightforest.compat.clothConfig.configFiles.TFConfigCommon;
import twilightforest.dispenser.CrumbleDispenseBehavior;
import twilightforest.dispenser.FeatherFanDispenseBehavior;
import twilightforest.dispenser.MoonwormDispenseBehavior;
import twilightforest.dispenser.TransformationDispenseBehavior;
import twilightforest.entity.TFEntities;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.item.TFItems;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.feature.BlockSpikeFeature;
import twilightforest.world.registration.*;
import twilightforest.world.registration.biomes.BiomeKeys;

public class TwilightForestMod implements ModInitializer {

	public static final GameRules.Key<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRuleRegistry.register("tfEnforcedProgression", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true)); //Putting it in UPDATES since other world stuff is here

	public static CreativeModeTab creativeTab = FabricItemGroupBuilder.build(new ResourceLocation(TFConstants.ID, TFConstants.ID), () -> new ItemStack(TFBlocks.twilight_portal_miniature_structure));

	public static final Logger LOGGER = LogManager.getLogger(TFConstants.ID);

	public static TFConfigCommon COMMON_CONFIG;
	public static boolean SERVER_SIDE_ONLY = true;

	@Override
	public void onInitialize() {
		TwilightForestMod();
		LOGGER.info("Portal Lighting: "+ TwilightForestMod.COMMON_CONFIG.portalLightning);
		init();
		TwilightForestMod.LOGGER.info("RUNNING OK");

	}

	public void TwilightForestMod() {
		// FIXME: safeRunWhenOn is being real jank for some reason, look into it
		//noinspection Convert2Lambda,Anonymous2MethodRef

		//Cloth Config Setup
		clothConfigSetup();

		registerCommands();

		TFBlocks.registerItemblocks();
		TFItems.init();
		//TFPotions.POTIONS.register(modbus);
		BiomeKeys.init();
		//modbus.addGenericListener(SoundEvent.class, TFSounds::registerSounds);
		TFBlockEntities.init();
		TFParticleType.init();
		TFStructures.registerFabricEvents();
		TFBiomeFeatures.init();
		//TFContainers.CONTAINERS.register(modbus);
		//TFEnchantments.ENCHANTMENTS.register(modbus);

		{
			TFStructureProcessors.init();
			TreeConfigurations.init();
			TreeDecorators.init();
			TFStructures.register();
			ConfiguredWorldCarvers.register();
			TwilightFeatures.registerPlacementConfigs();
			ConfiguredFeatures.init();
			TwilightSurfaceBuilders.register();
			registerSerializers();
		}

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

	public static void registerSerializers() {
		//How do I add a condition serializer as fast as possible? An event that fires really early
		//CraftingHelper.register(new UncraftingEnabledCondition.Serializer());
		TFTreasure.init();
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> TFCommand.register(dispatcher));
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

		{
			TFEntities.registerEntities();
			TFEntities.addEntityAttributes();
			TFEntities.registerSpawnEggs();
		}

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

		BlockSpikeFeature.loadStalactites();

		{
			TFBlocks.tfCompostables();
			TFBlocks.TFBurnables();
			TFBlocks.TFPots();
			TFSounds.registerParrotSounds();

			AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
			AxeItem.STRIPPABLES.put(TFBlocks.oak_log, TFBlocks.stripped_oak_log);
			AxeItem.STRIPPABLES.put(TFBlocks.canopy_log, TFBlocks.stripped_canopy_log);
			AxeItem.STRIPPABLES.put(TFBlocks.mangrove_log, TFBlocks.stripped_mangrove_log);
			AxeItem.STRIPPABLES.put(TFBlocks.dark_log, TFBlocks.stripped_dark_log);
			AxeItem.STRIPPABLES.put(TFBlocks.time_log, TFBlocks.stripped_time_log);
			AxeItem.STRIPPABLES.put(TFBlocks.transformation_log, TFBlocks.stripped_transformation_log);
			AxeItem.STRIPPABLES.put(TFBlocks.mining_log, TFBlocks.stripped_mining_log);
			AxeItem.STRIPPABLES.put(TFBlocks.sorting_log, TFBlocks.stripped_sorting_log);

			AxeItem.STRIPPABLES.put(TFBlocks.oak_wood, TFBlocks.stripped_oak_wood);
			AxeItem.STRIPPABLES.put(TFBlocks.canopy_wood, TFBlocks.stripped_canopy_wood);
			AxeItem.STRIPPABLES.put(TFBlocks.mangrove_wood, TFBlocks.stripped_mangrove_wood);
			AxeItem.STRIPPABLES.put(TFBlocks.dark_wood, TFBlocks.stripped_dark_wood);
			AxeItem.STRIPPABLES.put(TFBlocks.time_wood, TFBlocks.stripped_time_wood);
			AxeItem.STRIPPABLES.put(TFBlocks.transformation_wood, TFBlocks.stripped_transformation_wood);
			AxeItem.STRIPPABLES.put(TFBlocks.mining_wood, TFBlocks.stripped_mining_wood);
			AxeItem.STRIPPABLES.put(TFBlocks.sorting_wood, TFBlocks.stripped_sorting_wood);

			DispenserBlock.registerBehavior(TFItems.moonworm_queen, new MoonwormDispenseBehavior() {
				@Override
				protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
					return new MoonwormShot(worldIn, position.x(), position.y(), position.z());
				}
			});

			DispenseItemBehavior idispenseitembehavior = new OptionalDispenseItemBehavior() {
				/**
				 * Dispense the specified stack, play the dispense sound and spawn particles.
				 */
				protected ItemStack execute(BlockSource source, ItemStack stack) {
					this.setSuccess(ArmorItem.dispenseArmor(source, stack));
					return stack;
				}
			};
			DispenserBlock.registerBehavior(TFBlocks.naga_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.lich_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.minoshroom_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.hydra_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.knight_phantom_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.ur_ghast_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.snow_queen_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.quest_ram_trophy.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.cicada.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.firefly.asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.moonworm.asItem(), idispenseitembehavior);

			DispenseItemBehavior pushmobsbehavior = new FeatherFanDispenseBehavior();
			DispenserBlock.registerBehavior(TFItems.peacock_fan.asItem(), pushmobsbehavior);

			DispenseItemBehavior crumblebehavior = new CrumbleDispenseBehavior();
			DispenserBlock.registerBehavior(TFItems.crumble_horn.asItem(), crumblebehavior);

			DispenseItemBehavior transformbehavior = new TransformationDispenseBehavior();
			DispenserBlock.registerBehavior(TFItems.transformation_powder.asItem(), transformbehavior);

			DispenserBlock.registerBehavior(TFItems.twilight_scepter, new MoonwormDispenseBehavior() {
				@Override
				protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
					return new TwilightWandBolt(worldIn, position.x(), position.y(), position.z());
				}

				@Override
				protected void playSound(BlockSource source) {
					BlockPos pos = source.getPos();
					source.getLevel().playSound(null, pos, TFSounds.SCEPTER_PEARL, SoundSource.BLOCKS, 1, 1);
				}
			});
			WoodType.register(TFBlocks.TWILIGHT_OAK);
			WoodType.register(TFBlocks.CANOPY);
			WoodType.register(TFBlocks.MANGROVE);
			WoodType.register(TFBlocks.DARKWOOD);
			WoodType.register(TFBlocks.TIMEWOOD);
			WoodType.register(TFBlocks.TRANSFORMATION);
			WoodType.register(TFBlocks.MINING);
			WoodType.register(TFBlocks.SORTING);
		}
	}

	//-----FABRIC ONLY METHODS-----
	public static void commonConfigInit(){
		if(SERVER_SIDE_ONLY){
			COMMON_CONFIG = AutoConfig.getConfigHolder(TFConfigCommon.class).getConfig();
		}

		else{
			COMMON_CONFIG = AutoConfig.getConfigHolder(TFConfig.class).getConfig().tfConfigCommon;
		}
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
}
