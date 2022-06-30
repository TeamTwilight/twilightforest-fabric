package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public class SpecialFlowerPotBlock extends FlowerPotBlock {

	public SpecialFlowerPotBlock(Supplier<? extends Block> flower, Properties properties) {
		super(flower.get(), properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if(!this.isEmpty()) {
			level.setBlock(pos, Blocks.FLOWER_POT.defaultBlockState(), 3);
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide());
		} else {
			return super.use(state, level, pos, player, hand, result);
		}
	}
}
