package twilightforest.init;

import com.chocohead.mm.api.ClassTinkerers;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import twilightforest.TwilightForestMod;
import twilightforest.enchantment.ChillAuraEnchantment;
import twilightforest.enchantment.DestructionEnchantment;
import twilightforest.enchantment.FireReactEnchantment;

public class TFEnchantments {

	public static final LazyRegistrar<Enchantment> ENCHANTMENTS = LazyRegistrar.create(Registries.ENCHANTMENT, TwilightForestMod.ID);

	public static final RegistryObject<Enchantment> FIRE_REACT = ENCHANTMENTS.register("fire_react", () -> new FireReactEnchantment(Enchantment.Rarity.UNCOMMON));
	public static final RegistryObject<Enchantment> CHILL_AURA = ENCHANTMENTS.register("chill_aura", () -> new ChillAuraEnchantment(Enchantment.Rarity.UNCOMMON));
	public static final RegistryObject<Enchantment> DESTRUCTION = ENCHANTMENTS.register("destruction", () -> new DestructionEnchantment(Enchantment.Rarity.RARE));

	public static final EnchantmentCategory BLOCK_AND_CHAIN = ClassTinkerers.getEnum(EnchantmentCategory.class, "TWILIGHTFOREST_BLOCK_AND_CHAIN");
}
