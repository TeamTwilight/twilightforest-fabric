package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CodexSaplingBlock extends SaplingBlock {

    public CodexSaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties, BlockState templateState) {
        super(treeGrower, properties);
    }

}