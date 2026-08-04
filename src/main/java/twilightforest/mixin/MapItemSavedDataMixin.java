package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.MapHooks;

@Mixin(MapItemSavedData.class)
public class MapItemSavedDataMixin {

	@ModifyExpressionValue(
		method = "tickCarriedBy(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Inventory;contains(Ljava/util/function/Predicate;)Z"
		)
	)
	private boolean twilightforest$updateMapsInGoggles(
		boolean original,
		Player player,
		ItemStack mapStack
	) {
		return MapHooks.updateMapsInGoggles(
			original,
			mapStack,
			player
		);
	}
}