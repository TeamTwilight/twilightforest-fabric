package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.ArmorHooks;

@Mixin(CapeLayer.class)
public class CapeLayerMixin {

	@ModifyReturnValue(
		method = "hasLayer(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;)Z",
		at = @At(value = "RETURN")
	)
	private boolean twilightforest$fixCapeRendering(
		boolean original,
		@Local(argsOnly = true, name = "itemStack") ItemStack itemStack
	) {
		return ArmorHooks.fixCapeRendering(original, itemStack);
	}
}