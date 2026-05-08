package twilightforest.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public final class TFTransformations {

    private TFTransformations() {}

    /** Returns the transformation result, or null if no mapping exists. */
    public static EntityType<?> lookup(EntityType<?> from) {
        return TFDataMaps.getTransformationPowderResult(from);
    }

    /** Returns the transformation result for a living entity's type, or null. */
    public static EntityType<?> lookup(LivingEntity entity) {
        return lookup(entity.getType());
    }
}
