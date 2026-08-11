package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import twilightforest.TFMain;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.enchantment.SmashBlocksEffect;

public class TFEnchantmentEffects {

	public static final MapCodec<ApplyFrostedEffect> APPLY_FROSTED = register("apply_frosted", ApplyFrostedEffect.CODEC);
	public static final MapCodec<RechargeScepterEffect> RECHARGE_SCEPTER = register("recharge_scepter", RechargeScepterEffect.CODEC);
	public static final MapCodec<SmashBlocksEffect> SMASH_BLOCKS = register("smash_blocks", SmashBlocksEffect.CODEC);

	private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String name, MapCodec<T> codec) {
		return Registry.register(
			BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
			TFMain.prefix(name),
			codec
		);
	}
}