package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TFMain;
import twilightforest.init.TFStructurePieceTypes;

public class NagaCourtyardHedgeCapPillarComponent extends NagaCourtyardHedgeAbstractComponent {
	public NagaCourtyardHedgeCapPillarComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFNCCpP, nbt, TFMain.prefix("courtyard/hedge_end_pillar"), TFMain.prefix("courtyard/hedge_end_pillar_big"));
	}

	public NagaCourtyardHedgeCapPillarComponent(StructureTemplateManager manager, int i, int x, int y, int z, Rotation rotation) {
		super(manager, TFStructurePieceTypes.TFNCCpP, i, x, y, z, rotation, TFMain.prefix("courtyard/hedge_end_pillar"), TFMain.prefix("courtyard/hedge_end_pillar_big"));
	}
}