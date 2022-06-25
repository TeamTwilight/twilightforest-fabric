package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import twilightforest.item.MazebreakerPickItem;

import org.jetbrains.annotations.Nullable;

/**
 * Castle block makes a castle
 *
 * @author Ben
 */
public class CastleBlock extends Block {

	public CastleBlock(MaterialColor color) {
		super(Properties.of(Material.STONE, color).requiresCorrectToolForDrops().strength(100.0F, 50.0F).sound(SoundType.STONE));
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
		ItemStack cei = player.getMainHandItem();
		if (cei.getItem() instanceof DiggerItem && !(cei.getItem() instanceof MazebreakerPickItem)) {
			cei.hurtAndBreak(16, player, (user) -> user.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		}

		super.playerDestroy(level, player, pos, state, te, stack);
	}
}
