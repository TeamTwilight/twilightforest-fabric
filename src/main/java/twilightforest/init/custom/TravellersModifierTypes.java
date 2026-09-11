package twilightforest.init.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import twilightforest.TFCommon;
import twilightforest.init.TFRegistries;
import twilightforest.item.travellers_gear.modifiers.*;

public class TravellersModifierTypes {

	public static final MapCodec<TravellersEntryModifier> ATTRIBUTE_ENTRY = register("attribute", TravellersEntryModifier.CODEC);
	public static final MapCodec<BuiltinTravellersComponentModifier> BUILTIN = register("builtin", BuiltinTravellersComponentModifier.CODEC);
	public static final MapCodec<TravellersComponentModifier> COMPONENT = register("component", TravellersComponentModifier.CODEC);
	public static final MapCodec<TransferableComponentModifier> TRANSFERABLE_COMPONENT = register("transferable_component", TransferableComponentModifier.CODEC);

	private static <T extends TravellersModifier> MapCodec<T> register(
		String name,
		MapCodec<T> codec
	) {
		return Registry.register(
			TFRegistries.TRAVELLERS_MODIFIER_TYPE,
			TFCommon.prefix(name),
			codec
		);
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing travellers modifier types...");
	}
}