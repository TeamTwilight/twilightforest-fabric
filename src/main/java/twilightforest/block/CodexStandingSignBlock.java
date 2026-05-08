package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class CodexStandingSignBlock extends StandingSignBlock {

    public CodexStandingSignBlock(WoodType woodType, BlockBehaviour.Properties properties, BlockState templateState) {
        super(woodType, properties);
    }

}
