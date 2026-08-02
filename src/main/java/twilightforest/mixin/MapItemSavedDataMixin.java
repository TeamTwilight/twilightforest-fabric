package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.MapHooks;

import java.util.function.Predicate;

@Mixin(MapItemSavedData.class)
public class MapItemSavedDataMixin {

	@WrapOperation(
		method = "tickCarriedBy",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Inventory;contains(Ljava/util/function/Predicate;)Z"
		)
	)
	private boolean twilightforest$updateMapsInGoggles(net.minecraft.world.entity.player.Inventory inventory, Predicate<ItemStack> predicate, Operation<Boolean> original, Player player, ItemStack stack) {
		boolean originalResult = original.call(inventory, predicate);
		return MapHooks.updateMapsInGoggles(originalResult, stack, player);
	}
}