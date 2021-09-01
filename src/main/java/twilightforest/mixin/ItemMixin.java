package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import twilightforest.extensions.IItem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(Item.class)
public class ItemMixin implements IItem {
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity living, int count) {

    }
}
