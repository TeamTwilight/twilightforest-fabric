package twilightforest.mixin;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import twilightforest.inventory.UncraftingMenu;

@Mixin(UncraftingMenu.class)
public interface UncraftingMenuAccessor {
	@Invoker
	static int callCountDamageableParts(Container matrix) {
		throw new UnsupportedOperationException();
	}
}
