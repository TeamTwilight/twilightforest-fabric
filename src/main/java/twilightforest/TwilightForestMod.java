package twilightforest;

import com.chocohead.mm.api.ClassTinkerers;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.command.TFCommand;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.events.*;
import twilightforest.init.*;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.init.custom.DwarfRabbitVariant;
import twilightforest.init.custom.Enforcement;
import twilightforest.init.custom.MagicPaintingVariants;
import twilightforest.init.custom.Restrictions;
import twilightforest.init.custom.TinyBirdVariant;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.item.recipe.UncraftingTableCondition;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateGamerulePacket;
import twilightforest.util.MagicPaintingVariant;
import twilightforest.util.Restriction;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.biomesources.LandmarkBiomeSource;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;

import java.util.Locale;

public class TwilightForestMod implements ModInitializer {

	public static final String ID = "twilightforest";
	public static final String REGISTRY_NAMESPACE = "twilight";

	private static final String MODEL_DIR = "textures/model/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";
	// odd one out, as armor textures are a stringy mess at present
	public static final String ARMOR_DIR = ID + ":textures/armor/";

	public static final GameRules.Key<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRuleRegistry.register("tfEnforcedProgression", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true, (server, enforced) -> TFPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateGamerulePacket(enforced.get())))); //Putting it in UPDATES since other world stuff is here

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final Rarity rarity = ClassTinkerers.getEnum(Rarity.class, "TWILIGHT");

	@Override
	public void onInitialize() {
		{
			final Pair<TFConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TFConfig.Common::new);
			ForgeConfigRegistry.INSTANCE.register(ID, ModConfig.Type.COMMON, specPair.getRight());
			TFConfig.COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TFConfig.Client::new);
			ForgeConfigRegistry.INSTANCE.register(ID, ModConfig.Type.CLIENT, specPair.getRight());
			TFConfig.CLIENT_CONFIG = specPair.getLeft();
		}

		CommandRegistrationCallback.EVENT.register(this::registerCommands);
		Stalactite.reloadStalactites();

		TFBannerPatterns.BANNER_PATTERNS.register();
		TFSounds.SOUNDS.register();
		TFBlocks.BLOCKS.register();
		TFBlockEntities.BLOCK_ENTITIES.register();
		TFLoot.CONDITIONS.register();
		TFMenuTypes.CONTAINERS.register();
		TFEnchantments.ENCHANTMENTS.register();
		TFEntities.ENTITIES.register();
		BiomeLayerTypes.BIOME_LAYER_TYPES.register();
		BiomeLayerStack.BIOME_LAYER_STACKS.register();
		TFFeatures.FEATURES.register();
		TFFeatureModifiers.FOLIAGE_PLACERS.register();
		TFLoot.FUNCTIONS.register();
		TFItems.ITEMS.register();
		TFLootModifiers.LOOT_MODIFIERS.register();
		TFMobEffects.MOB_EFFECTS.register();
		TFParticleType.PARTICLE_TYPES.register();
		TFPOITypes.POIS.register();
		TFFeatureModifiers.PLACEMENT_MODIFIERS.register();
		TFRecipes.RECIPE_SERIALIZERS.register();
		TFRecipes.RECIPE_TYPES.register();
		TFEntities.SPAWN_EGGS.register();
		TFStats.STATS.register();
		TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register();
		TFStructureProcessors.STRUCTURE_PROCESSORS.register();
		TFStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES.register();
		TFStructureTypes.STRUCTURE_TYPES.register();
		TFCreativeTabs.TABS.register();
		TFFeatureModifiers.TREE_DECORATORS.register();
		TFFeatureModifiers.TRUNK_PLACERS.register();

		DwarfRabbitVariant.DWARF_RABBITS.register();
		TinyBirdVariant.TINY_BIRDS.register();
		WoodPalettes.WOOD_PALETTES.register();
		Enforcement.ENFORCEMENTS.register();
		Restrictions.RESTRICTIONS.register();
		MagicPaintingVariants.MAGIC_PAINTINGS.register(modbus);

		modbus.addListener(this::sendIMCs);
		modbus.addListener(this::init);
		modbus.addListener(this::registerExtraStuff);
		modbus.addListener(this::setRegistriesForDatapack);
		modbus.addListener(CapabilityList::registerCapabilities);

		if (ModList.get().isLoaded("curios")) {
			Bindings.getForgeBus().get().addListener(CuriosCompat::keepCurios);
			modbus.addListener(CuriosCompat::registerCurioRenderers);
			modbus.addListener(CuriosCompat::registerCurioLayers);
		}

		BiomeGrassColors.init();

		addClassicPack();
		initEvents();
		init();

		TFCreativeTabs.registerTFBlocksTab();
	}

	public void initEvents() {
		TFTickHandler.init();
		CapabilityEvents.init();
		EntityEvents.init();
		ProgressionEvents.init();
		MiscEvents.init();
		HostileMountEvents.init();
		CharmEvents.init();
		ToolEvents.init();

		ModConfigEvents.reloading(ID).register(TFConfig::onConfigReload);
		TwilightForestMod.setRegistriesForDatapack();
		event.dataPackRegistry(BiomeLayerStack.BIOME_STACK_KEY, BiomeLayerStack.DISPATCH_CODEC);
		event.dataPackRegistry(Restrictions.RESTRICTION_KEY, Restriction.CODEC, Restriction.CODEC);
		event.dataPackRegistry(MagicPaintingVariants.REGISTRY_KEY, MagicPaintingVariant.CODEC, MagicPaintingVariant.CODEC);
	}

	public void registerExtraStuff(RegisterEvent evt) {
		if (evt.getRegistryKey().equals(Registries.BIOME_SOURCE)) {
			Registry.register(BuiltInRegistries.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
			Registry.register(BuiltInRegistries.BIOME_SOURCE, TwilightForestMod.prefix("landmarks"), LandmarkBiomeSource.CODEC);
		} else if (evt.getRegistryKey().equals(Registries.CHUNK_GENERATOR)) {
			Registry.register(BuiltInRegistries.CHUNK_GENERATOR, TwilightForestMod.prefix("structure_locating_wrapper"), ChunkGeneratorTwilight.CODEC);
		} else if (evt.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(UncraftingTableCondition.Serializer.INSTANCE);
		}
	}

	public static void init() {
		TFPacketHandler.init();
		TFAdvancements.init();

//		evt.enqueueWork(() -> {
			TFSounds.registerParrotSounds();
			TFDispenserBehaviors.init();
			TFStats.init();

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

			FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;

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

			FireBlock fireblock = (FireBlock) Blocks.FIRE;
			fireblock.setFlammable(TFBlocks.ROOT_BLOCK.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.ARCTIC_FUR_BLOCK.get(), 20, 20);
			fireblock.setFlammable(TFBlocks.LIVEROOT_BLOCK.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.EMPTY_CANOPY_BOOKSHELF.get(), 30, 20);
			fireblock.setFlammable(TFBlocks.DEATH_TOME_SPAWNER.get(), 30, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.CANOPY_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_BOOKSHELF.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.MANGROVE_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_ROOT.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.DARK_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TIME_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.MINING_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.SORTING_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_GATE.get(), 5, 20);

			ComposterBlock.add(0.1F, TFBlocks.FALLEN_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.CANOPY_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.CLOVER_PATCH.get());
			ComposterBlock.add(0.3F, TFBlocks.DARK_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.FIDDLEHEAD.get());
			ComposterBlock.add(0.3F, TFBlocks.HEDGE.get());
			ComposterBlock.add(0.3F, TFBlocks.MANGROVE_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.MAYAPPLE.get());
			ComposterBlock.add(0.3F, TFBlocks.MINING_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.TWILIGHT_OAK_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.RAINBOW_OAK_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.ROOT_STRAND.get());
			ComposterBlock.add(0.3F, TFBlocks.SORTING_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.THORN_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.TIME_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.TRANSFORMATION_LEAVES.get());
			ComposterBlock.add(0.3F, TFBlocks.TWILIGHT_OAK_SAPLING.get());
			ComposterBlock.add(0.3F, TFBlocks.CANOPY_SAPLING.get());
			ComposterBlock.add(0.3F, TFBlocks.MANGROVE_SAPLING.get());
			ComposterBlock.add(0.3F, TFBlocks.DARKWOOD_SAPLING.get());
			ComposterBlock.add(0.3F, TFBlocks.RAINBOW_OAK_SAPLING.get());
			ComposterBlock.add(0.5F, TFBlocks.BEANSTALK_LEAVES.get());
			ComposterBlock.add(0.5F, TFBlocks.MOSS_PATCH.get());
			ComposterBlock.add(0.5F, TFBlocks.ROOT_BLOCK.get());
			ComposterBlock.add(0.5F, TFBlocks.THORN_ROSE.get());
			ComposterBlock.add(0.5F, TFBlocks.TROLLVIDR.get());
			ComposterBlock.add(0.5F, TFBlocks.HOLLOW_OAK_SAPLING.get());
			ComposterBlock.add(0.5F, TFBlocks.TIME_SAPLING.get());
			ComposterBlock.add(0.5F, TFBlocks.TRANSFORMATION_SAPLING.get());
			ComposterBlock.add(0.5F, TFBlocks.MINING_SAPLING.get());
			ComposterBlock.add(0.5F, TFBlocks.SORTING_SAPLING.get());
			ComposterBlock.add(0.5F, TFBlocks.TORCHBERRY_PLANT.get());
			ComposterBlock.add(0.65F, TFBlocks.HUGE_MUSHGLOOM_STEM.get());
			ComposterBlock.add(0.65F, TFBlocks.HUGE_WATER_LILY.get());
			ComposterBlock.add(0.65F, TFBlocks.LIVEROOT_BLOCK.get());
			ComposterBlock.add(0.65F, TFBlocks.MUSHGLOOM.get());
			ComposterBlock.add(0.65F, TFBlocks.UBEROUS_SOIL.get());
			ComposterBlock.add(0.65F, TFBlocks.HUGE_STALK.get());
			ComposterBlock.add(0.65F, TFBlocks.UNRIPE_TROLLBER.get());
			ComposterBlock.add(0.65F, TFBlocks.TROLLBER.get());
			ComposterBlock.add(0.85F, TFBlocks.HUGE_LILY_PAD.get());
			ComposterBlock.add(0.85F, TFBlocks.HUGE_MUSHGLOOM.get());

			//eh, we'll do items here too
			ComposterBlock.add(0.3F, TFItems.TORCHBERRIES.get());
			ComposterBlock.add(0.5F, TFItems.LIVEROOT.get());
			ComposterBlock.add(0.65F, TFItems.MAZE_WAFER.get());
			ComposterBlock.add(0.85F, TFItems.EXPERIMENT_115.get());
			ComposterBlock.add(0.85F, TFItems.MAGIC_BEANS.get());

			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.COBBLESTONE, TFBlocks.GIANT_COBBLESTONE.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LOG, TFBlocks.GIANT_LOG.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LEAVES, TFBlocks.GIANT_LEAVES.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OBSIDIAN, TFBlocks.GIANT_OBSIDIAN.get().asItem());
//		});
	}

	public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
		TFCommand.register(dispatcher);
	}

	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(ID, name.toLowerCase(Locale.ROOT));
	}

	public static ResourceLocation namedRegistry(String name) {
		return new ResourceLocation(REGISTRY_NAMESPACE, name.toLowerCase(Locale.ROOT));
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
