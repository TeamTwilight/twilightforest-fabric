package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.HollowLogClimbable;
import twilightforest.block.HollowLogHorizontal;
import twilightforest.block.HollowLogVertical;

import java.util.Map;

public class HollowLogItem extends BlockItem {
	private final HollowLogHorizontal horizontalLog;
	private final HollowLogVertical verticalLog;
	private final HollowLogClimbable climbable;

	public HollowLogItem(RegistryObject<HollowLogHorizontal> horizontalLog, RegistryObject<HollowLogVertical> verticalLog, RegistryObject<HollowLogClimbable> climbable, Properties properties) {
		super(verticalLog.get(), properties);
		this.horizontalLog = horizontalLog.get();
		this.verticalLog = verticalLog.get();
		this.climbable = climbable.get();
		FuelRegistry.INSTANCE.add(this, 300);
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
		blockItemMap.put(this.verticalLog, item);
		blockItemMap.put(this.climbable, item);
	}

	@Override
	public void removeFromBlockToItemMap(Map<Block, Item> blockItemMap, Item item) {
		super.removeFromBlockToItemMap(blockItemMap, item);
		blockItemMap.remove(this.horizontalLog);
		blockItemMap.remove(this.verticalLog);
		blockItemMap.remove(this.climbable);
	}
}