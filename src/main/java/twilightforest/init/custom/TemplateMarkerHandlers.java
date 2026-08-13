package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import twilightforest.TFMain;
import twilightforest.TFRegistries;
import twilightforest.init.TFBlocks;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.structures.markerhandler.*;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Map;

public class TemplateMarkerHandlers {

	public static final Codec<TemplateMarkerHandlerType> TYPE_CODEC = Codec.lazyInitialized(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES::byNameCodec);
	public static final Codec<TemplateMarkerHandler> DISPATCH_CODEC = TYPE_CODEC.dispatch("type", TemplateMarkerHandler::getType, TemplateMarkerHandlerType::getCodec);
	public static final Codec<Holder<TemplateMarkerHandler>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, DISPATCH_CODEC);

	public static final TemplateMarkerHandlerType BLOCK_PLACEMENT = register("block_placement", () -> BlockPlaceMarkerHandler.CODEC);
	public static final TemplateMarkerHandlerType HANDLER_SWITCH = register("handler_switch", () -> SwitchMarkerHandler.CODEC);
	public static final TemplateMarkerHandlerType ROTATION = register("rotation", () -> RotationMarkerHandler.CODEC);
	public static final TemplateMarkerHandlerType DRYING_RACK = register("drying_rack", () -> DryingRackMarkerHandler.CODEC);
	public static final TemplateMarkerHandlerType PAINTING = register("painting", () -> PaintingMarkerHandler.CODEC);
	public static final TemplateMarkerHandlerType LOOT = register("loot", () -> LootMarkerHandler.CODEC);

	public static final ResourceKey<TemplateMarkerHandlerList> CAMP_MARKER_HANDLERS = ResourceKey.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TFMain.prefix("camp_marker_handlers"));

	private static TemplateMarkerHandlerType register(String name, TemplateMarkerHandlerType type) {
		return Registry.register(
			TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES,
			TFMain.prefix(name),
			type
		);
	}

	// TODO
	//  Lich Tower:
	//    Dangling handler
	//    List handler
	//    Mason jar handler
	//    Skull handler
	//    Candle handler
	//    Candled Skull handler
	//    Bookshelf handler
	//    Bookshelf Mimic handler
	//    Lectern handler
	//    Spawner handler

	public static void bootstrap(BootstrapContext<TemplateMarkerHandlerList> context) {
		BlockPlaceMarkerHandler campfireSeat = new BlockPlaceMarkerHandler(new WeightedStateProvider(WeightedList.<BlockState>builder()
			.add(TFBlocks.TWILIGHT_OAK_SLAB.defaultBlockState(), 1)
			.add(Blocks.AIR.defaultBlockState(), 3)
			.build()));

		DryingRackMarkerHandler armorRack = new DryingRackMarkerHandler(SimpleStateProvider.simple(TFBlocks.CANOPY_DRYING_RACK), TFLootTables.CAMP_ARMOR_RACK);

		DryingRackMarkerHandler birchDryingRack = new DryingRackMarkerHandler(SimpleStateProvider.simple(TFBlocks.BIRCH_DRYING_RACK), TFLootTables.CAMP_DRYING_RACK);

		PaintingMarkerHandler painting = new PaintingMarkerHandler(PaintingVariantTags.PLACEABLE);

		LootMarkerHandler tentPot = new LootMarkerHandler(BlockStateProvider.simple(Blocks.DECORATED_POT), TFLootTables.CAMP_POT);

		Map<String, Holder<TemplateMarkerHandler>> keyedHandlers = Map.of(
			"twilight_oak_slab", Holder.direct(campfireSeat),
			"camp_armor_rack", Holder.direct(armorRack),
			"birch_drying_rack", Holder.direct(birchDryingRack),
			"painting", Holder.direct(painting),
			"tent_pot",  Holder.direct(tentPot)
		);
		context.register(CAMP_MARKER_HANDLERS, TemplateMarkerHandlerList.of(
			new RotationMarkerHandler(Holder.direct(new SwitchMarkerHandler(keyedHandlers)))
		));
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing template marker handler types...");
	}
}