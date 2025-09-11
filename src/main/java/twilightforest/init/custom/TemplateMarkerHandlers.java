package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.markerhandler.BlockPlaceMarkerHandler;
import twilightforest.world.components.structures.markerhandler.SwitchMarkerHandler;
import twilightforest.world.components.structures.markerhandler.TemplateMarkerHandler;
import twilightforest.world.components.structures.markerhandler.TemplateMarkerHandlerType;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Map;

public class TemplateMarkerHandlers {

	public static final DeferredRegister<TemplateMarkerHandlerType> TEMPLATE_MARKER_HANDLER_TYPES = DeferredRegister.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_TYPE, TwilightForestMod.ID);
	public static final Codec<TemplateMarkerHandlerType> TYPE_CODEC = Codec.lazyInitialized(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES::byNameCodec);
	public static final Codec<TemplateMarkerHandler> DISPATCH_CODEC = TYPE_CODEC.dispatch("type", TemplateMarkerHandler::getType, TemplateMarkerHandlerType::getCodec);
	public static final Codec<Holder<TemplateMarkerHandler>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, DISPATCH_CODEC);

	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> BLOCK_PLACEMENT = TEMPLATE_MARKER_HANDLER_TYPES.register("block_placement", () -> () -> BlockPlaceMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> HANDLER_SWITCH = TEMPLATE_MARKER_HANDLER_TYPES.register("handler_switch", () -> () -> SwitchMarkerHandler.CODEC);

	public static final ResourceKey<TemplateMarkerHandlerList> CAMP_MARKER_HANDLERS = ResourceKey.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TwilightForestMod.prefix("camp_marker_handlers"));

	// TODO
	//  Rotation handler
	//  Drying Rack handler
	//  -
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
		Map<String, Holder<TemplateMarkerHandler>> keyedHandlers = Map.of(
			"twilight_oak_slab", Holder.direct(new BlockPlaceMarkerHandler(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
				.add(TFBlocks.TWILIGHT_OAK_SLAB.value().defaultBlockState())
				.add(Blocks.AIR.defaultBlockState(), 3)
				.build())))
		);
		context.register(CAMP_MARKER_HANDLERS, TemplateMarkerHandlerList.of(
			new SwitchMarkerHandler(keyedHandlers)
		));
	}

}
