package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Q33 paired-client CakeBlock. Server keeps real {@link CakeBlock} eating
 * mechanics (BITES property decrements on right-click, restores hunger,
 * removes block when fully eaten). paired-client clients see the raw
 * {@code twilightforest:*} id; vanilla clients see a normal cake at the same
 * BITES level so the slice visualization matches.
 */
public class CodexCakeBlock extends CakeBlock {

    public CodexCakeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

}
