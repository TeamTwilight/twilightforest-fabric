package twilightforest.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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

    @Redirect(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    public boolean destroyBlockHook(ServerLevel level, BlockPos pos, boolean isMoving) {
        return removeBlock(pos, isMoving);
    }

    @Inject(method = "destroyBlock", at = @At(value = "RETURN", ordinal = 3))
    public void hookBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        removeBlock(pos, false);
    }

    @Unique
    private boolean removeBlock(BlockPos p_180235_1_, boolean canHarvest) {
        BlockState state = this.level.getBlockState(p_180235_1_);
        boolean removed = ((IBlockStateEx)state).removedByPlayer(this.level, p_180235_1_, this.player, canHarvest, this.level.getFluidState(p_180235_1_));
        if (removed)
            state.getBlock().destroy(this.level, p_180235_1_, state);
        return removed;
    }
}
