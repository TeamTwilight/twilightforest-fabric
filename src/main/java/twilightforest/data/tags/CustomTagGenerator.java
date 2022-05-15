package twilightforest.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.TwilightForestMod;

//a place to hold all custom tags, since I imagine we wont have a lot of them
public class CustomTagGenerator {

	public static class EnchantmentTagGenerator extends TagsProvider<Enchantment> {

		public static final TagKey<Enchantment> PHANTOM_ARMOR_BANNED_ENCHANTS = TagKey.create(Registry.ENCHANTMENT_REGISTRY, TwilightForestMod.prefix("phantom_armor_banned_enchants"));

		public EnchantmentTagGenerator(FabricDataGenerator generatorIn) {
			super(generatorIn, Registry.ENCHANTMENT);
		}

		@Override
		protected void addTags() {
			tag(PHANTOM_ARMOR_BANNED_ENCHANTS).add(Enchantments.VANISHING_CURSE, Enchantments.BINDING_CURSE);
		}

		@Override
		public String getName() {
			return "Twilight Forest Enchantment Tags";
		}
	}
}
