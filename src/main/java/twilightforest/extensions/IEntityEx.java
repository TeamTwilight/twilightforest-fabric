package twilightforest.extensions;

import twilightforest.client.model.entity.PartEntity;
import twilightforest.world.components.TFTeleporter;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

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

    default Entity changeDimension(ServerLevel pServer, TFTeleporter teleporter) {
        return ((Entity)this).changeDimension(pServer);
    }

    default CompoundTag getPersistentData() {
        return null;
    };
}
