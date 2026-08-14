package carminite.extensions;

import carminite.entity.PartEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.Collection;

public interface ILevelExtension {
	default Collection<PartEntity<?>> carminite$getPartEntities() {
		return carminite$getPartEntityMap().values();
	}

	default Int2ObjectMap<PartEntity<?>> carminite$getPartEntityMap() {
		throw new AssertionError();
	}
}