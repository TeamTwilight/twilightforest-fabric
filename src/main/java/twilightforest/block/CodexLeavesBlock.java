package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CodexLeavesBlock extends LeavesBlock {

    public CodexLeavesBlock(BlockBehaviour.Properties properties, BlockState templateState) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(DISTANCE, 7)
                .setValue(PERSISTENT, true)
                .setValue(WATERLOGGED, false));
    }

}
