package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.block.CompressedBlock;

import java.util.Random;

@Mixin(FireBlock.class)
@Debug(export = true)
public class FireBlockMixin {

    @Unique
    private BlockPos currentPos;

    @Unique
    private ServerLevel currentLevel;

    //@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/Tag;)Z"))
    //public boolean isFireBlock(BlockState blockState, Tag<Block> tag) {
    //    return blockState.is(tag);
    //}

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/Tag;)Z"))
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo ci) {
        currentPos = pos;
        currentLevel = level;
    }

    @ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/Tag;)Z"))
    public boolean isFireBlock(boolean bl) {

        if(this.currentLevel.getBlockState(currentPos.below()).getBlock() instanceof CompressedBlock block){
            return block.isFireSource(this.currentLevel.getBlockState(currentPos.below()), this.currentLevel, currentPos, null); //Direction dosn't matter
        }
        else{
            return bl;
        }
    }
}
