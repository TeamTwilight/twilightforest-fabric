package carminite.extensions;

import carminite.hooks.EventHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockExtension {
	default boolean carminite$canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		return EventHooks.doPlayerHarvestCheck(player, state, level, pos);
	}
}