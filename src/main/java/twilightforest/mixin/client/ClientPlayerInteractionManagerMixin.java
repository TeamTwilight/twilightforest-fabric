package twilightforest.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.extensions.IBlockMethods;

@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public class ClientPlayerInteractionManagerMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
    public void breakBlockMixin(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
       if(minecraft.level.getBlockState(blockPos).getBlock() instanceof IBlockMethods myBlock) {
           if (!myBlock.canEntityDestroy(minecraft.level.getBlockState(blockPos), blockPos, minecraft.player)) {
               cir.setReturnValue(false);
               cir.cancel();
           }
       }
    }



}

