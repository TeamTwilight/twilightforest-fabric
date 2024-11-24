package twilightforest.compat.rei.entries;

import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparatorRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface EntityTypeComparatorRegistry extends EntryComparatorRegistry<Entity, EntityType<Entity>> {

	static EntryComparator<Entity> entityTypeNbt() {
		EntryComparator<Tag> nbtHasher = EntryComparator.nbt();
		return (context, entity) -> {
			CompoundTag tag = entity.saveWithoutId(new CompoundTag());
			return nbtHasher.hash(context, tag);
		};
	}

	default void registerNbt(EntityType<Entity> entityType) {
		register(entityTypeNbt(), entityType);
	}

	/**
	 * Registers entityTypes to compare via their nbt.
	 *
	 * @param entityTypes the entityTypes to compare
	 */
	default void registerNbt(EntityType<Entity>... entityTypes) {
		register(entityTypeNbt(), entityTypes);
	}
}
