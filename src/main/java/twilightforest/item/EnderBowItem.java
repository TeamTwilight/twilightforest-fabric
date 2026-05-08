package twilightforest.item;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EnderBowItem extends CodexBowItem {
    public static final String KEY = "twilightforest:ender";

    public EnderBowItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        arrow.addTag(KEY);
        return arrow;
    }
}
