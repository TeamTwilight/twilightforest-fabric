package twilightforest.fabric.entity;

import org.jspecify.annotations.Nullable;

public interface IMultiPartEntity {
	default boolean isMultipartEntity() {
		return false;
	}

	@Nullable
	default PartEntity<?> @Nullable [] getParts() {
		return null;
	}
}