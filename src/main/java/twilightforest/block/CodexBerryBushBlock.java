package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CodexBerryBushBlock extends Block {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 0, 8);


    public CodexBerryBushBlock(BlockBehaviour.Properties properties, BlockState templateState) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 3)
                .setValue(LAYERS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LAYERS);
    }
}