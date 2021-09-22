package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import twilightforest.block.IPlantable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BushBlock.class)
public class BushBlockMixin implements IPlantable {
    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != (Object) this) return ((Block) (Object) this).defaultBlockState();
        return state;
    }
}
