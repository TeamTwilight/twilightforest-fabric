package twilightforest.item;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import twilightforest.entity.projectile.IceArrow;

public class IceBowItem extends CodexBowItem {

    public IceBowItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        return new IceArrow(arrow, projectileStack.copyWithCount(1), weaponStack);
    }
}
