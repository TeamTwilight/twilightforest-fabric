package twilightforest.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.ItemHooks;

@Mixin(WrittenBookItem.class)
public abstract class WrittenBookItemMixin {

	@Inject(method = "getName", at = @At("RETURN"), cancellable = true)
	private void twilightforest$translateStructureHintBookName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
		cir.setReturnValue(ItemHooks.modifyWrittenBookName(cir.getReturnValue(), stack));
	}
}
