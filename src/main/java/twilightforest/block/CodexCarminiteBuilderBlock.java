package twilightforest.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CodexCarminiteBuilderBlock extends Block {
    public static final EnumProperty<BuilderState> STATE = EnumProperty.create("state", BuilderState.class);


    public CodexCarminiteBuilderBlock(BlockBehaviour.Properties properties, BlockState templateState) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, BuilderState.BUILDER_INACTIVE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    public enum BuilderState implements StringRepresentable {
        BUILDER_ACTIVE("builder_active"),
        BUILDER_INACTIVE("builder_inactive"),
        BUILDER_TIMEOUT("builder_timeout");

        private final String serializedName;

        BuilderState(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public String getSerializedName() {
            return this.serializedName;
        }
    }
}