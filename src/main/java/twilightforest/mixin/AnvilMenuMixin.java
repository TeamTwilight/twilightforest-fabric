package twilightforest.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDataComponents;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

	@Shadow
	@Final
	private DataSlot cost;

	@Inject(
		method = "createResult()V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$cancelCombiningTravellersGear(CallbackInfo ci) {
		AnvilMenu self = (AnvilMenu) (Object) this;
		ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;

		ItemStack left = accessor.twilightforest$getInputSlots().getItem(0);
		ItemStack right = accessor.twilightforest$getInputSlots().getItem(1);

		if (left.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()) && right.has(TFDataComponents.IS_TRAVELLERS_GEAR.get())) {
			accessor.twilightforest$getResultSlots().setItem(0, ItemStack.EMPTY);
			this.cost.set(0);
			ci.cancel();
		}
	}
}