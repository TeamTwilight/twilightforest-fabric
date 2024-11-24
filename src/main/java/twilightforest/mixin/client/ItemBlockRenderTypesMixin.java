package twilightforest.mixin.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.GiantLeavesBlock;

@Mixin(ItemBlockRenderTypes.class)
public class ItemBlockRenderTypesMixin {
	@Shadow
	private static boolean renderCutout;

	@Inject(method = "getChunkRenderType", at = @At("HEAD"), cancellable = true)
	private static void twilightforest$fixGiantLeavesChunkRenderType(BlockState blockState, CallbackInfoReturnable<RenderType> cir) {
		if (blockState.getBlock() instanceof GiantLeavesBlock)
			cir.setReturnValue(renderCutout ? RenderType.cutoutMipped() : RenderType.solid());
	}

	@Inject(method = "getMovingBlockRenderType", at = @At("HEAD"), cancellable = true)
	private static void twilightforest$fixGiantLeavesMovingRenderType(BlockState blockState, CallbackInfoReturnable<RenderType> cir) {
		if (blockState.getBlock() instanceof GiantLeavesBlock)
			cir.setReturnValue(renderCutout ? RenderType.cutoutMipped() : RenderType.solid());
	}
}
