package twilightforest.util;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 * Fabric 兼容的 PieceBeardifierModifier 替代。
 * 在 NeoForge 中用于修改结构的地形平整范围，Fabric 中暂不实现。
 */
public class TFPieceBeardifierModifier {

	public static BoundingBox getCustomBoundingBox() {
		return null;
	}

	public static BoundingBox getCustomBoundingBoxWithGroundHeightDelta(int groundHeightDelta) {
		return null;
	}
}