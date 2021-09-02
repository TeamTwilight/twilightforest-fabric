package twilightforest.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.client.ColorHandler;

import net.minecraft.client.color.block.BlockColors;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

    @Inject(method = "createDefault", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void initBlockColors(CallbackInfoReturnable<BlockColors> cir, BlockColors colors) {
        ColorHandler.registerBlockColors(colors);
    }
}
