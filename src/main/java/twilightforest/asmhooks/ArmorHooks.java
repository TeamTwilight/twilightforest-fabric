package twilightforest.asmhooks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFDataComponents;
import twilightforest.util.ArmorUtil;

public class ArmorHooks {

	private static final ArmorUtil armorUtil = ArmorUtil.INSTANCE;

	public static double modifyArmorVisibility(double o, LivingEntity entity) {
		return o - armorUtil.getShroudedArmorPercentage(entity);
	}

	public static boolean cancelArmorRendering(boolean o, ItemStack stack) {
		if (o && stack.has(TFDataComponents.EMPERORS_CLOTH.get())) {
			return false;
		}
		return o;
	}

	public static boolean fixCapeRendering(boolean o, ItemStack stack) {
		return o && !stack.has(TFDataComponents.EMPERORS_CLOTH.get());
	}
}
