package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.block.ValidSpawnBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.RedThreadBlockEntity;
import twilightforest.init.TFBlocks;

import org.jetbrains.annotations.Nullable;

public class RedThreadBlock extends MultifaceBlock implements EntityBlock , ValidSpawnBlock {
	public RedThreadBlock(Properties properties) {
		super(properties);
	}

	public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
		return !ctx.getItemInHand().is(TFBlocks.RED_THREAD.get().asItem()) || super.canBeReplaced(state, ctx);
	}

	@Override
	public MultifaceSpreader getSpreader() {
		return new MultifaceSpreader(this);
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter getter, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return false;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedThreadBlockEntity(pos, state);
	}
}
