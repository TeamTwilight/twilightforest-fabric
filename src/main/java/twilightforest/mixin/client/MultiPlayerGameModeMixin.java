package twilightforest.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.ThornsBlock;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), cancellable = true)
    private void clientDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ThornsBlock blockThorn = this.minecraft.level.getBlockState(pos).getBlock() instanceof ThornsBlock blockThorny? blockThorny : null;
        if(blockThorn != null){
            cir.setReturnValue(removeBlock(pos, blockThorn, false));
            cir.cancel();
        }
    }

    @Unique
    private boolean removeBlock(BlockPos pos, ThornsBlock block, boolean canHarvest) {
        BlockState state = this.minecraft.level.getBlockState(pos);
        return block.removedByPlayer(state, this.minecraft.level, pos, this.minecraft.player, canHarvest, this.minecraft.level.getFluidState(pos));
    }
}
