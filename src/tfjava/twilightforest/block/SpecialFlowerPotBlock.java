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
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * 1:1 port of upstream {@code twilightforest.block.SpecialFlowerPotBlock}. Adds an
 * empty-on-shift-use override so right-clicking a filled pot empties it back to the
 * empty pot variant (vanilla flower pots can't be emptied through the gameplay loop).
 */
public class SpecialFlowerPotBlock extends FlowerPotBlock {
	@Nullable
	private final Supplier<FlowerPotBlock> emptyPot;
	private final Supplier<? extends Block> flower;

	public SpecialFlowerPotBlock(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> flower, Properties properties) {
		super(flower.get(), properties);
		this.emptyPot = emptyPot;
		this.flower = flower;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (this.flower.get() != Blocks.AIR) {
			FlowerPotBlock pot = this.emptyPot == null ? (FlowerPotBlock) Blocks.FLOWER_POT : this.emptyPot.get();
			level.setBlock(pos, pot.defaultBlockState(), Block.UPDATE_ALL);
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide());
		} else {
			return super.useWithoutItem(state, level, pos, player, result);
		}
	}
}
