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

public class NagaArmorItem extends ArmorItem implements ArmorRenderingRegistry.TextureProvider{
	protected NagaArmorItem(ArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties props) {
		super(materialIn, equipmentSlotIn, props);
	}

	@Override
	public @NotNull ResourceLocation getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, boolean secondLayer, @Nullable String suffix, ResourceLocation defaultTexture) {
		if (slot == EquipmentSlot.LEGS) {
			return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "naga_scale_2.png");
		} else {
			return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "naga_scale_1.png");
		}
	}

	//@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_1.png";
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.slot) {
				case CHEST:
					istack.enchant(Enchantments.FIRE_PROTECTION, 3);
					break;
				case LEGS:
					istack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 3);
					break;
				default:
					break;
			}
			list.add(istack);
		}
	}
}
