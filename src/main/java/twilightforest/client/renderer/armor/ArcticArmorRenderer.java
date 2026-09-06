package twilightforest.client.renderer.armor;

import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.item.ArcticArmorItem;

public final class ArcticArmorRenderer extends TFSimpleArmorRenderer {
	public ArcticArmorRenderer() {
		super(TFArmorModel::new, TFModelLayers.ARCTIC_ARMOR_INNER, TFModelLayers.ARCTIC_ARMOR_OUTER);
	}

	@Override
	public int getDefaultDyeColor(ItemStack stack) {
		return ARGB.opaque(DyedItemColor.getOrDefault(stack, ArcticArmorItem.DEFAULT_COLOR));
	}
}