package twilightforest.util;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockCapabilityDirectionalCache<R> {

	private final BlockApiLookup<R, Direction> capability;

	private final Map<BlockPos, Map<Direction, BlockApiCache<R, Direction>>> data = new HashMap<>();

	public BlockCapabilityDirectionalCache(BlockApiLookup<R, Direction> capability) {
		this.capability = capability;
	}

	public List<R> getAllSides(ServerLevel level, BlockPos pos) {
		Map<Direction, BlockApiCache<R, Direction>> caches = data.get(pos);
		if (caches == null) {
			BlockPos key = pos.immutable();
			caches = new EnumMap<>(Direction.class);
			for (Direction side : Direction.values()) {
				caches.put(side, BlockApiCache.create(capability, level, key));
			}
			data.put(key, caches);
		}

		List<R> handlers = new ArrayList<>();
		for (Map.Entry<Direction, BlockApiCache<R, Direction>> entry : caches.entrySet()) {
			R handler = entry.getValue().find(entry.getKey());
			if (handler != null) {
				handlers.add(handler);
			}
		}
		return handlers;
	}

	public void invalidate(BlockPos pos) {
		data.remove(pos);
	}
}