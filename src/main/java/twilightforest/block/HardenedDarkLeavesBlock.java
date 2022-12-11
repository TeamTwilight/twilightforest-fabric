package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.block.CustomBurnabilityBlock;
import io.github.fabricators_of_create.porting_lib.block.ValidSpawnBlock;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;

public class HardenedDarkLeavesBlock extends Block implements BlockPickInteractionAware, ValidSpawnBlock, CustomBurnabilityBlock {

	public HardenedDarkLeavesBlock(Properties props) {
		super(props);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFireSpreadSpeed(), getFlammability());
	}

	public int getFlammability() {
		return 1;
	}

	public int getFireSpreadSpeed() {
		return 0;
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return false;
	}

	// fabric: getFireSpreadSpeed returns 0
	// fire blocks check that to see if they can burn stuff (getFlameOdds)
	// normally, if it's 0, it can't burn, so it doesn't tick or anything
	// however, forge patches fire and makes it actually checks getBurnOdds instead (getFlammability)
	// this method gives us parity with forge by allowing burning even with 0 spread speed
	@Override
	public boolean canBurn(BlockState state) {
		return true;
	}

	@Override
	public ItemStack getPickedStack(BlockState state, BlockGetter view, BlockPos pos, @Nullable Player player, @Nullable HitResult result) {
		return new ItemStack(TFBlocks.DARK_LEAVES.get());
	}
}
