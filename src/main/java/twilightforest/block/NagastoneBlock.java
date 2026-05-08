package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import twilightforest.enums.NagastoneVariant;
import twilightforest.init.TFBlocks;

public class NagastoneBlock extends Block {
    public static final EnumProperty<NagastoneVariant> VARIANT = EnumProperty.create("variant", NagastoneVariant.class);

    public NagastoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(VARIANT, NagastoneVariant.SOLID));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction directionToNeighbor, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
        return this.getVariant(accessor, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getVariant(context.getLevel(), context.getClickedPos());
    }

    @SuppressWarnings("fallthrough")
    private BlockState getVariant(LevelAccessor accessor, BlockPos pos) {
        int connectionCount = 0;
        Direction[] facings = new Direction[2];
        for (Direction side : Direction.values()) {
            BlockState neighborState = accessor.getBlockState(pos.relative(side));
            if (neighborState.getBlock() == this || (neighborState.is(TFBlocks.NAGASTONE_HEAD.get()) && neighborState.hasProperty(DirectionalBlock.FACING) && side == neighborState.getValue(DirectionalBlock.FACING))) {
                facings[connectionCount++] = side;
                if (connectionCount >= 2) {
                    break;
                }
            }
        }
        return switch (connectionCount) {
            case 1 -> defaultBlockState().setValue(VARIANT, NagastoneVariant.getVariantFromDoubleFacing(facings[0], facings[0]));
            case 2 -> defaultBlockState().setValue(VARIANT, NagastoneVariant.getVariantFromDoubleFacing(facings[0], facings[1]));
            default -> this.defaultBlockState();
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }
}