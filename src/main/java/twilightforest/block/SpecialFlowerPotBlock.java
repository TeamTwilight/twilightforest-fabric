package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class SpecialFlowerPotBlock extends FlowerPotBlock {
	public SpecialFlowerPotBlock(Block potted, Properties properties) {
		super(potted, properties);
	}

	@Override
	protected InteractionResult useWithoutItem(
		BlockState state,
		Level level,
		BlockPos pos,
		Player player,
		BlockHitResult hitResult
	) {
		if (!this.isEmpty()) {
			level.setBlock(pos, Blocks.FLOWER_POT.defaultBlockState(), 3);
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.SUCCESS;
		}

		return super.useWithoutItem(state, level, pos, player, hitResult);
	}
}