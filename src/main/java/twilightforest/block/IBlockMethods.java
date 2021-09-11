package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

//TODO: Implement a mixin for each one of these properties somewhere in minecraft, to check, and do said special behaviour if the block has that property.
public interface IBlockMethods {
    default float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
        return 0.0f;
    }

    default int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 0;
    }

    default int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 0;
    }

    default boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
        return false;
    }

    default boolean isStickyBlock(BlockState state) {
        return false;
    }

    default boolean canCreatureSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, @Nullable EntityType<?> entityType) {
        return true;
    }
    default float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        return 0.0f;
    }
    default boolean canEntityDestroy(BlockState state, BlockPos pos, Entity entity) {
        return true;
    }

}
