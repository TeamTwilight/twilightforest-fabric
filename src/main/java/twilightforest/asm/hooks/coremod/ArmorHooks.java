package twilightforest.asm.hooks.coremod;

import net.minecraft.world.entity.LivingEntity;
import twilightforest.util.ArmorUtil;

public final class ArmorHooks {
	private static final ArmorUtil armorUtil = ArmorUtil.INSTANCE;

	public static float modifyArmorVisibility(float o, LivingEntity entity) {
		return o - armorUtil.getShroudedArmorPercentage(entity);
	}
}