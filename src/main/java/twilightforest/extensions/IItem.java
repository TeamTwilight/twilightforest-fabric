package twilightforest.extensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IItem {
    void onUsingTick(ItemStack stack, LivingEntity living, int count);
}
