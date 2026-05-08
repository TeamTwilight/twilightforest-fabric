package twilightforest.util;

import net.minecraft.core.BlockPos;

public final class LegacyLandmarkPlacements {
    private LegacyLandmarkPlacements() {
    }

    public static BlockPos getNearestCenterXZ(int chunkX, int chunkZ) {
        return twilightforest.util.landmarks.LegacyLandmarkPlacements.getNearestCenterXZ(chunkX, chunkZ);
    }
}
