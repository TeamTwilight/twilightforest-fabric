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

		Item boatItem = twilightforest$getTFChestBoatItem(type);
		if (boatItem != null) {
			cir.setReturnValue(boatItem);
		}
	}

	@Unique
	private static Item twilightforest$getTFChestBoatItem(Boat.Type type) { // sic
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
