package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.world.components.feature.trees.growers.StructureTreeGrower;

public class CodexHollowOakSaplingBlock extends CodexSaplingBlock {

    public CodexHollowOakSaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties, BlockState templateState) {
        super(treeGrower, properties, templateState);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
            return;
        }

        StructureTreeGrower.growHollowTree(level, level.getChunkSource().getGenerator(), pos, state, random);
    }
}
