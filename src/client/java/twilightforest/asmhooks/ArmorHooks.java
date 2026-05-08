package twilightforest.asmhooks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFDataComponents;
import twilightforest.util.ArmorUtil;

public class ArmorHooks {

	private static final ArmorUtil ARMOR_UTIL = new ArmorUtil();

	public static float modifyArmorVisibility(float original, LivingEntity entity) {
		return original - ARMOR_UTIL.getShroudedArmorPercentage(entity);
	}

	public static boolean cancelArmorRendering(boolean original, ItemStack stack) {
		return original && !stack.has(TFDataComponents.EMPERORS_CLOTH);
	}

	public static boolean fixCapeRendering(boolean original, ItemStack stack) {
		return original && !stack.has(TFDataComponents.EMPERORS_CLOTH);
	}
}
