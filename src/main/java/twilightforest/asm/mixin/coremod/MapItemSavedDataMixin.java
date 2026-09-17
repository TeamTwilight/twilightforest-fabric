package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.MapHooks;

@Mixin(MapItemSavedData.class)
public class MapItemSavedDataMixin {

	@ModifyExpressionValue(
		method = "tickCarriedBy(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/decoration/ItemFrame;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Inventory;contains(Ljava/util/function/Predicate;)Z"
		)
	)
	private boolean twilightforest$updateMapsInGoggles(
		boolean original,
		@Local(argsOnly = true, name = "tickingPlayer") Player tickingPlayer,
		@Local(argsOnly = true, name = "itemStack") ItemStack itemStack
	) {
		return MapHooks.updateMapsInGoggles(original, itemStack, tickingPlayer);
	}
}