package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BlockCapabilityDirectionalCache<R> {
	private final Map<BlockPosAndDirection, R> data = new HashMap<>();

	@SuppressWarnings("unchecked")
	public @Nullable R get(ServerLevel level, BlockPos pos, Direction direction) {
		BlockPosAndDirection key = new BlockPosAndDirection(pos.immutable(), direction);
		if (this.data.containsKey(key)) {
			return this.data.get(key);
		}

		BlockEntity blockEntity = level.getBlockEntity(pos);
		R value = null;
		if (blockEntity instanceof Container container) {
			value = (R) IItemHandler.of(container);
		}
		this.data.put(key, value);
		return value;
	}

	private record BlockPosAndDirection(BlockPos pos, Direction direction) {
	}
}
