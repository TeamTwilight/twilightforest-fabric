package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import twilightforest.init.TFBlocks;

/**
 * 1:1 port of upstream {@code twilightforest.block.HardenedDarkLeavesBlock} — looks like
 * a Dark Tower exterior block but pick-block returns the soft DARK_LEAVES item (not its
 * own item form).
 *
 * <p>Codex Fabric port note: upstream NeoForge added a 5-arg
 * {@code getCloneItemStack(BlockState, HitResult, LevelReader, BlockPos, Player)} hook on
 * {@link Block}. Vanilla Fabric 1.21.1 only has the 3-arg
 * {@code getCloneItemStack(LevelReader, BlockPos, BlockState)} on {@code BlockStateBase}.
 * We override the 3-arg version (which is what vanilla pick-block actually invokes) and
 * keep the 5-arg method body 1:1 (without {@code @Override}) so future Fabric mixins or
 * registry hookups can re-attach the upstream signature.</p>
 */
public class HardenedDarkLeavesBlock extends Block {

	public HardenedDarkLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader reader, BlockPos pos, BlockState state) {
		return new ItemStack(TFBlocks.DARK_LEAVES.get());
	}

	// Codex Fabric port note: NF-only 5-arg signature; kept body 1:1 for future mixin/registry hookup.
	public ItemStack getCloneItemStack(BlockState state, HitResult result, LevelReader reader, BlockPos pos, Player player) {
		return new ItemStack(TFBlocks.DARK_LEAVES.get());
	}
}
