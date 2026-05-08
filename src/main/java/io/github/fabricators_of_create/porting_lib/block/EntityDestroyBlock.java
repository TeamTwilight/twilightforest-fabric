package io.github.fabricators_of_create.porting_lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface EntityDestroyBlock {
    default boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
        return true;
    }
}
