package carminite.transfer.asm;

import carminite.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

/**
 * Implemented by ItemMixin
 */
public interface IItemResourceDuck {

	@Nullable
	ItemResource carminite$getDefaultResource();

	void carminite$setDefaultResource(@Nullable ItemResource resource);
}