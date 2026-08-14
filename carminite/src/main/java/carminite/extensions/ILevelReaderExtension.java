package carminite.extensions;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;

public interface ILevelReaderExtension {
	private LevelReader self() {
		return (LevelReader) this;
	}

	default boolean carminite$isAreaLoaded(BlockPos center, int range) {
		return self().hasChunksAt(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	default <T> Holder<T> carminite$holderOrThrow(ResourceKey<T> key) {
		return this.self().registryAccess().carminite$holderOrThrow(key);
	}

	default <T> Optional<Holder.Reference<T>> carminite$holder(ResourceKey<T> key) {
		return this.self().registryAccess().carminite$holder(key);
	}
}