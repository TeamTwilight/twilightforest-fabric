package twilightforest.mixin;

import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.ThornsBlock;
import twilightforest.extensions.IBlockMethods;
import twilightforest.extensions.IBlockStateEx;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow protected ServerLevel level;

    @Shadow @Final protected ServerPlayer player;

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"), cancellable = true)
    private void serverDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ThornsBlock blockThorn = this.level.getBlockState(pos).getBlock() instanceof ThornsBlock blockThorny? blockThorny : null;
        if(blockThorn != null){
            cir.setReturnValue(removeBlock(pos, blockThorn, false));
            cir.cancel();
        }
    }

    @Unique
    private boolean removeBlock(BlockPos pos, ThornsBlock block, boolean canHarvest) {
        BlockState state = this.level.getBlockState(pos);
        boolean removed =  block.removedByPlayer(state, this.level, pos, this.player, canHarvest, this.level.getFluidState(pos));
        if (removed)
            block.destroy(this.level, pos, state);
        return removed;
    }


}
