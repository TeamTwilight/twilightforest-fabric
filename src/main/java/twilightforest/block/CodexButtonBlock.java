package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class CodexButtonBlock extends ButtonBlock {

    public CodexButtonBlock(BlockSetType blockSetType, int ticksToStayPressed, BlockBehaviour.Properties properties, BlockState templateState) {
        super(blockSetType, ticksToStayPressed, properties);
    }

}
