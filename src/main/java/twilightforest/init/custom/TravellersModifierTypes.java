package twilightforest.init.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.item.travellers_gear.modifiers.*;

public final class TravellersModifierTypes {
    public static final MapCodec<TravellersEntryModifier> ATTRIBUTE_ENTRY = register("attribute", TravellersEntryModifier.CODEC);
    public static final MapCodec<BuiltinTravellersComponentModifier> BUILTIN = register("builtin", BuiltinTravellersComponentModifier.CODEC);
    public static final MapCodec<TravellersComponentModifier> COMPONENT = register("component", TravellersComponentModifier.CODEC);
    public static final MapCodec<TransferableComponentModifier> TRANSFERABLE_COMPONENT = register("transferable_component", TransferableComponentModifier.CODEC);

    private TravellersModifierTypes() {
    }

    public static void bootstrap() {
    }

    private static <T extends TravellersModifier> MapCodec<T> register(String path, MapCodec<T> codec) {
        return Registry.register(TFRegistries.TRAVELLERS_MODIFIER_TYPE, TwilightForestMod.prefix(path), codec);
    }
}
