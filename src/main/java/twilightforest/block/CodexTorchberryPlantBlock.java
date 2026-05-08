package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CodexTorchberryPlantBlock extends Block {
    public static final BooleanProperty HAS_TORCHBERRIES = BooleanProperty.create("has_torchberries");


    public CodexTorchberryPlantBlock(BlockBehaviour.Properties properties, BlockState templateState) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_TORCHBERRIES, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_TORCHBERRIES);
    }
}