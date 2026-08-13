package twilightforest.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class HollowLogItem extends BlockItem {
	private final Block horizontalLog;
	private final Block verticalLog;
	private final Block climbable;

	public HollowLogItem(Block horizontalLog, Block verticalLog, Block climbable, Properties properties) {
		super(verticalLog, properties);
		this.horizontalLog = horizontalLog;
		this.verticalLog = verticalLog;
		this.climbable = climbable;
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context) {
		return switch (context.getClickedFace().getAxis()) {
			case Y -> this.verticalLog.getStateForPlacement(context);
			case X, Z -> this.horizontalLog.getStateForPlacement(context);
		};
	}

	@Override
	public void registerBlocks(Map<Block, Item> blockItemMap, Item item) {
		super.registerBlocks(blockItemMap, item);
		blockItemMap.put(this.horizontalLog, item);
		blockItemMap.put(this.climbable, item);
	}
}