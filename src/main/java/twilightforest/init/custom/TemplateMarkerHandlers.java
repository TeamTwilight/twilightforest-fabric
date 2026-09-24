package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import twilightforest.TFCommon;
import twilightforest.init.TFRegistries;
import twilightforest.world.components.structures.markerhandler.*;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

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

	public static final ResourceKey<TemplateMarkerHandlerList> CAMP_MARKER_HANDLERS = ResourceKey.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TFCommon.prefix("camp_marker_handlers"));

	private static TemplateMarkerHandlerType register(String name, TemplateMarkerHandlerType type) {
		return Registry.register(
			TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES,
			TFCommon.prefix(name),
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

	public static void init() {
		TFCommon.LOGGER.info("Initializing template marker handler types...");
	}
}