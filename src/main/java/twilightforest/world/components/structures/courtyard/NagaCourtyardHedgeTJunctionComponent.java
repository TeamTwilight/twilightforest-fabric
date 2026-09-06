package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TFCommon;
import twilightforest.init.TFStructurePieceTypes;

public class NagaCourtyardHedgeTJunctionComponent extends NagaCourtyardHedgeAbstractComponent {
	public NagaCourtyardHedgeTJunctionComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFNCT, nbt, TFCommon.prefix("courtyard/hedge_t"), TFCommon.prefix("courtyard/hedge_t_big"));
	}

	public NagaCourtyardHedgeTJunctionComponent(StructureTemplateManager manager, int i, int x, int y, int z, Rotation rotation) {
		super(manager, TFStructurePieceTypes.TFNCT, i, x, y, z, rotation, TFCommon.prefix("courtyard/hedge_t"), TFCommon.prefix("courtyard/hedge_t_big"));
	}
}