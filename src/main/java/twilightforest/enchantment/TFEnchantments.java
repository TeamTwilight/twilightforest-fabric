package twilightforest.enchantment;

import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;
import twilightforest.TwilightForestMod;

public class TFEnchantments {

	//public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TwilightForestMod.ID);

	public static final Enchantment FIRE_REACT = Registry.register(Registry.ENCHANTMENT, TwilightForestMod.prefix("fire_react"), new FireReactEnchantment(Enchantment.Rarity.UNCOMMON));
	public static final Enchantment CHILL_AURA = Registry.register(Registry.ENCHANTMENT, TwilightForestMod.prefix("chill_aura"), new ChillAuraEnchantment(Enchantment.Rarity.UNCOMMON));
}
