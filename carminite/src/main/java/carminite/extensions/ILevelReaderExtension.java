package carminite.extensions;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;

/**
 * Injected into LevelReader in twilightforest.classtweaker
 */
public interface ILevelReaderExtension {
	private LevelReader self() {
		return (LevelReader) this;
	}

	default boolean isAreaLoaded(BlockPos center, int range) {
		return self().hasChunksAt(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	default <T> Holder<T> holderOrThrow(ResourceKey<T> key) {
		return this.self().registryAccess().holderOrThrow(key);
	}

	default <T> Optional<Holder.Reference<T>> holder(ResourceKey<T> key) {
		return this.self().registryAccess().holder(key);
	}
}