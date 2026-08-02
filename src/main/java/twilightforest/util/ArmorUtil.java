package twilightforest.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFDataComponents;

public class ArmorUtil {
	public static final ArmorUtil INSTANCE = new ArmorUtil();

	public float getShroudedArmorPercentage(LivingEntity entity) {
		int shroudedArmor = 0;
		int nonShroudedArmor = 0;

		for (ItemStack stack : entity.getArmorSlots()) {
			if (!stack.isEmpty() && stack.get(TFDataComponents.EMPERORS_CLOTH.get()) != null) {
				shroudedArmor++;
			}

			nonShroudedArmor++;
		}

		return nonShroudedArmor > 0 && shroudedArmor > 0 ? (float) shroudedArmor / (float) nonShroudedArmor : 0.0F;
	}

}
