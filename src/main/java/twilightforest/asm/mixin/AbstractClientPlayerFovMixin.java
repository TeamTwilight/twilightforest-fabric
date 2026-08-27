package twilightforest.asm.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.item.EnderBowItem;
import twilightforest.item.IceBowItem;
import twilightforest.item.SeekerBowItem;
import twilightforest.item.TripleBowItem;

/**
 * Recreates NeoForge's ComputeFovModifierEvent handling: twilight bows zoom the
 * FOV while drawn, mirroring the vanilla behaviour in getFieldOfViewModifier.
 */
@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerFovMixin {

	@Inject(method = "getFieldOfViewModifier(ZF)F", at = @At("RETURN"), cancellable = true)
	private void twilightforest$bowFovModifier(boolean usingFOV, float partialTick, CallbackInfoReturnable<Float> cir) {
		AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
		ItemStack useItem = player.getUseItem();
		if (player.isUsingItem() && (useItem.getItem() instanceof TripleBowItem || useItem.getItem() instanceof EnderBowItem || useItem.getItem() instanceof IceBowItem || useItem.getItem() instanceof SeekerBowItem)) {
			float f = player.getTicksUsingItem() / 20.0F;
			f = f > 1.0F ? 1.0F : f * f;
			cir.setReturnValue((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, cir.getReturnValue() * (1.0F - f * 0.15F)));
		}
	}
}
