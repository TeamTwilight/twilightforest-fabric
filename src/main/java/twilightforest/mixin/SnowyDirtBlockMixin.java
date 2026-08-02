package twilightforest.mixin;

import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;

@Mixin(SnowyDirtBlock.class)
public class SnowyDirtBlockMixin {

	@Inject(method = "isSnowySetting", at = @At("RETURN"), cancellable = true)
	private static void twilightforest$keepSnowyStateForSnowloggableBlocks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(BlockHooks.keepSnowyStateForSnowloggableBlocks(cir.getReturnValue(), state));
	}
}