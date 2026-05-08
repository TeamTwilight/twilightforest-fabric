package twilightforest.world.components.structures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public abstract class TFStructureComponentTemplate extends StructurePiece {
    public final Runnable LAZY_TEMPLATE_LOADER = () -> {
    };

    protected TFStructureComponentTemplate(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
        super(type, genDepth, boundingBox);
    }

    protected TFStructureComponentTemplate(StructurePieceType type, CompoundTag tag) {
        super(type, tag);
    }
}
