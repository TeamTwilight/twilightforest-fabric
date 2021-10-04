package twilightforest.extensions;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

//TODO: PORT
public interface IItemEx {
    default void onUsingTick(ItemStack stack, LivingEntity living, int count) {};

    default boolean showDurabilityBar(ItemStack stack) {
        return stack.isDamaged();
    }

    default int getRGBDurabilityForDisplay(ItemStack stack) {
        return Mth.hsvToRgb(Math.max(0.0F, (float) (1.0F - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
    }
    default double getDurabilityForDisplay(ItemStack stack) {
        return (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    }
    default boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return this instanceof AxeItem;
    }

    default float getXpRepairRatio(ItemStack stack) {
        return 1.0f;
    }

    default boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }
}
