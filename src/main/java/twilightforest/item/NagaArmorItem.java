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

public class NagaArmorItem extends ArmorItem implements ArmorTextureItem {
	public NagaArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_1.png";
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (this.allowedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			switch (this.getSlot()) {
				case CHEST -> stack.enchant(Enchantments.FIRE_PROTECTION, 3);
				case LEGS -> stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 3);
				default -> {
				}
			}
			items.add(stack);
		}
	}
}