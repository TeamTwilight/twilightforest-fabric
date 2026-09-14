package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.ArmorHooks;

@Mixin(WingsLayer.class)
public class WingsLayerMixin {

	@ModifyExpressionValue(
		method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Optional;isEmpty()Z"
		)
	)
	private boolean twilightforest$cancelWingsRendering(
		boolean original,
		@Local(name = "itemStack") ItemStack itemStack
	) {
		return ArmorHooks.cancelWingsRendering(original, itemStack);
	}
}