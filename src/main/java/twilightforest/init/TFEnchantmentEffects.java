package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import twilightforest.TwilightForestMod;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.enchantment.SmashBlocksEffect;

/**
 * Fabric port — registers TF custom {@link EnchantmentEntityEffect} types into
 * {@link BuiltInRegistries#ENCHANTMENT_ENTITY_EFFECT_TYPE} so enchantment JSONs
 * (chill_aura/destruction/fire_react) parse without «Unknown registry key».
 */
public final class TFEnchantmentEffects {

    public static final TFRegistryObject<MapCodec<? extends EnchantmentEntityEffect>> APPLY_FROSTED = register("apply_frosted", ApplyFrostedEffect.CODEC);
    public static final TFRegistryObject<MapCodec<? extends EnchantmentEntityEffect>> SMASH_BLOCKS = register("smash_blocks", SmashBlocksEffect.CODEC);
    public static final TFRegistryObject<MapCodec<? extends EnchantmentEntityEffect>> RECHARGE_SCEPTER = register("recharge_scepter", RechargeScepterEffect.CODEC);

    private TFEnchantmentEffects() {
    }

    public static void bootstrap() {
        APPLY_FROSTED.get();
        SMASH_BLOCKS.get();
        RECHARGE_SCEPTER.get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static TFRegistryObject<MapCodec<? extends EnchantmentEntityEffect>> register(String path, MapCodec<? extends EnchantmentEntityEffect> codec) {
        MapCodec<? extends EnchantmentEntityEffect> registered = Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, TwilightForestMod.prefix(path), codec);
        return (TFRegistryObject) new TFRegistryObject(registered);
    }
}
