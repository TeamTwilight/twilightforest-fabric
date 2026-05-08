package twilightforest.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Centralised helpers for the F1.9 / F1.13 guard pattern: vanilla-TF code paths
 * call {@code state.setValue(DirectionalBlock.FACING, ...)} on FIREFLY / CICADA
 * (and similar critter blocks) assuming they expose the {@code FACING} property.
 * In this paired-client port those blocks are registered as plain {@code tfBlock(...)}
 * stubs without the property, so {@code setValue} throws
 * {@code IllegalArgumentException} during chunk generation. Each call site routes
 * through the helper so the property is only applied when the runtime block
 * actually declares it.
 */
public final class TFBlockStateHelper {

    private TFBlockStateHelper() {
    }

    public static BlockState setFacingIfPossible(BlockState state, Direction direction) {
        return state.hasProperty(DirectionalBlock.FACING)
                ? state.setValue(DirectionalBlock.FACING, direction)
                : state;
    }
}
