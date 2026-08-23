package twilightforest.world.components.structures.lichtowerrevamp;

import carminite.interfaces.markers.IPieceBeardifierModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TFMain;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;

public final class LichBossRoof extends TwilightJigsawPiece implements IPieceBeardifierModifier, SpawnIndexProvider.Deny {
	public LichBossRoof(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_BOSS_ROOF, compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	public LichBossRoof(StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_BOSS_ROOF, 1, structureManager, TFMain.prefix("lich_tower/tower_boss_roof"), jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	@Override
	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 0;
	}
}
