package twilightforest.block.entity.bookshelf;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import org.jetbrains.annotations.Nullable;

/**
 * Fabric-compatible replacement for NeoForge's IOwnedSpawner.
 * Provides the interface needed by BookshelfSpawner.
 */
public interface IOwnedSpawner {

	@Nullable
	SpawnData getNextSpawnData();

	void broadcastEvent(Level level, BlockPos pos, int id);

	void setEntityId(net.minecraft.world.entity.EntityType<?> type, @Nullable Level level, net.minecraft.util.RandomSource random, BlockPos pos);
}