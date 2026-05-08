package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.util.PlayerHelper;

import java.util.List;

/**
 * 1:1 port of upstream {@code twilightforest.block.AuroraBrickBlock}.
 *
 * <p>Returns a much faster destroy progress (0.1F) once the breaking player has the
 * {@code progress_glacier} advancement, otherwise falls back to vanilla destroy speed.</p>
 */
public class AuroraBrickBlock extends Block {
	public AuroraBrickBlock(Properties properties) {
		super(properties);
	}

	@Override
	@SuppressWarnings("deprecation")
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		return PlayerHelper.playerHasRequiredAdvancements(player, List.of(TwilightForestMod.prefix("progress_glacier"))) ? 0.1F : super.getDestroyProgress(state, player, getter, pos);
	}
}
