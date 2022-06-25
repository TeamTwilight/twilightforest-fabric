package twilightforest.world.components.structures;

import twilightforest.init.TFLandmark;

@Deprecated
public interface LegacyLandmarkGetter {
    @Deprecated // For Legacy usage
    default void setFeature(TFLandmark type) {
        // Legacy hook, expect no-op
    }

    @Deprecated // Remove this whole class - These TFLandmark labels inside StructComps serve no purpose
    TFLandmark getFeatureType(); // Old method name
}
