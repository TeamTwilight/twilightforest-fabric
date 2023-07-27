package twilightforest.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.curios.CuriosCompat;

public interface CurioItem {

	@Nullable
	default ICapabilityProvider setupCurio(ItemStack stack, @Nullable ICapabilityProvider provider) {
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.setupCuriosCapability(stack);
		}
		return provider;
	}
}
