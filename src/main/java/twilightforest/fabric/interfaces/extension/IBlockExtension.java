package twilightforest.fabric.interfaces.extension;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.fabric.hooks.EventHooks;

public interface IBlockExtension {
	default boolean twilightforest$canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		return EventHooks.doPlayerHarvestCheck(player, state, level, pos);
	}
}