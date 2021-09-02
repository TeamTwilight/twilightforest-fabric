package twilightforest.extensions;

import twilightforest.client.model.entity.PartEntity;

import javax.annotation.Nullable;

public interface IEntityEx {
    default boolean isMultipartEntity()
    {
        return false;
    }

    @Nullable
    default PartEntity<?>[] getParts()
    {
        return null;
    }
}
