package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;

public final class LichBossRoof extends TwilightJigsawPiece implements SpawnIndexProvider.Deny {
	public LichBossRoof(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_BOSS_ROOF.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	public LichBossRoof(StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_BOSS_ROOF.get(), 1, structureManager, TwilightForestMod.prefix("lich_tower/tower_boss_roof"), jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	@Override
	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
	}
}
