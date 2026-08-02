package twilightforest.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFItems;
import twilightforest.util.TFBoatTypes;

/**
 * Mixin to handle custom Twilight Forest boat types.
 * In 1.21.1, Boat.getDropItem() uses an exhaustive switch on Boat.Type,
 * which crashes with MatchException when a custom type is used.
 * This mixin intercepts the method to return the appropriate TF boat item.
 */
@Mixin(Boat.class)
public class BoatMixin {

	@Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
	private void twilightforest$getDropItem(CallbackInfoReturnable<Item> cir) {
		Boat self = (Boat) (Object) this;
		Boat.Type type = self.getVariant();
		boolean hasChest = self.getType() == EntityType.CHEST_BOAT;

		// Check if this is a TF custom boat type and return the appropriate item
		Item boatItem = getTFBoatItem(type, hasChest);
		if (boatItem != null) {
			cir.setReturnValue(boatItem);
		}
	}

	/**
	 * Returns the TF boat item for the given type, or null if not a TF type.
	 */
	private static Item getTFBoatItem(Boat.Type type, boolean hasChest) {
		if (type == TFBoatTypes.TWILIGHT_OAK) {
			return hasChest ? TFItems.TWILIGHT_OAK_CHEST_BOAT.get() : TFItems.TWILIGHT_OAK_BOAT.get();
		}
		if (type == TFBoatTypes.CANOPY) {
			return hasChest ? TFItems.CANOPY_CHEST_BOAT.get() : TFItems.CANOPY_BOAT.get();
		}
		if (type == TFBoatTypes.MANGROVE_TYPE) {
			return hasChest ? TFItems.MANGROVE_CHEST_BOAT.get() : TFItems.MANGROVE_BOAT.get();
		}
		if (type == TFBoatTypes.DARK) {
			return hasChest ? TFItems.DARK_CHEST_BOAT.get() : TFItems.DARK_BOAT.get();
		}
		if (type == TFBoatTypes.TIME) {
			return hasChest ? TFItems.TIME_CHEST_BOAT.get() : TFItems.TIME_BOAT.get();
		}
		if (type == TFBoatTypes.TRANSFORMATION) {
			return hasChest ? TFItems.TRANSFORMATION_CHEST_BOAT.get() : TFItems.TRANSFORMATION_BOAT.get();
		}
		if (type == TFBoatTypes.MINING) {
			return hasChest ? TFItems.MINING_CHEST_BOAT.get() : TFItems.MINING_BOAT.get();
		}
		if (type == TFBoatTypes.SORTING) {
			return hasChest ? TFItems.SORTING_CHEST_BOAT.get() : TFItems.SORTING_BOAT.get();
		}
		return null; // Not a TF type, let vanilla handle it
	}
}