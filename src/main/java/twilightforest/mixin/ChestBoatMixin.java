package twilightforest.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFItems;
import twilightforest.util.TFBoatTypes;

/**
 * Mixin to handle custom Twilight Forest chest boat types.
 * In 1.21.1, ChestBoat.getDropItem() uses an exhaustive switch on Boat.Type,
 * which returns Items.OAK_CHEST_BOAT for unmatched types instead of the correct TF item.
 * This mixin intercepts the method to return the appropriate TF chest boat item.
 */
@Mixin(ChestBoat.class)
public class ChestBoatMixin {

	@Inject(
		method = "getDropItem",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$getDropItem(CallbackInfoReturnable<Item> cir) {
		ChestBoat self = (ChestBoat) (Object) this;
		Boat.Type type = self.getVariant(); // sic - uses inner class import via parent

		Item boatItem = getTFChestBoatItem(type);
		if (boatItem != null) {
			cir.setReturnValue(boatItem);
		}
	}

	@Unique
	private static Item getTFChestBoatItem(Boat.Type type) { // sic
		if (type == TFBoatTypes.TWILIGHT_OAK) return TFItems.TWILIGHT_OAK_CHEST_BOAT.get();
		if (type == TFBoatTypes.CANOPY) return TFItems.CANOPY_CHEST_BOAT.get();
		if (type == TFBoatTypes.MANGROVE_TYPE) return TFItems.MANGROVE_CHEST_BOAT.get();
		if (type == TFBoatTypes.DARK) return TFItems.DARK_CHEST_BOAT.get();
		if (type == TFBoatTypes.TIME) return TFItems.TIME_CHEST_BOAT.get();
		if (type == TFBoatTypes.TRANSFORMATION) return TFItems.TRANSFORMATION_CHEST_BOAT.get();
		if (type == TFBoatTypes.MINING) return TFItems.MINING_CHEST_BOAT.get();
		if (type == TFBoatTypes.SORTING) return TFItems.SORTING_CHEST_BOAT.get();
		return null;
	}
}
