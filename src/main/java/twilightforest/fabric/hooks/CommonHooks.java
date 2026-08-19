package twilightforest.fabric.hooks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.fabric.events.neo.BreakBlockEvent;

public class CommonHooks {
	public static BreakBlockEvent fireBlockBreak(Level level, GameType gameType, Player player, BlockPos pos, BlockState state) {
		boolean preCancelEvent = false;

		ItemStack itemstack = player.getMainHandItem();
		if (!itemstack.isEmpty() && !itemstack.canDestroyBlock(state, level, pos, player)) {
			preCancelEvent = true;
		}

		if (player.blockActionRestricted(level, pos, gameType)) {
			preCancelEvent = true;
		}

		if (state.getBlock() instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
			preCancelEvent = true;
		}

		var event = new BreakBlockEvent(level, pos, state, player);
		event.setCanceled(preCancelEvent);
		event = event.post();

		if (event.isCanceled() && event.shouldNotifyClient() && player instanceof ServerPlayer sp) {
			sp.connection.send(new ClientboundBlockUpdatePacket(pos, state));
		}

		return event;
	}
}