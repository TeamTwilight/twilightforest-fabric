package twilightforest.mixin;

import io.github.fabricators_of_create.porting_lib.core.util.ServerLifecycleHooks;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.List;
import java.util.stream.Stream;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {

	@Shadow
	@Final
	private Container resultSlots;

	@Shadow
	@Final
	Container repairSlots;

	@Inject(
		method = "createResult()V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$onChanged(CallbackInfo ci) {
		ItemStack top = this.repairSlots.getItem(0);
		ItemStack bottom = this.repairSlots.getItem(1);

		if (ServerLifecycleHooks.getCurrentServer() == null)
			return;

		RegistryAccess access = ServerLifecycleHooks.getCurrentServer().registryAccess();
		List<ItemStack> travellersItemStacks = Stream.of(top, bottom)
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
			.toList();

		if (travellersItemStacks.isEmpty())
			return; // Delegate to vanilla logic
		if (travellersItemStacks.size() > 1) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			ci.cancel();
			return;
		}
		ItemStack inputStack = travellersItemStacks.getFirst();
		List<Holder.Reference<TravellersModifier>> modifiers = TravellersModifiersManager.findAllInsertableModifiers(access, inputStack);
		if (modifiers.isEmpty()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			ci.cancel();
			return;
		}

		ItemStack unmodifiedStack = inputStack.copy();
		modifiers.forEach(modifier -> ((InsertableTravellersModifier) modifier.value()).removeModifier(unmodifiedStack));

		this.resultSlots.setItem(0, unmodifiedStack.copy());
		ci.cancel();
	}
}