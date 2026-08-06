package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.ArmorHooks;

/**
 * Modifies the elytra check in CapeLayer so that Emperor's Cloth on the elytra
 * allows the cape to render (since the elytra itself is hidden by the cloth).
 * Matches the Neoforge FixCapeUnrenderingTransformer behavior.
 */
@Mixin(CapeLayer.class)
public class CapeLayerMixin {

	@WrapOperation(
		method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
		)
	)
	private boolean twilightforest$fixCapeRendering(
		ItemStack stack,
		Item item,
		Operation<Boolean> original
	) {
		return ArmorHooks.fixCapeRendering(
			original.call(stack, item),
			stack
		);
	}
}