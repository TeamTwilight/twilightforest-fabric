package twilightforest.compat.clothConfig.configEntry;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ExtendedConfigEntry {

    private ExtendedConfigEntry(){
    }

    /**
     * Applies to double fields.
     * In a future version it will enforce bounds at deserialization.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface BoundedDouble {
        double min() default 0;

        double max();
    }

    /**
     * Applies to float fields.
     * In a future version it will enforce bounds at deserialization.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface BoundedFloat {
        float min() default 0;

        float max();
    }
}
