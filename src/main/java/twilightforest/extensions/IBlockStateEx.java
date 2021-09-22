package twilightforest.extensions;

import twilightforest.block.IPlantable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface IBlockStateEx {

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param pos Block position in world
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @param fluid The current fluid and block state for the position in the world.
     * @return True if the block is actually destroyed.
     */
    default boolean removedByPlayer(Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        return ((IBlockMethods)((BlockState) this).getBlock()).removedByPlayer(((BlockState) this), world, pos, player, willHarvest, fluid);
    }

    /**
     * Determines if this block can support the passed in plant, allowing it to be planted and grow.
     * Some examples:
     *   Reeds check if its a reed, or if its sand/dirt/grass and adjacent to water
     *   Cacti checks if its a cacti, or if its sand
     *   Nether types check for soul sand
     *   Crops check for tilled soil
     *   Caves check if it's a solid surface
     *   Plains check if its grass or dirt
     *   Water check if its still water
     *
     * @param world The current world
     * @param facing The direction relative to the given position the plant wants to be, typically its UP
     * @param plantable The plant that wants to check
     * @return True to allow the plant to be planted/stay.
     */
    default boolean canSustainPlant(BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return ((IBlockMethods)((BlockState) this).getBlock()).canSustainPlant((BlockState) this, world, pos, facing, plantable);
    }
}
