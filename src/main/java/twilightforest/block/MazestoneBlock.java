package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.item.MazebreakerPickItem;

public class MazestoneBlock extends Block {

	public MazestoneBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		super.playerWillDestroy(level, pos, state, player);
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

		// damage the player's pickaxe
		if (!level.isClientSide() && !stack.isEmpty() && stack.getItem().canBeDepleted() && !(stack.getItem() instanceof MazebreakerPickItem)) {
			stack.hurtAndBreak(16, player, (user) -> user.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		}
	}
}
