package twilightforest.events;

import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.TFMain;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.entity.boss.*;
import twilightforest.entity.monster.*;
import twilightforest.entity.passive.*;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.network.*;
import twilightforest.world.components.speleothem.StalactiteReloadListener;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;

@Component
public class RegistrationEvents {


	@Autowired
	private StructureTemplateDefinitions structureTemplateDefinitions;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::init);
		bus.addListener(this::createDataMaps);
		bus.addListener(this::addBlockEntityTypes);
		bus.addListener(this::registerGenericItemHandlers);
		bus.addListener(TFCreativeTabs::addToTabs);

		NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(TFMain.prefix("quest"), new QuestReloadListener()));
		NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(TFMain.prefix("travellers_cache"), TravellersModifiersManager.CacheInvalidationReloadListener.INSTANCE));
		NeoForge.EVENT_BUS.addListener(StalactiteReloadListener.INSTANCE::registerListener);
		NeoForge.EVENT_BUS.addListener(this.structureTemplateDefinitions::registerListener);
	}

	private void registerGenericItemHandlers(RegisterCapabilitiesEvent event) {
		IBlockCapabilityProvider<ResourceHandler<ItemResource>, @Nullable Direction> itemHandlerProvider = (level, pos, state, blockEntity, side) -> level.getBlockEntity(pos) instanceof ChestBlockEntity tfChestBlock ? VanillaContainerWrapper.of(tfChestBlock) : null;
		event.registerBlock(
			Capabilities.Item.BLOCK,
			itemHandlerProvider,
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(),
			TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.CANOPY_TRAPPED_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
			TFBlocks.DARK_CHEST.get(),
			TFBlocks.DARK_TRAPPED_CHEST.get(),
			TFBlocks.TIME_CHEST.get(),
			TFBlocks.TIME_TRAPPED_CHEST.get(),
			TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(),
			TFBlocks.SORTING_CHEST.get(),
			TFBlocks.SORTING_TRAPPED_CHEST.get()
		);

		event.registerBlockEntity(Capabilities.Item.BLOCK, TFBlockEntities.MASON_JAR.get(), (masonJarBlock, side) ->
			side == Direction.UP ? masonJarBlock.getItemHandler() : null);

		event.registerBlockEntity(Capabilities.Item.BLOCK, TFBlockEntities.DRYING_RACK.get(), (entity, side) -> new DryingRackBlockEntity.DryingRackHandler(entity));
		event.registerBlockEntity(Capabilities.Item.BLOCK, TFBlockEntities.CHISELED_CANOPY_BOOKSHELF.get(), (entity, side) -> entity.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER) ? null : VanillaContainerWrapper.of(entity));
	}

	public void addBlockEntityTypes(BlockEntityTypeAddBlocksEvent event) {
		event.modify(BlockEntityType.HANGING_SIGN,
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(),
			TFBlocks.CANOPY_HANGING_SIGN.get(), TFBlocks.CANOPY_WALL_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(),
			TFBlocks.DARK_HANGING_SIGN.get(), TFBlocks.DARK_WALL_HANGING_SIGN.get(),
			TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TIME_WALL_HANGING_SIGN.get(),
			TFBlocks.TRANSFORMATION_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(),
			TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.MINING_WALL_HANGING_SIGN.get(),
			TFBlocks.SORTING_HANGING_SIGN.get(), TFBlocks.SORTING_WALL_HANGING_SIGN.get());

		event.modify(BlockEntityType.SIGN,
			TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_WALL_SIGN.get(),
			TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(),
			TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_WALL_SIGN.get(),
			TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(),
			TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_WALL_SIGN.get(),
			TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(),
			TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_WALL_SIGN.get(),
			TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get());
	}

	public void createDataMaps(RegisterDataMapTypesEvent event) {
		event.register(TFDataMaps.CRUMBLE_HORN);
		event.register(TFDataMaps.TRANSFORMATION_POWDER);
		event.register(TFDataMaps.OMINOUS_FIRE);
		event.register(TFDataMaps.MAGIC_MAP_BIOME_COLOR);
		event.register(TFDataMaps.ORE_MAP_ORE_COLOR);
	}

	public void init(FMLCommonSetupEvent evt) {
		evt.enqueueWork(() -> {
			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_HELMET.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_CHESTPLATE.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_BOOTS.get(), CauldronInteraction.DYED_ITEM);

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

			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.COBBLESTONE, TFBlocks.GIANT_COBBLESTONE.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LOG, TFBlocks.GIANT_LOG.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LEAVES, TFBlocks.GIANT_LEAVES.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OBSIDIAN, TFBlocks.GIANT_OBSIDIAN.get().asItem());
		});
	}
}