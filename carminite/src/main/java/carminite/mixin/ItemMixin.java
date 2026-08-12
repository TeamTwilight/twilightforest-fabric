package carminite.mixin;

import carminite.transfer.asm.IItemResourceDuck;
import carminite.transfer.item.ItemResource;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public class ItemMixin implements IItemResourceDuck {

	@Unique
	private @Nullable ItemResource carminite$defaultResource;

	@Override
	public @Nullable ItemResource carminite$getDefaultResource() {
		return carminite$defaultResource;
	}

	@Override
	public void carminite$setDefaultResource(@Nullable ItemResource resource) {
		carminite$defaultResource = resource;
	}
}