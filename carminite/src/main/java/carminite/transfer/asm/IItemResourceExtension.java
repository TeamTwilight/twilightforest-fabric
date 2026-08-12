package carminite.transfer.asm;

import carminite.transfer.item.ItemResource;
import net.minecraft.world.item.Item;

import java.util.function.Function;

/**
 * Injected into Item in twilightforest.classtweaker
 */
public interface IItemResourceExtension extends IItemResourceDuck {
	default ItemResource carminite$computeDefaultResource(
		Function<Item, ItemResource> resourceConstructor
	) {
		ItemResource resource = carminite$getDefaultResource();

		if (resource == null) {
			resource = resourceConstructor.apply((Item) (Object) this);
			carminite$setDefaultResource(resource);
		}

		return resource;
	}

	default void carminite$resetDefaultResource() {
		carminite$setDefaultResource(null);
	}
}