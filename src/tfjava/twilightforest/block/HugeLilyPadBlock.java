package twilightforest.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import twilightforest.enums.HugeLilypadPiece;

/**
 * Stub block class exposing FACING and PIECE properties referenced statically by
 * HugeLilypadFeature. Runtime block is the vanilla TFBlocks.HUGE_LILY_PAD alias
 * (lily_pad), which doesn't have these properties — feature code that calls
 * {@code defaultBlockState().setValue(FACING, ...)} on the vanilla block fails
 * silently (trySetValue returns the original state). This is acceptable; lilypads
 * still place, just not as 4-piece quads. Full fidelity needs a custom paired-client block.
 */
public final class HugeLilyPadBlock {
    public static final DirectionProperty FACING = net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<HugeLilypadPiece> PIECE = EnumProperty.create("piece", HugeLilypadPiece.class);

    private HugeLilyPadBlock() {
    }
}
