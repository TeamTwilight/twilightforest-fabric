package twilightforest.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import twilightforest.asmhooks.StackSizeItemExtension;

@Mixin(Item.class)
public class ItemMixin implements StackSizeItemExtension {
}