package twilightforest.enchantment;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import twilightforest.TwilightForestMod;
import twilightforest.item.ChainBlockItem;

public class TFEnchantments {

	public static final LazyRegistrar<Enchantment> ENCHANTMENTS = LazyRegistrar.create(Registry.ENCHANTMENT, TwilightForestMod.ID);

	public static final RegistryObject<Enchantment> FIRE_REACT = ENCHANTMENTS.register("fire_react", () -> new FireReactEnchantment(Enchantment.Rarity.UNCOMMON));
	public static final RegistryObject<Enchantment> CHILL_AURA = ENCHANTMENTS.register("chill_aura", () -> new ChillAuraEnchantment(Enchantment.Rarity.UNCOMMON));
	public static final RegistryObject<Enchantment> PRESERVATION = ENCHANTMENTS.register("preservation", () -> new PreservationEnchantment(Enchantment.Rarity.RARE));
	public static final RegistryObject<Enchantment> BLOCK_STRENGTH = ENCHANTMENTS.register("block_strength", () -> new BlockStrengthEnchantment(Enchantment.Rarity.RARE));
	public static final RegistryObject<Enchantment> DESTRUCTION = ENCHANTMENTS.register("destruction", () -> new DestructionEnchantment(Enchantment.Rarity.RARE));

	public static final EnchantmentCategory BLOCK_AND_CHAIN = EnchantmentCategory.create("twilightforest_block_and_chain", item -> item instanceof ChainBlockItem);
}
