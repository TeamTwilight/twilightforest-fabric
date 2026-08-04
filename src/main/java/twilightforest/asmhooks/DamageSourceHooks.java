package twilightforest.asmhooks;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.item.CustomDamageProvider;

public class DamageSourceHooks {

	public static DamageSource getCustomDamageSource(DamageSource o, LivingEntity entity) {
		return entity.getWeaponItem().getItem() instanceof CustomDamageProvider customDamageType ? customDamageType.getDamageSource(entity) : o;
	}
}
