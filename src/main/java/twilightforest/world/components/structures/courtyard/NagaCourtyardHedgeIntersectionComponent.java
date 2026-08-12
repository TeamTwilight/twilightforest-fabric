package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TFMain;
import twilightforest.init.TFStructurePieceTypes;

public class NagaCourtyardHedgeIntersectionComponent extends NagaCourtyardHedgeAbstractComponent {
	public NagaCourtyardHedgeIntersectionComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFNCIs, nbt, TFMain.prefix("courtyard/hedge_intersection"), TFMain.prefix("courtyard/hedge_intersection_big"));
	}

	public NagaCourtyardHedgeIntersectionComponent(StructureTemplateManager manager, int i, int x, int y, int z, Rotation rotation) {
		super(manager, TFStructurePieceTypes.TFNCIs, i, x, y, z, rotation, TFMain.prefix("courtyard/hedge_intersection"), TFMain.prefix("courtyard/hedge_intersection_big"));
	}
}
