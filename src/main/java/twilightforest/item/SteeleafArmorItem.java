package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.ArmorTextureItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.TwilightForestMod;

public class SteeleafArmorItem extends ArmorItem implements ArmorTextureItem {

	public SteeleafArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_1.png";
		}
	}

//	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
//		if (this.allowedIn(tab)) {
//			ItemStack stack = new ItemStack(this);
//			switch (this.getSlot()) {
//				case HEAD -> stack.enchant(Enchantments.PROJECTILE_PROTECTION, 2);
//				case CHEST -> stack.enchant(Enchantments.BLAST_PROTECTION, 2);
//				case LEGS -> stack.enchant(Enchantments.FIRE_PROTECTION, 2);
//				case FEET -> stack.enchant(Enchantments.FALL_PROTECTION, 2);
//				default -> {
//				}
//			}
//			items.add(stack);
//		}
//	}
}