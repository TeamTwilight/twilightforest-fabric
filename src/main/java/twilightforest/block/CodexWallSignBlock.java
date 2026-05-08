package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class CodexWallSignBlock extends WallSignBlock {

    public CodexWallSignBlock(WoodType woodType, BlockBehaviour.Properties properties, BlockState templateState) {
        super(woodType, properties);
    }

}
