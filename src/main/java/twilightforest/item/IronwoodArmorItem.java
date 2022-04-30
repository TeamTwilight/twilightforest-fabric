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

public class IronwoodArmorItem extends ArmorItem implements ArmorTextureItem {

	public IronwoodArmorItem(ArmorMaterial armorMaterial, EquipmentSlot armorType, Properties props) {
		super(armorMaterial, armorType, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "ironwood_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "ironwood_1.png";
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.getSlot()) {
				case HEAD -> istack.enchant(Enchantments.AQUA_AFFINITY, 1);
				case CHEST, LEGS -> istack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 1);
				case FEET -> istack.enchant(Enchantments.FALL_PROTECTION, 1);
				default -> { }
			}
			list.add(istack);
		}
	}
}
