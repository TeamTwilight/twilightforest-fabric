package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.extensions.CustomArrowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import twilightforest.entity.projectile.IceArrow;
import twilightforest.util.TFToolMaterials;

public class IceBowItem extends BowItem implements CustomArrowItem {

	public IceBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		return new IceArrow(arrow, projectileStack.copyWithCount(1), weaponStack);
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairWith) {
		return TFToolMaterials.ICE.getRepairIngredient().test(repairWith);
	}
}