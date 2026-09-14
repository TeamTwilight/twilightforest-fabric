package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.ArmorHooks;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

	@ModifyExpressionValue(
		method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/equipment/Equippable;Lnet/minecraft/world/entity/EquipmentSlot;)Z"
		)
	)
	private boolean twilightforest$cancelArmorRendering(
		boolean original,
		@Local(argsOnly = true, name = "itemStack") ItemStack itemStack
	) {
		return ArmorHooks.cancelArmorRendering(original, itemStack);
	}
}