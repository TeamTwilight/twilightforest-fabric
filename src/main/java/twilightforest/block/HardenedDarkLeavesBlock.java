package twilightforest.block;

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

public class HardenedDarkLeavesBlock extends Block implements BlockPickInteractionAware, ValidSpawnBlock {

	public HardenedDarkLeavesBlock(Properties props) {
		super(props);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFlammability(), getFireSpreadSpeed());
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

	@Override
	public ItemStack getPickedStack(BlockState state, BlockGetter view, BlockPos pos, @Nullable Player player, @Nullable HitResult result) {
		return new ItemStack(TFBlocks.DARK_LEAVES.get());
	}
}
