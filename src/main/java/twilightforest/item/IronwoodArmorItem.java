package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import twilightforest.TwilightForestMod;

public class IronwoodArmorItem extends ArmorItem {

	public IronwoodArmorItem(ArmorMaterial armorMaterial, EquipmentSlot armorType, Properties props) {
		super(armorMaterial, armorType, props);
	}

//	@Override
//	public @NotNull ResourceLocation getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, boolean secondLayer, @Nullable String suffix, ResourceLocation defaultTexture) {
//		if (slot == EquipmentSlot.LEGS) {
//			return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "ironwood_2.png");
//		} else {
//			return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "ironwood_1.png");
//		}
//	}

	//@Override
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
				case HEAD:
					istack.enchant(Enchantments.AQUA_AFFINITY, 1);
					break;
				case CHEST:
				case LEGS:
					istack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 1);
					break;
				case FEET:
					istack.enchant(Enchantments.FALL_PROTECTION, 1);
					break;
				default:
					break;
			}
			list.add(istack);
		}
	}
}
