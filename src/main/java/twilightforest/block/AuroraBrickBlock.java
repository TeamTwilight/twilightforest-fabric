package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TFMain;
import twilightforest.util.PlayerHelper;

public class AuroraBrickBlock extends Block {
	public AuroraBrickBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		return PlayerHelper.doesPlayerHaveRequiredAdvancements(player, TFMain.prefix("progress_glacier")) ? 0.1F : super.getDestroyProgress(state, player, getter, pos);
	}
}
