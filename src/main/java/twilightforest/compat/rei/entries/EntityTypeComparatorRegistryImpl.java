package twilightforest.compat.rei.entries;

import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.impl.common.entry.comparison.EntryComparatorRegistryImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityTypeComparatorRegistryImpl extends EntryComparatorRegistryImpl<Entity, EntityType<Entity>> implements EntityTypeComparatorRegistry {

	public static final EntityTypeComparatorRegistryImpl INSTANCE = new EntityTypeComparatorRegistryImpl();

	@Override
	public EntityType<Entity> getEntry(Entity entity) {
		return (EntityType<Entity>) entity.getType();
	}

	@Override
	public EntryComparator<Entity> defaultComparator() {
		return EntityTypeComparatorRegistry.entityTypeNbt();
	}
}
