package io.github.fabricators_of_create.porting_lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;

public interface ExplosionResistanceBlock {
    default float getExplosionResistance(BlockState state, BlockGetter getter, BlockPos pos, Explosion explosion) {
        return state.getBlock().getExplosionResistance();
    }
}
