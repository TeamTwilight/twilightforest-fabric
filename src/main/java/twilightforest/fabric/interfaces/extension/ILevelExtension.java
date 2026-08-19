package twilightforest.fabric.interfaces.extension;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import twilightforest.fabric.entity.PartEntity;

import java.util.Collection;

public interface ILevelExtension {
	default Collection<PartEntity<?>> twilightforest$getPartEntities() {
		return twilightforest$getPartEntityMap().values();
	}

	default Int2ObjectMap<PartEntity<?>> twilightforest$getPartEntityMap() {
		throw new AssertionError();
	}
}