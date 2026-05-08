package twilightforest.init;

import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.util.BoundingBoxUtils;

public final class TFLandmark {
    private TFLandmark() {
    }

    public static BoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int spanX, int spanY, int spanZ, Direction dir, boolean centerBounds) {
        return BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, minX, minY, minZ, spanX, spanY, spanZ, dir, centerBounds);
    }
}
