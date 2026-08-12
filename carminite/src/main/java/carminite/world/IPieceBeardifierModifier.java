package carminite.world;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

public interface IPieceBeardifierModifier {
	BoundingBox getBeardifierBox();

	TerrainAdjustment getTerrainAdjustment();

	int getGroundLevelDelta();
}