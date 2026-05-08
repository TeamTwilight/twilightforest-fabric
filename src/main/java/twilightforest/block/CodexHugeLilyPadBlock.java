package twilightforest.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import twilightforest.enums.HugeLilypadPiece;

public class CodexHugeLilyPadBlock extends Block {

    public CodexHugeLilyPadBlock(BlockBehaviour.Properties properties, BlockState templateState) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HugeLilyPadBlock.FACING, Direction.NORTH)
                .setValue(HugeLilyPadBlock.PIECE, HugeLilypadPiece.NW));
    }

    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(HugeLilyPadBlock.FACING, rotation.rotate(state.getValue(HugeLilyPadBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HugeLilyPadBlock.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HugeLilyPadBlock.FACING, HugeLilyPadBlock.PIECE);
    }
}