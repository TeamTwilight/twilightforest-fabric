package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
public class SpecialFlowerPotBlock extends FlowerPotBlock {

	private final Block emptyPot;

	public SpecialFlowerPotBlock(Block emptyPot, Block flower, Properties properties) {
		super(flower, properties);
		this.emptyPot = emptyPot;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (!this.isEmpty()) {
			level.setBlock(pos, this.emptyPot.defaultBlockState(), Block.UPDATE_ALL);
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide());
		} else {
			return super.useWithoutItem(state, level, pos, player, result);
		}
	}
}
