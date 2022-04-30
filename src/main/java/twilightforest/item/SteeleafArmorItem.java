package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.util.ArmorTextureItem;
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

	public SteeleafArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties props) {
		super(material, slot, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "steeleaf_1.png";
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.slot) {
				case HEAD -> istack.enchant(Enchantments.PROJECTILE_PROTECTION, 2);
				case CHEST -> istack.enchant(Enchantments.BLAST_PROTECTION, 2);
				case LEGS -> istack.enchant(Enchantments.FIRE_PROTECTION, 2);
				case FEET -> istack.enchant(Enchantments.FALL_PROTECTION, 2);
				default -> { }
			}
			list.add(istack);
		}
	}
}
