package twilightforest.extensions;

import twilightforest.api.client.IItemRenderProperties;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IItem {
    default void onUsingTick(ItemStack stack, LivingEntity living, int count) {};
    default void initializeClient(java.util.function.Consumer<IItemRenderProperties> consumer) {}

    default Object getRenderPropertiesInternal() {
        return null;
    }
}
