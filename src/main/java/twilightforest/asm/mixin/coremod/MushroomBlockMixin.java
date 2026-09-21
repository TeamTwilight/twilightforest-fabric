package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.BlockHooks;

@Mixin(MushroomBlock.class)
public class MushroomBlockMixin {

	@ModifyReturnValue(
        method = "canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
        at = @At("RETURN")
    )
    private boolean twilightforest$modifySoilDecisionForMushroomBlockSurvivability(
        boolean original,
        BlockState state,
        LevelReader level,
        BlockPos pos
    ) {
        return BlockHooks.modifySoilDecisionForMushroomBlockSurvivability(original, level, pos);
    }
}